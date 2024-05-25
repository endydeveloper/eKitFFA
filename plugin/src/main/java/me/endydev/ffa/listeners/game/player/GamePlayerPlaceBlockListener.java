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
        if(!gameManager.isBuildMode(player.getUniqueId())) {
            if (event.getPlayer().hasPermission("thepit.admin")) {
                event.setCancelled(false);
                return;
            }
        }

        if (event.getBlock().getType().equals(Material.OBSIDIAN)) {
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    gameManager.removeObisidianBlock(event.getBlock());
                }
            }.runTaskLater(plugin, 20L * 120L);
            gameManager.addObsidianBlock(event.getBlock(), task);
            return;
        }

        if (event.getBlock().getTypeId() == 11 || event.getBlock().getTypeId() == 10) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getBlock().setType(Material.AIR);
                }
            }.runTaskLater(plugin, 100L);
            return;
        }

        event.setCancelled(true);
    }
}
