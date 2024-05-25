package me.endydev.ffa.inject;

import me.endydev.ffa.FFAPlugin;
import com.google.gson.Gson;
import me.endydev.ffa.api.handler.level.LevelHandler;
import me.endydev.ffa.api.handler.level.impl.LevelHandlerImpl;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.inject.modules.*;
import me.endydev.ffa.utils.PapiHook;
import me.endydev.ffa.utils.Utils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;
import team.unnamed.inject.Singleton;

import java.text.DecimalFormat;


public class BinderModule extends AbstractModule {
    private final FFAPlugin plugin;
    private final ConfigFile configFile;

    public BinderModule(FFAPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new ConfigFile(plugin, "config.yml");
    }

    @Override
    protected void configure() {
        this.bind(FFAPlugin.class).toInstance(this.plugin);
        this.bind(ConfigFile.class).toInstance(this.configFile);
        this.bind(Gson.class).toInstance(new Gson());
        this.bind(Utils.class).to(Utils.class).singleton();
        this.bind(PapiHook.class).singleton();

        this.bind(BukkitAudiences.class).toInstance(BukkitAudiences.create(plugin));

        this.install(new DatabaseModule());
        this.install(new FileModule(plugin));
        this.install(new VersionModule());
        this.install(new CacheModule());
        this.install(new TranslationModule(plugin));
        this.install(new RepositoryModule());
        this.install(new ListenerModule());
        this.install(new ManagerModule());
        this.install(new CommandModule());
        this.install(new TaskModule());
        this.install(new APIModule());
        this.install(new ServiceModule());
    }

    @Singleton
    @Provides
    public LevelHandler provideLevelHandler() {
        return new LevelHandlerImpl(
                new DecimalFormat("###,###"),
                new DecimalFormat("###,###.#")
        );
    }
}
