package me.endydev.ffa.inject.modules;

import com.zelicraft.core.spigot.api.CoreAPI;
import me.endydev.ffa.api.FFAAPI;
import me.endydev.ffa.api.SimpleFFAAPI;
import org.bukkit.Bukkit;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;
import team.unnamed.inject.Singleton;

public class APIModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(FFAAPI.class).to(SimpleFFAAPI.class).singleton();
    }

    @Singleton
    @Provides
    public CoreAPI provideCoreAPI() {
        return Bukkit.getServicesManager().getRegistration(CoreAPI.class).getProvider();
    }
}
