package me.endydev.ffa.translation.linguist;

import com.zelicraft.commons.shared.data.enums.Language;
import com.zelicraft.core.spigot.api.CoreAPI;
import lombok.RequiredArgsConstructor;
import me.yushust.message.language.Linguist;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class SenderLinguist implements Linguist<CommandSender> {
    private final CoreAPI coreAPI;

    @Override
    public @Nullable String getLanguage(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            return coreAPI.getPlayerLanguage(player.getUniqueId()).getCode();
        }
        return Language.getDefaultLanguage().getCode();
    }
}