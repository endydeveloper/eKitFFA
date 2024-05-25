package me.endydev.ffa.listeners.game.quit;

import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import team.unnamed.inject.Inject;

public class GameDataQuitListener implements Listener {

    @Inject
    private GameManager gameManager;

    @Inject
    private PlayerDataManager playerDataManager;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDataQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        playerDataManager.getPlayer(player.getUniqueId())
                .ifPresent(p -> gameManager.savePlayer(p));

        playerDataManager.removePlayer(player.getUniqueId());
    }
}
