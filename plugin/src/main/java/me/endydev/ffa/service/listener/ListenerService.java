package me.endydev.ffa.service.listener;

import me.endydev.ffa.FFAPlugin;
import com.zelicraft.commons.shared.services.Service;
import org.bukkit.event.Listener;
import team.unnamed.inject.Inject;

import java.util.Set;

public class ListenerService implements Service {
    @Inject
    private Set<Listener> listeners;

    @Inject
    private FFAPlugin plugin;

    @Override
    public void start() {
        listeners.forEach(x -> plugin.getServer().getPluginManager().registerEvents(x, plugin));
    }
}
