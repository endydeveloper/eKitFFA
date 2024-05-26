package me.endydev.ffa.listeners.game.quit;

import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.api.perks.PerkType;
import me.endydev.ffa.api.version.VersionSupport;
import me.endydev.ffa.cache.TagPlayer;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.managers.RegionManager;
import me.endydev.ffa.perks.Perk;
import me.endydev.ffa.repositories.FFAPlayerRepository;
import me.endydev.ffa.utils.Utils;
import me.yushust.message.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import team.unnamed.inject.Inject;

import java.util.List;
import java.util.Map;

public class GameTagQuitListener implements Listener {

    @Inject
    private VersionSupport versionSupport;

    @Inject
    private GameManager gameManager;

    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private FFAPlayerRepository ffaPlayerRepository;

    @Inject
    private RegionManager regionManager;

    @Inject
    private MessageHandler messageHandler;

    @Inject
    private FFAPlugin plugin;

    @Inject
    private ConfigFile configFile;

    @Inject
    private Map<PerkType, Perk> perkMap;

    @Inject
    private Utils utils;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);

        TagPlayer tagPlayer = gameManager.getPlayerTag(player);

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        playerDataManager.getPlayer(player.getUniqueId())
                .ifPresent(playerData -> playerData.setKillStreak(0));
        versionSupport.clearArrowsFromPlayerBody(player);
        if (!tagPlayer.hasAttackers()) {
            return;
        }

        Player attacker = tagPlayer.getLastAttacker();
        FFAPlayer attackerData = playerDataManager.getPlayer(attacker.getUniqueId()).orElse(null);
        if (attackerData == null) {
            return;
        }

        attackerData.addKillStreak();
        attackerData.addKills();
        if (attackerData.getKillStreak() > attackerData.getMaxKs()) {
            attackerData.setMaxKillStreak(attackerData.getKillStreak());
        }

        gameManager.rewardPlayer(attacker, GameManager.RewardType.KILL);
        utils.killStreak(attacker);

        if (ffaPlayerRepository.hasPerk(attacker.getUniqueId(), PerkType.GOLDEN_HEAD)) {
            perkMap.get(PerkType.GOLDEN_HEAD).runAction(player);
        } else {
            attacker.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
        }

        attacker.updateInventory();
        versionSupport.playAction(gameManager.getPlayerTag(player).getLastAttacker(),
                utils.replaceDeathMessage(messageHandler.get(player, "action-bar.kill"), player));
        utils.playSound(configFile.getConfigurationSection("sounds.kill"), gameManager.getPlayerTag(player).getLastAttacker());
        gameManager.givePerk(attacker);
        List<String> playerCause = messageHandler.getMany(player, "death-messages.player");
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(utils.replaceDeathMessage(playerCause.get(Utils.randomInt(0, playerCause.size() - 1)), player, gameManager.getPlayerTag(player).getLastAttacker()));
        }
    }
}
