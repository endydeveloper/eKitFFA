package me.endydev.ffa.inject.modules;

import me.endydev.ffa.managers.*;
import team.unnamed.inject.AbstractModule;

public class ManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(GameManager.class).singleton();
        this.bind(DuelManager.class).singleton();
        this.bind(PlayerDataManager.class).singleton();
        this.bind(RegionManager.class).singleton();
        this.bind(KitManager.class).singleton();
    }
}
