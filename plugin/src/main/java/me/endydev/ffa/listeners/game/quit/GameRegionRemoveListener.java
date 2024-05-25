package me.endydev.ffa.listeners.game.quit;

import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.managers.RegionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import team.unnamed.inject.Inject;

public class GameRegionRemoveListener implements Listener {

    @Inject
    private RegionManager regionManager;

    @Inject
    private PlayerDataManager playerDataManager;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (regionManager.getRegion() != null && regionManager.getRegion().contains(event.getPlayer().getLocation())) {
            playerDataManager.removePlayer(player.getUniqueId());
            return;
        }
    }
}
