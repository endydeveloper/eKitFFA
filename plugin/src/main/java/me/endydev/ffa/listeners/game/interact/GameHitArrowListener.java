package me.endydev.ffa.listeners.game.interact;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class GameHitArrowListener implements Listener {
    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        Projectile p = e.getEntity();
        if (p instanceof Arrow) {
            p.remove();
        }
    }
}
