package me.endydev.ffa.inject.modules;

import me.endydev.ffa.tasks.*;
import team.unnamed.inject.AbstractModule;

public class TaskModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(WorldTask.class).singleton();
        this.bind(TagTask.class).singleton();
        this.bind(DataSaverTask.class).singleton();
        this.bind(BlockRemoverTask.class).singleton();
        this.bind(DroppedItemsTask.class).singleton();
    }
}
