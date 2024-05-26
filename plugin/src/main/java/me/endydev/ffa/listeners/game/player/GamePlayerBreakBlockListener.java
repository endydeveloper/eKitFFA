package me.endydev.ffa.listeners.game.player;

import me.endydev.ffa.managers.GameManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import team.unnamed.inject.Inject;

public class GamePlayerBreakBlockListener implements Listener {
    @Inject
    private GameManager gameManager;

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(gameManager.isBuildMode(player.getUniqueId()) && player.hasPermission("thepit.admin")) {
            Block block = event.getBlock();
            if (gameManager.containsBlock(block.getLocation())) {
                gameManager.removeBlock(block.getLocation());
                return;
            }
            event.setCancelled(false);
            return;
        }

        event.setCancelled(true);
    }
}
