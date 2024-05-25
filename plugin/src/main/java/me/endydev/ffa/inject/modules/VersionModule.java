package me.endydev.ffa.inject.modules;

import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.api.version.VersionSupport;
import org.bukkit.Bukkit;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;
import team.unnamed.inject.Singleton;

import java.lang.reflect.InvocationTargetException;

public class VersionModule extends AbstractModule {
    private static final String version = Bukkit.getServer().getClass().getName().split("\\.")[3];

    @Singleton
    @Provides
    public VersionSupport provideVersionSupport(FFAPlugin plugin) {
        Class supp;

        try {
            supp = Class.forName("me.endydev.ffa.support.version." + version + "." + version);
        } catch (ClassNotFoundException e) {
            plugin.getLogger().severe("I can't run on your version: " + version);
            return null;
        }

        try {
           return (VersionSupport) supp.getConstructor(Class.forName("org.bukkit.plugin.Plugin"), String.class).newInstance(plugin, version);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                 ClassNotFoundException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Could not load support for server version: " + version);
            return null;
        }
    }
}
