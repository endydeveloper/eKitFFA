package me.endydev.ffa.managers;

import com.google.gson.Gson;
import com.zelicraft.commons.shared.cache.ObjectCache;
import com.zelicraft.core.spigot.api.CoreAPI;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.api.FFAAPI;
import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.api.data.TemporalBlock;
import me.endydev.ffa.api.data.TemporalDropItem;
import me.endydev.ffa.api.handler.level.LevelHandler;
import me.endydev.ffa.api.perks.PerkType;
import me.endydev.ffa.api.version.VersionSupport;
import me.endydev.ffa.cache.TagPlayer;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.database.Database;
import me.endydev.ffa.perks.Perk;
import me.endydev.ffa.repositories.FFAPlayerRepository;
import me.endydev.ffa.utils.Utils;
import me.yushust.message.MessageHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;
import team.unnamed.inject.Inject;

import java.util.*;

public class GameManager {

    @Inject
    private FFAPlugin plugin;

    @Inject
    private ObjectCache<UUID, TagPlayer> taggeds;

    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private MessageHandler messageHandler;

    @Inject
    private LevelHandler levelHandler;

    @Inject
    private ConfigFile config;

    @Inject
    private FFAAPI api;

    @Inject
    private Database database;

    @Inject
    private VersionSupport versionSupport;

    @Inject
    private ConfigFile configFile;

    @Inject
    private RegionManager regionManager;

    @Inject
    private Utils utils;

    @Inject
    private FFAPlayerRepository ffaPlayerRepository;

    @Inject
    private Gson gson;

    @Inject
    private ObjectCache<UUID, BukkitTask> teleportTask;

    @Inject
    private ObjectCache<UUID, Boolean> buildModeCache;

    @Inject
    private Map<PerkType, Perk> perkMap;

    @Inject
    private CoreAPI coreAPI;

    private static final Random RANDOM = new Random();

    @Inject
    private ObjectCache<UUID, TemporalDropItem> droppedItems;

    @Inject
    private ObjectCache<String, TemporalBlock> blockCache;


    private final List<String> playersDrop = new ArrayList<>();
    public Map<UUID, TemporalDropItem> getDroppedItems() {
        return droppedItems.getAll();
    }

    public TemporalDropItem addDroppedItem(Item item, int time) {
        long current = System.currentTimeMillis();
        UUID uuid = UUID.randomUUID();
        TemporalDropItem dropItem = TemporalDropItem.of(uuid, item, current+time);
        droppedItems.add(uuid, dropItem);
        return dropItem;
    }

    public void addDroppedItem(List<Item> items, int time) {
        for (Item item : items) {
            addDroppedItem(item, time);
        }
    }

    public void addTag(TagPlayer tagPlayer) {
        taggeds.add(tagPlayer.getUniqueId(), tagPlayer);
    }

    public void removeDroppedItem(UUID uuid) {
        droppedItems.remove(uuid);
    }

    public Map<String, TemporalBlock> getBlocks() {
        return blockCache.getAll();
    }

    public boolean containsBlock(Location location) {
        return blockCache.contains(Utils.locationToIdString(location));
    }

    public Optional<TemporalBlock> getBlock(Location location) {
        return blockCache.find(Utils.locationToIdString(location));
    }

    public void addBlock(Block block, int time) {
        String id = Utils.locationToIdString(block.getLocation());
        blockCache.add(id, TemporalBlock.of(
                id,
                block,
                block.getLocation(),
                System.currentTimeMillis()+time
        ));
    }

    public void removeBlock(Location location) {
        getBlock(location)
                .ifPresent(x -> {
                    x.getLocation().getBlock().setType(Material.AIR);
                    blockCache.remove(Utils.locationToIdString(location));
                });
    }

    public void assitKill(Player player) {
        TagPlayer tagPlayer = getPlayerTag(player);
        if(tagPlayer.getAssister() != null) {
            playerDataManager.getPlayer(tagPlayer.getAssister().getUniqueId())
                    .ifPresent(playerData -> {
                        rewardPlayer(tagPlayer.getAssister(), RewardType.ASSIST);
                        playerData.addAssist();
                        versionSupport.playAction(tagPlayer.getAssister(),
                                utils.replaceDeathMessage(messageHandler.get(player, "action-bar.assist"), player)
                        );
                        utils.playSound(config.getConfigurationSection("sounds.assist"), tagPlayer.getAssister());
                    });
        }
    }

    public boolean isBuildMode(UUID uuid) {
        return buildModeCache.find(uuid).orElse(false);
    }

    public boolean toggleBuildMode(UUID uuid) {
        boolean value = !isBuildMode(uuid);
        buildModeCache.add(uuid, value);

        return value;
    }

    public void setBuildMode(UUID uuid, boolean value) {
        if(!value) {
            buildModeCache.remove(uuid);
        } else {
            buildModeCache.add(uuid, true);
        }
    }

    public void savePlayer(FFAPlayer player) {
        ffaPlayerRepository.savePlayer(player);
    }

    public boolean containsRegion(Location location) {
        return regionManager.getRegion() == null || regionManager.getRegion().contains(location);
    }

    public void givePerk(Player player) {
        FFAPlayer ffaPlayer = playerDataManager.getPlayer(player.getUniqueId()).orElse(null);
        if(ffaPlayer == null) {
            return;
        }

        if (api.hasPerk(player.getUniqueId(), PerkType.GOLDEN_HEAD)) {
            perkMap.get(PerkType.GOLDEN_HEAD).runAction(player);
            return;
        }

        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
    }

    public void addTask(UUID uuid, BukkitTask bukkitTask) {
        teleportTask.add(uuid, bukkitTask);
    }

    public BukkitTask getTask(UUID uuid) {
        return teleportTask.find(uuid).orElse(null);
    }

    public void removeTask(UUID uuid) {
        teleportTask.remove(uuid);
    }

    public List<String> getPlayersFalled() {
        return playersDrop;
    }

    public void addPlayerFalled(String name) {
        if(!playersDrop.contains(name)) {
            playersDrop.add(name);
        }
    }

    public void removePlayerFalled(String name) {
        playersDrop.remove(name);
    }

    public TagPlayer getPlayerTag(Player player) {
        if(!taggeds.contains(player.getUniqueId())) {
            taggeds.add(player.getUniqueId(), new TagPlayer(player));
        }

        return taggeds.find(player.getUniqueId()).orElse(null);
    }

    public String getPlayerState(Player player) {
        if(getPlayerTag(player) != null && getPlayerTag(player).isTagged()) {
            return "combat";
        } else {
            return "free";
        }
    }

    public TagPlayer getAttackerTag(Player attacker) {
        for (TagPlayer tagPlayer : taggeds.findAll()) {
            if(tagPlayer == null) {
                return null;
            }

            if(tagPlayer.getLastAttacker() != null && tagPlayer.getLastAttacker().getName().equals(attacker.getName())) {
                return tagPlayer;
            }
        }

        return null;
    }

    public boolean containsTagAll(Player player) {
        for (TagPlayer tagPlayer : taggeds.findAll()) {
            if(tagPlayer == null) {
                return false;
            }
            if((tagPlayer.getUniqueId().equals(player.getUniqueId())) || (tagPlayer.getLastAttacker().getUniqueId() != null && tagPlayer.getLastAttacker().getName().equals(player.getName()))) {
                return true;
            }
        }
        return false;
    }

    public boolean containsTagPlayer(Player player) {
        for (TagPlayer tagPlayer : taggeds.findAll()) {
            if(tagPlayer == null) {
                return false;
            }
            if(tagPlayer.getUniqueId().equals(player.getUniqueId())) {
                return true;
            }
        }

        return false;
    }

    public boolean containsTagAttacker(Player player) {
        for (TagPlayer tagPlayer : taggeds.findAll()) {
            if(tagPlayer == null) {
                return false;
            }
            if(tagPlayer.getLastAttacker() != null && tagPlayer.getLastAttacker().getUniqueId().equals(player.getUniqueId())) {
                return true;
            }
        }

        return false;
    }

    public void addPlayerTag(Player victim, Player attacker) {
        if(teleportTask.contains(victim.getUniqueId())) {
            messageHandler.send(victim, "teleport.cancel");
            teleportTask.find(victim.getUniqueId()).ifPresent(BukkitTask::cancel);

            teleportTask.remove(victim.getUniqueId());
            utils.sendTitle("titles.spawn-cancelled", victim);
            utils.playSound(config.getConfigurationSection("sounds.spawn-cancelled"), victim);
        } else if(teleportTask.contains(attacker.getUniqueId())) {
            messageHandler.send(attacker, "teleport.cancel");
            teleportTask.find(attacker.getUniqueId()).ifPresent(BukkitTask::cancel);
            teleportTask.remove(victim.getUniqueId());
            utils.sendTitle("titles.spawn-cancelled", attacker);
            utils.playSound(config.getConfigurationSection("sounds.spawn-cancelled"), attacker);
        }

        TagPlayer tagPlayer = getPlayerTag(victim);

        tagPlayer.resetSeconds();
        tagPlayer.setTagged(true);
        tagPlayer.addAttacker(attacker);

        TagPlayer attackerTag = getPlayerTag(attacker);

        attackerTag.resetSeconds();
        attackerTag.setTagged(true);
    }

    public void removePlayerTag(Player player) {
        taggeds.remove(player.getUniqueId());
    }

    public void rewardPlayer(Player player, RewardType type) {
        String key = "rewards."+type.toString().toLowerCase();
        int maximumGold = config.getInt(key + ".gold.max", 10);
        boolean randomizeGolds = config.getBoolean(key + ".gold.randomize", true);
        int goldsToAdd = randomizeGolds ? RANDOM.nextInt(maximumGold) + 1 : maximumGold;

        int maximumXP = config.getInt(key + ".xp.max-amount", 10);
        boolean randomizeXP = config.getBoolean(key + ".types.xp.randomize", true);
        int xpToAdd = randomizeXP ? RANDOM.nextInt(maximumXP) + 1 : maximumXP;

        FFAPlayer ffaPlayer = playerDataManager.getPlayer(player.getUniqueId())
                .orElseThrow(() -> new IllegalStateException("unable to find player on cache"));

        ffaPlayer.addCoins(goldsToAdd);

        api.addXP(player, xpToAdd);

        messageHandler.sendReplacing(player, "misc.rewards." + type.toString().toLowerCase(),
                "%golds%", goldsToAdd,
                "%xp%", xpToAdd
        );
    }

    public void setKitToPlaye(Player player) {
        FFAPlayer ffaPlayer = playerDataManager.getPlayer(player.getUniqueId()).orElse(null);
        if(ffaPlayer == null) {
            return;
        }

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.getInventory().setArmorContents(null);
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            player.getInventory().setItem(i, new ItemStack(Material.AIR));
        }

        player.getInventory().setBoots(
                ItemBuilder
                        .from(Material.CHAINMAIL_BOOTS)
                        .unbreakable(true)
                        .flags(ItemFlag.HIDE_UNBREAKABLE)
                        .build()
        );

        player.getInventory().setLeggings(
                ItemBuilder
                        .from(Material.CHAINMAIL_LEGGINGS)
                        .unbreakable(true)
                        .flags(ItemFlag.HIDE_UNBREAKABLE)
                        .build()
        );

        player.getInventory().setChestplate(
                ItemBuilder
                        .from(Material.IRON_CHESTPLATE)
                        .unbreakable(true)
                        .flags(ItemFlag.HIDE_UNBREAKABLE)
                        .build()
        );

        player.getInventory().setHelmet(
                ItemBuilder.from(Material.IRON_HELMET)
                        .unbreakable(true)
                        .flags(ItemFlag.HIDE_UNBREAKABLE)
                        .build()
        );
        player.updateInventory();

        player.getInventory().setItem(0,
                ItemBuilder
                        .from(Material.IRON_SWORD)
                        .unbreakable(true)
                        .flags(ItemFlag.HIDE_UNBREAKABLE)
                        .build());

        if (api.hasPerk(player.getUniqueId(), PerkType.FISHING_ROD)) {
            perkMap.get(PerkType.FISHING_ROD).runAction(player);
        }

        perkMap.get(PerkType.BOW).runAction(player);

        player.getInventory().setItem(7, new ItemStack(Material.COOKED_BEEF, 16));
        player.getInventory().setItem(8, new ItemStack(Material.ARROW, 12));
        player.updateInventory();
    }

    public void setLevelBar(Player player) {
        playerDataManager.getPlayer(player.getUniqueId())
                .ifPresent(ffaPlayer -> {
                    int level = levelHandler.getLevelFromExperience(ffaPlayer.getXP());
                    player.setLevel(level);
                    player.setExp(calculatePercentage((float) ffaPlayer.getXP(), (float) levelHandler.getExperienceForLevel(level+1)));
                });
    }

    public float calculatePercentage(float value, float max) {
        if (max == 0) {
            throw new IllegalArgumentException("El valor máximo no puede ser cero");
        }

        return value / max;
    }

    public void executeDeathMessage(Player player, EntityDamageEvent.DamageCause cause, Entity damager) {
        TagPlayer tagPlayer = getPlayerTag(player);

        if (tagPlayer == null) {
            return;
        }

        boolean hasAttackers = tagPlayer.hasAttackers();
        Player attacker = tagPlayer.getLastAttacker();

        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();

        switch (cause) {
            case VOID:
                if (hasAttackers) {
                    versionSupport.playAction(tagPlayer.getLastAttacker(),
                            utils.replaceDeathMessage(messageHandler.get(player, "action-bar.kill"), player));
                    utils.playSound(configFile.getConfigurationSection("sounds.kill"), tagPlayer.getLastAttacker());
                    for (Player p : players) {
                        List<String> playerCause = messageHandler.getMany(p, "death-messages.void.player");
                        p.sendMessage(utils.replaceDeathMessage(playerCause.get(Utils.randomInt(0, playerCause.size() - 1)), player, tagPlayer.getLastAttacker()));

                    }
                } else {
                    for (Player p : players) {
                        List<String> soloCause = messageHandler.getMany(p, "death-messages.void.solo");
                        p.sendMessage(utils.replaceDeathMessage(soloCause.get(Utils.randomInt(0, soloCause.size() - 1)), player));
                    }
                }
                return;
            case ENTITY_ATTACK:
                if (hasAttackers) {
                    versionSupport.playAction(tagPlayer.getLastAttacker(), utils.replaceDeathMessage(messageHandler.get(player, "action-bar.kill"), player));
                    utils.playSound(configFile.getConfigurationSection("sounds.kill"), tagPlayer.getLastAttacker());
                    for (Player p : players) {
                        List<String> playerCause = messageHandler.getMany(p, "death-messages.player");
                        p.sendMessage(utils.replaceDeathMessage(playerCause.get(Utils.randomInt(0, playerCause.size() - 1)), player, tagPlayer.getLastAttacker()));
                    }
                }
                return;
            case FALL:
                if (hasAttackers) {
                    versionSupport.playAction(tagPlayer.getLastAttacker(), utils.replaceDeathMessage(messageHandler.get(player, "action-bar.kill"), player));
                    utils.playSound(configFile.getConfigurationSection("sounds.kill"), tagPlayer.getLastAttacker());
                    for (Player p : players) {
                        List<String> playerCause = messageHandler.getMany(player, "death-messages.fall.player");
                        p.sendMessage(utils.replaceDeathMessage(playerCause.get(Utils.randomInt(0, playerCause.size() - 1)), player, tagPlayer.getLastAttacker()));
                    }
                } else {
                    for (Player p : players) {
                        List<String> soloCause = messageHandler.getMany(player, "death-messages.fall.solo");
                        p.sendMessage(utils.replaceDeathMessage(soloCause.get(Utils.randomInt(0, soloCause.size() - 1)), player));
                    }
                }
                return;
            case LAVA:
                if (hasAttackers) {
                    versionSupport.playAction(tagPlayer.getLastAttacker(), utils.replaceDeathMessage(messageHandler.get(player, "action-bar.kill"), player));
                    utils.playSound(configFile.getConfigurationSection("sounds.kill"), tagPlayer.getLastAttacker());
                    for (Player p : players) {
                        List<String> playerCause = messageHandler.getMany(player, "death-messages.lava.player");
                        p.sendMessage(utils.replaceDeathMessage(playerCause.get(Utils.randomInt(0, playerCause.size() - 1)), player));
                    }
                } else {
                    for (Player p : players) {
                        List<String> soloCause = messageHandler.getMany(player, "death-messages.lava.solo");
                        p.sendMessage(utils.replaceDeathMessage(soloCause.get(Utils.randomInt(0, soloCause.size() - 1)), player));
                    }
                }
                return;
            case PROJECTILE: {
                if(damager == null) {
                    break;
                }

                if (damager instanceof Arrow) {
                    if (hasAttackers) {
                        versionSupport.playAction(tagPlayer.getLastAttacker(), utils.replaceDeathMessage(messageHandler.get(player, "action-bar.kill"), player));
                        utils.playSound(configFile.getConfigurationSection("sounds.kill"), tagPlayer.getLastAttacker());
                        for (Player p : players) {
                            List<String> bowCause = messageHandler.getMany(player, "death-messages.projectile.bow.player");
                            p.sendMessage(utils.replaceDeathMessage(bowCause.get(Utils.randomInt(0, bowCause.size() - 1)), player, tagPlayer.getLastAttacker()));

                        }
                    } else {
                        for (Player p : players) {
                            List<String> bowCause = messageHandler.getMany(player, "death-messages.projectile.bow.solo");
                            p.sendMessage(utils.replaceDeathMessage(bowCause.get(Utils.randomInt(0, bowCause.size() - 1)), player));
                        }
                    }
                }
                return;
            }
        }

        for (Player p : players) {
            List<String> unknownCause = messageHandler.getMany(player, "death-messages.unknown");
            p.sendMessage(utils.replaceDeathMessage(unknownCause.get(Utils.randomInt(0, unknownCause.size() - 1)), player));
        }
    }


    public enum RewardType {
        KILL,
        ASSIST;
    }
}
