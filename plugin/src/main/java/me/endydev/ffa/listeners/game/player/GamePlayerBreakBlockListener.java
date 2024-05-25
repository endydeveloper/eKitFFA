package me.endydev.ffa.listeners.game.player;

import me.endydev.ffa.managers.GameManager;
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
        if(!gameManager.isBuildMode(player.getUniqueId())) {
            if (event.getPlayer().hasPermission("thepit.admin")) {
                if (gameManager.getObsidianBlocks().containsKey(event.getBlock())) {
                    gameManager.removeObisidianBlock(event.getBlock());
                    return;
                }
                event.setCancelled(false);
                return;
            }
        }

        event.setCancelled(true);
    }
}
