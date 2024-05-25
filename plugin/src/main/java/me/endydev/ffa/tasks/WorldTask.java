package me.endydev.ffa.tasks;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldTask implements Runnable{
    @Override
    public void run() {
        for (World w : Bukkit.getServer().getWorlds()) {
            w.setTime(0L);
        }
    }
}
