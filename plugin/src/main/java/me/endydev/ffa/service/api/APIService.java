package me.endydev.ffa.service.api;

import me.endydev.ffa.FFAPlugin;
import com.zelicraft.commons.shared.services.Service;
import me.endydev.ffa.api.FFAAPI;
import org.bukkit.plugin.ServicePriority;
import team.unnamed.inject.Inject;

public class APIService implements Service {
    @Inject
    private FFAAPI api;

    @Inject
    private FFAPlugin plugin;

    @Override
    public void start() {
        plugin.getServer().getServicesManager().register(FFAAPI.class, api, plugin, ServicePriority.Normal);
    }

    @Override
    public void stop() {
        plugin.getServer().getServicesManager().unregister(FFAAPI.class, api);
    }
}