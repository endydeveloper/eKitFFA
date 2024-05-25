package me.endydev.ffa.commands;

import com.zelicraft.core.spigot.api.utils.Text;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Join;
import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.DuelManager;
import me.yushust.message.MessageHandler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

@Command("duel")
public class DuelCommand extends BaseCommand {
    @Inject
    private FFAPlugin plugin;

    @Inject
    private MessageHandler messageHandler;

    @Inject
    private DuelManager duelManager;

    @Inject
    private ConfigFile configFile;

    @Inject
    private BukkitAudiences bukkitAudiences;

    @Default
    public void duel(Player player, @Join String params) {
        String[] args = params.split(" ");
        if(!configFile.getBoolean("duels.enabled", false)) {
            player.sendMessage(Text.colorize("&cEsta función aún no esta activa, estamos trabajando para que pronto sea realidad."));
            return;
        }

        if (args.length == 0) {
            messageHandler.send(player, "duel.usage");
            return;
        }

        if (args[0].equalsIgnoreCase(player.getName())) {
            messageHandler.send(player, "duel.self");
            return;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if(target == null || !target.isOnline()) {
            messageHandler.send(player, "duel.notFound");
            return;
        }

        if (duelManager.isDueling(player.getUniqueId())) {
            messageHandler.send(player, "duel.alreadyDueling");
            return;
        }

        if (duelManager.isDueling(target.getUniqueId())) {
            messageHandler.send(player, "duel.targetAlreadyDueling");
            return;
        }

        duelManager.createDuelRequest(player.getUniqueId(), target.getUniqueId());
        messageHandler.sendReplacing(player, "duel.request-sent", "%player%", target.getName());

        for (String s : messageHandler.replacingMany(player, "duel.request-invite", "%player%", player.getName())) {
            bukkitAudiences.player(target).sendMessage(MiniMessage.miniMessage().deserialize(Text.parseColorToMiniMessage(s)));
        }
    }
}
