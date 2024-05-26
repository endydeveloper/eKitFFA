package me.endydev.ffa.menus.kits;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.Lists;
import com.zelicraft.commons.shared.cache.ObjectCache;
import com.zelicraft.core.spigot.api.utils.Text;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import me.endydev.ffa.api.data.kit.KitCreate;
import me.endydev.ffa.api.data.kit.KitInfo;
import me.endydev.ffa.api.menus.CoreBaseMenu;
import me.endydev.ffa.managers.KitManager;
import me.endydev.ffa.service.menu.MenuService;
import me.yushust.message.MessageHandler;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class KitCreatorSlotMenu extends CoreBaseMenu {
    @Inject
    private MessageHandler messageHandler;

    @Inject
    private KitManager kitManager;

    @Inject
    private ObjectCache<UUID, KitCreate> kitCache;

    @Inject
    private MenuService menuService;

    private final String KEY = "menus.kits.slotcreator.";


    @Override
    public void open(Player player, Object... objects) {
        Gui gui = Gui.gui()
                .rows(6)
                .disableAllInteractions()
                .title(LegacyComponentSerializer.legacySection().deserialize(messageHandler.get(player, KEY + "title")))
                .create();

        KitCreate kitCreate = kitCache.find(player.getUniqueId()).orElse(null);
        if (kitCreate == null) {
            return;
        }

        gui.setCloseGuiAction((close) -> {
            if (!menuService.hasIgnoreClickMenu(player.getUniqueId())) {
                kitCache.remove(player.getUniqueId());
            }
        });

        Set<Integer> usedSlots = kitManager.getKitsPage(kitCreate.getPage())
                .stream()
                .map(KitInfo::getSlot)
                .collect(Collectors.toSet());

        gui.getFiller().fillBetweenPoints(0, 0, 6, 9, ItemBuilder
                .from(XMaterial.GREEN_STAINED_GLASS_PANE.parseItem())
                .setName(Text.colorize("&a"))
                .asGuiItem(event -> {
                    kitCreate.setSlot(event.getSlot());
                    kitCache.add(player.getUniqueId(), kitCreate);
                    menuService.getLastBackHistory(player.getUniqueId())
                            .flatMap(x -> menuService.getMenu(x))
                            .ifPresent(menu -> {
                                menuService.setIgnoreClickMenu(player.getUniqueId());
                                menu.open(player, kitCreate.getName());
                            });
                }));

        for (Integer usedSlot : usedSlots) {
            gui.setItem(usedSlot, ItemBuilder
                    .from(XMaterial.RED_STAINED_GLASS_PANE.parseItem())
                    .setName(Text.colorize("&f"))
                    .asGuiItem());
        }

        List<Integer> otherSlots = Lists.newArrayList(
                getSlotFromRowCol(6, 1),
                getSlotFromRowCol(6, 9)
        );
        for (Integer otherSlot : otherSlots) {
            gui.setItem(otherSlot, ItemBuilder.from(Material.AIR).asGuiItem());
        }

        gui.open(player);
    }

    @Override
    public String getTitle(@Nullable Player player, Object... replace) {
        return null;
    }
}
