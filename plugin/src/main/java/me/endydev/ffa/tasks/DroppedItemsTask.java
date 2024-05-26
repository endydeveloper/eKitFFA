package me.endydev.ffa.tasks;

import me.endydev.ffa.managers.GameManager;
import team.unnamed.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DroppedItemsTask implements Runnable {
    @Inject
    private GameManager gameManager;

    @Override
    public void run() {
        long current = System.currentTimeMillis();
        List<UUID> toRemove = new ArrayList<>();
        gameManager.getDroppedItems()
                .values()
                .forEach(dropItem -> {
                    if(dropItem.getUntil() > current) {
                        return;
                    }

                    dropItem.getItem().remove();
                    toRemove.add(dropItem.getUuid());
                });

        for (UUID uuid : toRemove) {
            gameManager.removeDroppedItem(uuid);
        }
    }
}
