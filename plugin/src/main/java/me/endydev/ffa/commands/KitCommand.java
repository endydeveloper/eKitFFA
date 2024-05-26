package me.endydev.ffa.commands;

import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.KitManager;
import me.endydev.ffa.menus.kits.KitsMenu;
import me.yushust.message.MessageHandler;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

@Command("kit")
public class KitCommand extends BaseCommand {

    @Inject
    private KitManager kitManager;

    @Inject
    private MessageHandler messageHandler;

    @Inject
    private GameManager gameManager;

    @Inject
    private KitsMenu kitsMenu;

    @Default
    public void kitCommand(Player player, @Optional String name) {
        if(!gameManager.containsRegion(player.getLocation())) {
            messageHandler.send(player, "kit.use.outside");
            return;
        }

        if(name == null) {
            kitsMenu.open(player);
            return;
        }

        kitManager
                .getKit(name)
                .ifPresent(kit -> kit.load(player));
    }
}
