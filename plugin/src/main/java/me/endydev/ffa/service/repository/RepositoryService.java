package me.endydev.ffa.service.repository;

import com.zelicraft.commons.shared.services.Service;
import me.endydev.ffa.repositories.FFAPlayerRepository;
import team.unnamed.inject.Inject;

public class RepositoryService implements Service {
    @Inject
    private FFAPlayerRepository ffaPlayerRepository;

    @Override
    public void start() {
        ffaPlayerRepository.createTable();
    }
}
