package me.endydev.ffa.inject.modules;

import me.endydev.ffa.repositories.FFAPlayerRepository;
import team.unnamed.inject.AbstractModule;

public class RepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(FFAPlayerRepository.class).singleton();
    }
}
