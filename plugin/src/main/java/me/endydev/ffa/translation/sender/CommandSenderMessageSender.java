package me.endydev.ffa.translation.sender;

import com.cryptomorin.xseries.messages.Titles;
import me.yushust.message.send.MessageSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSenderMessageSender implements MessageSender<CommandSender> {
    @Override
    public void send(CommandSender sender, String mode, String msg) {
        if(mode == null || mode.trim().isEmpty()) {
            sender.sendMessage(msg);
            return;
        }

        switch (mode.toLowerCase()) {
            case "kick": {
                if(!(sender instanceof Player)) {
                    return;
                }

                Player player = (Player) sender;
                player.kickPlayer(msg);
                break;
            }

            case "title": {
                String[] split = msg.split(";;");

                if(split.length != 2 || !(sender instanceof Player)) {
                    return;
                }


                Player player = (Player) sender;
                Titles.sendTitle(player, split[0], split[1]);
                break;
            }
            default:
                sender.sendMessage(msg);
        }
    }
}
