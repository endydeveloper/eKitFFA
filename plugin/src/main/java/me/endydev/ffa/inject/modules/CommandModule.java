package me.endydev.ffa.inject.modules;

import me.endydev.ffa.FFAPlugin;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.BaseCommand;
import me.endydev.ffa.commands.DuelCommand;
import me.endydev.ffa.commands.KitCommand;
import me.endydev.ffa.commands.SpawnCommand;
import me.endydev.ffa.commands.ThePITCommand;
import org.bukkit.command.CommandSender;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;
import team.unnamed.inject.Singleton;

public class CommandModule extends AbstractModule {
    @Override
    protected void configure() {
        this.multibind(BaseCommand.class)
                .asSet()
                .to(ThePITCommand.class)
                .to(SpawnCommand.class)
                .to(DuelCommand.class)
                .to(KitCommand.class)
                .singleton();
    }

    @Singleton
    @Provides
    public BukkitCommandManager<CommandSender> provideBukkitCommandManager(FFAPlugin plugin) {
        return BukkitCommandManager.create(plugin);
    }
}
