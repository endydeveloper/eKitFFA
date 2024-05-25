package me.endydev.ffa.api.version;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class VersionSupport {
    private static String name2;
    private final Plugin plugin;

    public VersionSupport(Plugin plugin, String versionName) {
        name2 = versionName;
        this.plugin = plugin;
    }

    /**
     * Void damage with cause
     */
    public abstract void voidKill(Player p);

    /**
     * Hide player armor to a player
     */
    public abstract void hideArmor(Player victim, Player receiver);

    /**
     * Show a player armor
     */
    public abstract void showArmor(Player victim, Player receiver);
    public abstract void onUseGoldenHead(Player p);
    public abstract void respawnPlayer(Player p);
    public abstract void clearArrowsFromPlayerBody(Player player);
    public abstract void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut);

    /**
     * Send action-bar message
     */
    public abstract void playAction(Player p, String text);

    public void spigotShowPlayer(Player victim, Player receiver) {
        receiver.showPlayer(victim);
    }

    public void spigotHidePlayer(Player victim, Player receiver) {
        receiver.hidePlayer(victim);
    }


    public static String getName() {
        return name2;
    }

    public abstract int getVersion();

    public Plugin getPlugin() {
        return plugin;
    }
}
