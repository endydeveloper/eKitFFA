package me.endydev.ffa.inject.modules;

import me.endydev.ffa.tasks.DataSaverTask;
import me.endydev.ffa.tasks.TagTask;
import me.endydev.ffa.tasks.WorldTask;
import team.unnamed.inject.AbstractModule;

public class TaskModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(WorldTask.class).singleton();
        this.bind(TagTask.class).singleton();
        this.bind(DataSaverTask.class).singleton();
    }
}
