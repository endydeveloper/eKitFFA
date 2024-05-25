package me.endydev.ffa.listeners.game.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class GameFlowLavaListener implements Listener {
    @EventHandler
    public void flowLava(BlockFromToEvent event) {
        if (event.getBlock().getTypeId() == 11 || event.getBlock().getTypeId() == 10) {
            event.setCancelled(true);
        }
    }
}
