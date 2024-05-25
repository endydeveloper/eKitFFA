package me.endydev.ffa.listeners;

import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.managers.RegionManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import team.unnamed.inject.Inject;

public class RegionListener implements Listener {
    @Inject
    private RegionManager regionManager;

    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private GameManager gameManager;

    @Inject
    private FFAPlugin plugin;

    @EventHandler
    public void regionMoveCheckEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(regionManager.getRegion() != null && regionManager.getRegion().contains(player.getLocation())) {
            gameManager.removePlayerFalled(player.getName());
        }
    }

    @EventHandler
    public void interactEntity(PlayerInteractEvent event) {
        if(regionManager.getRegion() != null && regionManager.getRegion().contains(event.getPlayer().getLocation())) {
            Player player = event.getPlayer();
            if(!gameManager.isBuildMode(player.getUniqueId())) {
                if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType() != Material.ENDER_CHEST) {
                    if(!event.getPlayer().hasPermission("thepit.admin")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if(regionManager.getRegion() != null && regionManager.getRegion().contains(player.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                Player player = (Player) event.getEntity();
                if(regionManager.getRegion() != null && regionManager.getRegion().contains(player.getLocation())) {
                    event.setCancelled(true);
                    return;
                }
                if(regionManager.getRegion() != null && !regionManager.getRegion().contains(player.getLocation()) && !gameManager.getPlayersFalled().contains(player.getName())) {
                    gameManager.addPlayerFalled(player.getName());
                    event.setCancelled(true);
                }
            }
        }
    }
}
