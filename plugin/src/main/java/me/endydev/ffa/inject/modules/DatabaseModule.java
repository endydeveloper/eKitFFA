package me.endydev.ffa.inject.modules;

import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.database.Database;
import org.bukkit.configuration.ConfigurationSection;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;
import team.unnamed.inject.Singleton;

public class DatabaseModule extends AbstractModule {

    @Singleton
    @Provides
    public Database provideDatabase(ConfigFile config) {
        ConfigurationSection section = config.getConfigurationSection("mysql");
        return new Database(section.getString("host"), section.getInt("port"), section.getString("database"), section.getString("user"), section.getString("password"));
    }
}
