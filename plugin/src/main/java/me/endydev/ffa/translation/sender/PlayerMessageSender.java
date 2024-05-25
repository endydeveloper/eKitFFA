package me.endydev.ffa.translation.sender;

import com.zelicraft.commons.shared.repositories.user.dto.User;
import me.yushust.message.send.MessageSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerMessageSender implements MessageSender<User> {
    @Override
    public void send(User playerGlobal, String mode, String message) {
        Player player = Bukkit.getPlayer(playerGlobal.getUniqueId());
        if (player == null) {
            return;
        }

        player.sendMessage(message);
    }
}
