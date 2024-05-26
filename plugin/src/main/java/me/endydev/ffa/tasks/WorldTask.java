package me.endydev.ffa.tasks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WorldTask implements Runnable {
    @Override
    public void run() {
        for (World w : Bukkit.getServer().getWorlds()) {
            w.setTime(0L);
            for (Entity entity : w.getEntities()) {
                if(entity instanceof Player) {
                    continue;
                }

                entity.remove();
            }
        }
    }
}
