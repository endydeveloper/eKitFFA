package me.endydev.ffa.listeners.game.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class GameCreatureSpawnListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {
            return;
        }

        event.setCancelled(true);
    }
}
