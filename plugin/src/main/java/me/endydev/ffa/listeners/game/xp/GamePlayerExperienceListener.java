package me.endydev.ffa.listeners.game.xp;

import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.configuration.ConfigFile;
import me.yushust.message.MessageHandler;
import org.bukkit.event.Listener;
import team.unnamed.inject.Inject;

import java.text.DecimalFormat;

public class GamePlayerExperienceListener implements Listener {

    @Inject
    private FFAPlugin plugin;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###,###");

    @Inject
    private MessageHandler messageHandler;

    @Inject
    private ConfigFile configFile;

}
