package me.endydev.ffa.tasks;

import me.endydev.ffa.managers.GameManager;
import org.bukkit.Location;
import org.bukkit.Material;
import team.unnamed.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlockRemoverTask implements Runnable {
    @Inject
    private GameManager gameManager;

    @Override
    public void run() {
        long current = System.currentTimeMillis();
        List<Location> toRemove = new ArrayList<>();
        gameManager.getBlocks()
                .values()
                .forEach(block -> {
                    if(block.getBlock().getType().equals(Material.AIR)) {
                        toRemove.add(block.getLocation());
                        return;
                    }
                    if(block.getUntil() > current) {
                        return;
                    }

                    block.getLocation().getBlock().setType(Material.AIR);
                    toRemove.add(block.getLocation());
                });

        for (Location location : toRemove) {
            gameManager.removeBlock(location);
        }
    }
}
