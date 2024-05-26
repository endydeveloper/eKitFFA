package me.endydev.ffa.listeners.game.player;

import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.PlayerDataManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import team.unnamed.inject.Inject;

public class GamePlayerPlaceBlockListener implements Listener {
    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private GameManager gameManager;

    @Inject
    private FFAPlugin plugin;

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(gameManager.isBuildMode(player.getUniqueId()) && event.getPlayer().hasPermission("thepit.admin")) {
            event.setCancelled(false);
            return;
        }

        if(gameManager.containsRegion(event.getBlock().getLocation())) {
            event.setCancelled(true);
            return;
        }

        if (event.getBlock().getType().equals(Material.WATER) || event.getBlock().getType().equals(Material.LAVA)) {

            return;
        }

        gameManager.addBlock(event.getBlock(), 30000);
    }
}
