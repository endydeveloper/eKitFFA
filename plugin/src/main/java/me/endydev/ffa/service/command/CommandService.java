package me.endydev.ffa.service.command;

import lombok.var;
import me.endydev.ffa.FFAPlugin;
import com.google.common.collect.Lists;
import com.zelicraft.commons.shared.services.Service;
import com.zelicraft.commons.shared.utils.Permissions;
import com.zelicraft.core.spigot.api.CoreAPI;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.message.MessageKey;
import dev.triumphteam.cmd.core.message.context.MessageContext;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import me.yushust.message.MessageHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.zelicraft.commons.spigot.utils.TextUtils.colorize;

public class CommandService implements Service {

    @Inject
    private Set<BaseCommand> commands;

    @Inject
    private MessageHandler messageHandler;

    @Inject
    private CoreAPI coreAPI;

    @Inject
    private BukkitCommandManager<CommandSender> commandRepository;

    @Inject
    private FFAPlugin plugin;

    @Override
    public void start() {
        CommandSender sender = plugin.getServer().getConsoleSender();
        sender.sendMessage(colorize("&6-> [Commands] Registering commands..."));
        this.configureMessage(BukkitMessageKey.NO_PERMISSION, "command.no-permission");
        this.configureMessage(BukkitMessageKey.CONSOLE_ONLY, "command.console-only");
        this.configureMessage(BukkitMessageKey.PLAYER_ONLY, "command.player-only");
        this.configureMessage(MessageKey.UNKNOWN_COMMAND, "command.unknown-command");
        this.configureMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, "command.not-enough-arguments");
        this.configureMessage(MessageKey.TOO_MANY_ARGUMENTS, "command.too-many-arguments");
        this.configureMessage(MessageKey.INVALID_ARGUMENT, "command.invalid-argument");

        registerSuggestions();

        commands.forEach(commandRepository::registerCommand);
        sender.sendMessage(colorize("&a-> [Commands] Commands registered!"));
        sender.sendMessage("");
    }

    @Override
    public void stop() {
        commands.forEach(commandRepository::unregisterCommand);
    }

    private void configureMessage(BukkitMessageKey<? extends MessageContext> key, String path) {
        commandRepository.registerMessage(key, (sender, context) ->
                messageHandler.send(
                        sender, path.replace("%command%", context.getCommand())
                                .replace("%subcommand%", context.getSubCommand())
                )
        );
    }

    private void configureMessage(MessageKey<? extends MessageContext> key, String path) {
        commandRepository.registerMessage(key, (sender, context) ->
                messageHandler.send(
                        sender, path.replace("%command%", context.getCommand())
                                .replace("%subcommand%", context.getSubCommand())
                )
        );
    }

    private void registerSuggestions() {
        commandRepository.registerSuggestion(SuggestionKey.of("players"), (sender, context) -> {
            String playerInput = context.getArgs().get(context.getArgs().size() - 1);
            if(playerInput.length() < 1) {
                return new ArrayList<>();
            }

            boolean isPlayer = sender instanceof Player;
            var allPlayers =  plugin.getServer().getOnlinePlayers();
            List<UUID> ignoredPlayers = new ArrayList<>();
            if (isPlayer && !sender.hasPermission(Permissions.VANISH_ADMIN_PERMISSION)) {
                ignoredPlayers.addAll(coreAPI.getVanishRepository().getVanishOfPlayers(allPlayers.stream().map(Player::getUniqueId).collect(Collectors.toSet())));
            }

            return allPlayers
                    .stream()
                    .filter(x -> !ignoredPlayers.contains(x.getUniqueId()))
                    .map(Player::getName)
                    .filter(s -> s.toLowerCase().startsWith(playerInput.toLowerCase()))
                    .collect(Collectors.toList());
        });

        commandRepository.registerSuggestion(SuggestionKey.of("option_months"), (sender, ctx) -> Lists.newArrayList(
                "add",
                "remove",
                "set"
        ));
    }
}
