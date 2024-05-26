package me.endydev.ffa.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.api.FFAAPI;
import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.api.handler.level.LevelHandler;
import me.endydev.ffa.api.version.VersionSupport;
import me.endydev.ffa.cache.TagPlayer;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.PlayerDataManager;
import me.yushust.message.MessageHandler;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import team.unnamed.inject.Inject;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    @Inject
    private ConfigFile configFile;

    @Inject
    private MessageHandler messageHandler;

    @Inject
    private FFAAPI api;

    @Inject
    private FFAPlugin plugin;

    @Inject
    private GameManager gameManager;

    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private LevelHandler levelHandler;

    @Inject
    private VersionSupport versionSupport;

    public List<String> getList(CommandSender sender, String path) {
        return messageHandler.getMany(sender, path);
    }

    public static String CC(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /*
    public ItemBuilder createItem(ConfigurationSection section, Player player) {
        ItemBuilder item = new ItemBuilder(Material.BEDROCK);
        boolean hasMaterial = section.get("material") != null;
        Material material = null;
        boolean hasData = section.get("data") != null;
        boolean hasHideFlags = section.get("hideFlags") != null;
        boolean hasAmount = section.get("amount") != null;
        boolean hasGlow = section.get("glowing") != null;
        boolean hasSkullData = section.get("skull") != null;
        boolean hasDisplayName = section.get("displayName") != null;
        boolean hasLore = section.get("lore") != null;
        String title;
        if (hasMaterial) {
            title = section.getString("material").toUpperCase();
            if (title.contains(",")) {
                String[] materials = title.split(",");
                title = materials[(new Random()).nextInt(materials.length - 1)];
            }

            try {
                material = Material.valueOf(title);
            } catch (IllegalArgumentException var18) {
                plugin.getLogger().warning("&cMaterial on " + section.getName() + " is not valid.");
            }

            if (material != null) {
                item.setMaterial(material);
            }
        }

        if (hasData) {
            int data = section.getInt("data");
            item.setData((short)data);
        }

        if (hasAmount) {
            int amount = section.getInt("amount");
            item.setAmount(amount);
        }

        if (hasGlow) {
            boolean glowing = section.getBoolean("glowing");
            item.setGlow(glowing);
        }

        if (material == Material.SKULL_ITEM && hasSkullData) {
            String skullData = PlaceholderAPI.setPlaceholders(player, section.getString("skull"));
            if (skullData.startsWith("base64-")) {
                skullData = skullData.replace("base64-", "");
                item.setItem(SkullUtils.getHead(skullData));
            } else if (skullData.startsWith("uuid-")) {
                skullData = skullData.replace("uuid-", "");
                UUID uuid = UUID.fromString(skullData);
                item.setItem(SkullUtils.getHead(uuid));
            } else if (skullData.startsWith("player-")) {
                skullData = skullData.replace("player-", "");
                OfflinePlayer pf = Bukkit.getOfflinePlayer(skullData);
                item.setItem(SkullUtils.getHead(pf));
            } else if (skullData.startsWith("url-")) {
                skullData = skullData.replace("url-", "");
                skullData = "http://textures.minecraft.net/texture/" + skullData;
                item.setItem(SkullUtils.getHead(skullData));
            }

            if (skullData.startsWith("name-")) {
                skullData = skullData.replace("name-", "");
                item.setSkullOwner(skullData);
            }
        }

        if (hasDisplayName) {
            title = new Text(section.getString("displayName")).placeholder(player);
            item.setName(title);
        }

        if (hasLore) {
            List<String> lore = PlaceholderAPI.setPlaceholders(player, section.getStringList("lore"));
            item.setLore(lore);
        }

        if (hasHideFlags) {
            boolean hideFlags = section.getBoolean("hideFlags");
            item.setHideFlags(hideFlags);
        }

        return item;
    }

     */

    public void sendPlayer(Player var1, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        var1.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    public static String locationToIdString(Location location) {
        return location.getBlockX()+";"+location.getBlockY()+";"+location.getBlockZ()+";"+location.getWorld().getName();
    }

    public void sendTitle(String key, Player player) {
        ConfigurationSection section = configFile.getConfigurationSection(key);
        if(section.getBoolean("enabled")) {
            int fadeIn = section.getInt("fadeIn");
            int stay = section.getInt("stay");
            int fadeOut = section.getInt("fadeOut");
            String title = messageHandler.get(player, key + ".title");
            String subtitle = messageHandler.get(player, key +".subtitle");
            versionSupport.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    public void sendTitle(String key, Player player, int seconds) {
        ConfigurationSection section = configFile.getConfigurationSection(key);
        if(section.getBoolean("enabled")) {
            int fadeIn = section.getInt("fadeIn");
            int stay = section.getInt("stay");
            int fadeOut = section.getInt("fadeOut");
            String title = messageHandler.replacing(player, key + ".title", "%seconds%", String.valueOf(seconds));
            String subtitle = messageHandler.replacing(player, key +".subtitle", "%seconds%", String.valueOf(seconds));
            versionSupport.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    public static void playSound(String sound, int volume, float pitch, Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void playSound(Sound sound, int volume, float pitch, Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public void playSound(ConfigurationSection section, Player player) {
        if(section.getBoolean("enabled")) {
            if(section.get("sound") == null) return;
            if(section.get("volume") == null) return;
            if(section.get("pitch") == null) return;
            try {
                Sound.valueOf(section.getString("sound").toUpperCase());
            } catch (Exception e) {
                return;
            }
            Sound sound = Sound.valueOf(section.getString("sound").toUpperCase());
            int volume = section.getInt("volume");
            float pitch = section.getInt("pitch");
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public int executeMultiplier(Player player, int amount) {
        FFAPlayer ffaPlayer = api.getPlayer(player.getUniqueId()).orElse(null);
        if(ffaPlayer == null) {
            return 1;
        }

        if(!configFile.contains("multipliers") || configFile.getConfigurationSection("multipliers").getKeys(false).size() < 1) return amount;

        List<String> multipliers = new ArrayList<>(configFile.getConfigurationSection("multipliers").getKeys(false));
        multipliers.sort(new MultipliersComparator());

        for(String multiplier : multipliers) {
            System.out.println(multiplier);
        }
        return amount;
    }

    private class MultipliersComparator implements Comparator<String> {
        public int compare(String s1, String s2) {
            double v1 = configFile.getDouble("multipliers." + s1);
            double v2 = configFile.getDouble("multipliers." + s2);
            return Double.compare(v1, v2);
        }
    }

    public void killStreak(Player player) {
        playerDataManager.getPlayer(player.getUniqueId())
                .ifPresent(playerData -> {
                    for (String key : configFile.getConfigurationSection("kill-streak").getKeys(false)) {
                        try {
                            Integer.parseInt(key);
                        } catch (Exception e) {
                            continue;
                        }
                        int k = Integer.parseInt(key);
                        if(playerData.getKillStreak() == k) {
                            playerData.addCoins(executeMultiplier(player, configFile.getInt("kill-streak." + key + ".gold")));
                            int totalXP = executeMultiplier(player, configFile.getInt("kill-streak." + key + ".exp"));
                            playerData.addXP(totalXP);
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendMessage(replaceDeathMessage(configFile.getString("kill-streak." + key + ".message"), player));
                            }
                            return;
                        }
                    }
                });
    }

    public static ItemStack setUnbreakable(ItemStack itemStack) {
        return ItemBuilder.from(itemStack)
                .unbreakable(true)
                .flags(ItemFlag.HIDE_UNBREAKABLE)
                .build();
    }

    public void addGold(Player player) {
        FFAPlayer ffaPlayer = playerDataManager.getPlayer(player.getUniqueId()).orElse(null);
        if(ffaPlayer == null) {
            return;
        }

        int goldToAdd = configFile.getInt("kill.gold");
        ffaPlayer.addCoins(executeMultiplier(player, goldToAdd));
    }

    public static String calculateTimeAgo(long time) {
        long currentTimeMillis = System.currentTimeMillis();

        long timeDiffMillis = currentTimeMillis - time;
        long seconds = timeDiffMillis / 1000;

        long years = seconds / (60 * 60 * 24 * 365);
        seconds -= years * (60 * 60 * 24 * 365);
        long months = seconds / (60 * 60 * 24 * 30);
        seconds -= months * (60 * 60 * 24 * 30);
        long days = seconds / (60 * 60 * 24);
        seconds -= days * (60 * 60 * 24);
        long hours = seconds / (60 * 60);
        seconds -= hours * (60 * 60);
        long minutes = seconds / 60;
        seconds -= minutes * 60;

        StringBuilder sb = new StringBuilder();
        if (years > 0) {
            sb.append(years).append(" año").append(years > 1 ? "s" : "").append(" ");
        }
        if (months > 0) {
            sb.append(months).append(" mes").append(months > 1 ? "es" : "").append(" ");
        }
        if (days > 0) {
            sb.append(days).append(" dia").append(days > 1 ? "s" : "").append(" ");
        }
        if (hours > 0) {
            sb.append(hours).append(" hora").append(hours > 1 ? "s" : "").append(" ");
        }
        if (minutes > 0) {
            sb.append(minutes).append(" minuto").append(minutes > 1 ? "s" : "").append(" ");
        }
        if (seconds > 0) {
            sb.append(seconds).append(" segundo").append(seconds > 1 ? "s" : "").append(" ");
        }

        String timeAgo = sb.toString().trim();
        return timeAgo.isEmpty() ? "justo ahora" : "hace " + timeAgo;
    }

    public void addXP(Player player) {
        FFAPlayer ffaPlayer = api.getPlayer(player.getUniqueId()).orElse(null);

        int xpToAdd = executeMultiplier(player, configFile.getInt("kill.exp"));
        int level = levelHandler.getLevelFromExperience(ffaPlayer.getXP());

        ffaPlayer.addXP(xpToAdd);

        int neededXP = 5 * (level * level) + 80 * level + 100;

        if(ffaPlayer.getXP() > neededXP) {
            ffaPlayer.setLevel(level+1);
            gameManager.setLevelBar(player);
            versionSupport.playAction(player, messageHandler.getMessage("action-bar.level-up"));
        }
    }

    public String replaceDeathMessage(String text, Player victim, Player attacker) {
        text = text.replaceAll("%victim%", victim.getName());
        text = text.replaceAll("%killer%", attacker.getName());
        return text;
    }

    public String replaceDeathMessage(String text, Player victim) {
        text = text.replaceAll("%victim%", victim.getName());
        text = text.replaceAll("%player%", victim.getName());
        return text;
    }

    public static Location getLocationSection(ConfigurationSection section) {
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        float yaw = (float) section.getDouble("yaw");
        float pitch = (float) section.getDouble("pitch");
        World world = Bukkit.getWorld(section.getString("world"));
        if(world == null) {
            return null;
        }

        return new Location(world, x, y, z, yaw, pitch);
    }

    public void addLocationBlock(Location location, String path) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        String world = location.getWorld().getName();
        List<String> locs = configFile.getStringList("enderChests");
        locs.add(world+";"+x+";"+y+";"+z);
        configFile.set(path, locs);
        configFile.save();
    }

    public static Location getLocationBlock(String location) {
        String[] params = location.split(";");
        double x = Double.parseDouble(params[1]);
        double y = Double.parseDouble(params[2]);
        double z = Double.parseDouble(params[3]);
        World world = Bukkit.getWorld(params[0]);
        return new Location(world, x, y, z);
    }

    public void setLocationToString(Location location, String path) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        String world = location.getWorld().getName();
        configFile.set(path, world+";"+x+";"+y+";"+z+";"+yaw+";"+pitch);
    }

    public static Location getLocationFromString(String location) {
        String[] params = location.split(";");
        double x = Double.parseDouble(params[1]);
        double y = Double.parseDouble(params[2]);
        double z = Double.parseDouble(params[3]);
        float yaw = Float.parseFloat(params[4]);
        float pitch = Float.parseFloat(params[5]);
        World world = Bukkit.getWorld(params[0]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static Location getRegionLocationSection(ConfigurationSection section) {
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        World world = Bukkit.getWorld(section.getString("world"));

        return new Location(world, x, y, z);
    }

    public void setRegionLocationConfig(String key, Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        String worldName = location.getWorld().getName();
        configFile.set(key+"x", x);
        configFile.set(key+"y", y);
        configFile.set(key+"z", z);
        configFile.set(key+"world", worldName);
        configFile.save();
    }

    public void setLocationConfig(ConfigurationSection section, Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        String worldName = location.getWorld().getName();
        section.set("x", x);
        section.set("y", y);
        section.set("z", z);
        section.set("yaw", yaw);
        section.set("pitch", pitch);
        section.set("world", worldName);
        configFile.save();
    }

}
