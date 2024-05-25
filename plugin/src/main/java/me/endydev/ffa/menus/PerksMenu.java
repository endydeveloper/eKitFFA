package me.endydev.ffa.menus;

import com.zelicraft.core.spigot.api.menus.CoreBaseMenu;
import com.zelicraft.core.spigot.api.menus.dto.CreateItemSkullLore;
import dev.triumphteam.gui.guis.Gui;
import me.yushust.message.MessageHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

@Singleton
public class PerksMenu extends CoreBaseMenu {
    @Inject
    private MessageHandler messageHandler;

    @Override
    public void open(Player player, Object... objects) {
        String key = "ffa.menus.perks.";
        Gui gui = Gui.gui()
                .rows(3)
                .disableAllInteractions()
                .title(MiniMessage.miniMessage().deserialize(messageHandler.get(player, key+"title")))
                .create();

        gui.setItem(2, 3, generateItemLore(
                CreateItemSkullLore.builder()
                        .player(player)
                        .texture("")
                        .messageHandler(messageHandler)
                        .key(key)
                        .build()
        ).asGuiItem());
    }

    @Override
    public String getTitle(@Nullable Player player, Object... replace) {
        return null;
    }
}
