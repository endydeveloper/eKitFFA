package me.endydev.ffa.tasks;

import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

public class WorldTask implements Runnable {

    @Inject
    private ConfigFile configFile;


    @Override
    public void run() {
        if(configFile.contains("spawn")) {
            Location location = Utils.getLocationSection(configFile.getConfigurationSection("spawn"));
            if(location == null) {
                return;
            }

            World world = location.getWorld();

            world.setTime(0L);

            for (Entity entity : world.getEntities()) {
                if(entity instanceof Player || entity instanceof ArmorStand) {
                    continue;
                }

                entity.remove();
            }

            for (LivingEntity entity : world.getLivingEntities()) {
                if(entity instanceof Player || entity instanceof ArmorStand) {
                    continue;
                }

                entity.remove();
            }
        }

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
