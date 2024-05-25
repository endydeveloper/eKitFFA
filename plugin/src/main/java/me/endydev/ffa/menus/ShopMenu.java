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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class ShopMenu extends CoreBaseMenu {
    @Inject
    private MessageHandler messageHandler;

    private final Map<UUID, Integer> pageMap = new HashMap<>();

    @Override
    public void update(Player player) {
        super.update(player);
    }

    @Override
    public void open(Player player, Object... objects) {
        String key = "ffa.menus.shop.";
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

        gui.open(player);
    }

    @Override
    public String getTitle(@Nullable Player player, Object... replace) {
        return null;
    }
}
