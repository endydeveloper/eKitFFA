package me.endydev.ffa.listeners.game.quit;

import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import team.unnamed.inject.Inject;

public class GameQuitSpawnListener implements Listener {

    @Inject
    private ConfigFile configFile;

    @Inject
    private Utils utils;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!configFile.contains("spawn")) {
            return;
        }

        player.teleport(utils.getLocationSection(configFile.getConfigurationSection("spawn")));
    }
}
