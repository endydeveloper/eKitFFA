package me.endydev.ffa.listeners.game.player;

import me.endydev.ffa.managers.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import team.unnamed.inject.Inject;

public class GamePlayerDropItemListener implements Listener {
    @Inject
    private GameManager gameManager;

    @EventHandler
    public void dropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if(!gameManager.isBuildMode(player.getUniqueId())) {
            if (event.getPlayer().hasPermission("thepit.admin")) {
                event.setCancelled(false);
                return;
            }
        }

        event.getItemDrop().remove();
        event.setCancelled(true);
    }
}
