package me.endydev.ffa.listeners.game.player.death;

import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.api.version.VersionSupport;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.utils.Utils;
import me.yushust.message.MessageHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import team.unnamed.inject.Inject;

public class GameRespawnListener implements Listener {

    @Inject
    private ConfigFile configFile;

    @Inject
    private VersionSupport versionSupport;

    @Inject
    private Utils utils;

    @Inject
    private MessageHandler messageHandler;

    @Inject
    private GameManager gameManager;

    @Inject
    private FFAPlugin plugin;

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        new BukkitRunnable() {
            @Override
            public void run() {
                versionSupport.respawnPlayer(player);
                versionSupport.clearArrowsFromPlayerBody(player);
                player.teleport(utils.getLocationSection(configFile.getConfigurationSection("spawn")));
                utils.sendTitle("titles.death", player);
                utils.playSound(configFile.getConfigurationSection("sounds.death"), player);
                gameManager.setKitToPlayer(player);

                for (Player target : plugin.getServer().getOnlinePlayers()) {
                    versionSupport.hideArmor(player, target);
                }

                for (Player target : plugin.getServer().getOnlinePlayers()) {
                    versionSupport.showArmor(player, target);
                }
            }
        }.runTaskLater(plugin, 5L);
    }
}
