package me.endydev.ffa.listeners.game.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class GameEntityExplodeListener implements Listener {

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        e.blockList().clear();
    }
}
