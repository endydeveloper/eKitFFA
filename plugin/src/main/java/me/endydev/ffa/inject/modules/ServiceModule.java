package me.endydev.ffa.inject.modules;

import com.zelicraft.commons.shared.services.Service;
import me.endydev.ffa.service.MainService;
import me.endydev.ffa.service.api.APIService;
import me.endydev.ffa.service.command.CommandService;
import me.endydev.ffa.service.listener.ListenerService;
import me.endydev.ffa.service.menu.MenuService;
import me.endydev.ffa.service.repository.RepositoryService;
import me.endydev.ffa.service.task.TaskService;
import team.unnamed.inject.AbstractModule;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(Service.class)
                .to(MainService.class)
                .singleton();

        this.bind(Service.class)
                .named("listener")
                .to(ListenerService.class)
                .singleton();

        this.bind(Service.class)
                .named("api")
                .to(APIService.class)
                .singleton();

        this.bind(Service.class)
                .named("command")
                .to(CommandService.class)
                .singleton();

        this.bind(Service.class)
                .named("task")
                .to(TaskService.class)
                .singleton();

        this.bind(Service.class)
                .named("repository")
                .to(RepositoryService.class)
                .singleton();

        this.bind(Service.class)
                .named("menu")
                .to(MenuService.class)
                .singleton();
    }
}
