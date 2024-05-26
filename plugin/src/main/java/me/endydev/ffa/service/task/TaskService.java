package me.endydev.ffa.service.task;

import me.endydev.ffa.FFAPlugin;
import com.zelicraft.commons.shared.services.Service;
import me.endydev.ffa.tasks.DataSaverTask;
import me.endydev.ffa.tasks.DroppedItemsTask;
import me.endydev.ffa.tasks.TagTask;
import me.endydev.ffa.tasks.WorldTask;
import team.unnamed.inject.Inject;

public class TaskService implements Service {
    @Inject
    private WorldTask worldTask;

    @Inject
    private TagTask tagTask;

    @Inject
    private DataSaverTask dataSaverTask;

    @Inject
    private DroppedItemsTask droppedItemsTask;

    @Inject
    private FFAPlugin plugin;

    @Override
    public void start() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, worldTask,  0L, 10000L);
        plugin.getServer().getScheduler().runTaskTimer(plugin, tagTask, 0L, 20L);
        plugin.getServer().getScheduler().runTaskTimer(plugin, droppedItemsTask, 20L, 20L);
        int SAVE_TIME = 5 * (60 * 20);
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, dataSaverTask, SAVE_TIME, SAVE_TIME);
    }
}
