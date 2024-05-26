package me.endydev.ffa.menus.kits;

import com.cryptomorin.xseries.XMaterial;
import com.zelicraft.core.spigot.api.menus.dto.CreateItemLore;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.api.data.kit.KitInfo;
import me.endydev.ffa.api.handler.level.LevelHandler;
import me.endydev.ffa.api.menus.CoreBaseMenu;
import me.endydev.ffa.managers.KitManager;
import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.service.menu.MenuService;
import me.endydev.ffa.service.menu.dto.ActiveMenu;
import me.yushust.message.MessageHandler;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Singleton
public class KitsPreviewMenu extends CoreBaseMenu {
    @Inject
    private MessageHandler messageHandler;

    @Inject
    private MenuService menuService;

    @Inject
    private KitManager kitManager;

    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private LevelHandler levelHandler;

    private final String KEY = "menus.kits.preview.";

    private final Map<UUID, Integer> pageMap = new HashMap<>();

    @Override
    public void update(Player player) {
        ActiveMenu activeMenu = menuService.getActiveGUI(player.getUniqueId()).orElse(null);
        if (activeMenu == null) {
            return;
        }

        Gui gui = (Gui) activeMenu.getGui();

        kitManager.getKit(activeMenu.getObject(0))
                .ifPresent(kit -> {
                    gui.getGuiItems().clear();
                    FFAPlayer ffaPlayer = playerDataManager.getPlayer(player.getUniqueId()).orElse(null);
                    if (ffaPlayer == null) {
                        return;
                    }

                    Map<Integer, ItemStack> map = kit.getInventory();
                    for (int i = 0; i <= 8; i++) {
                        ItemStack itemStack = map.get(i);
                        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
                            continue;
                        }
                        gui.setItem(6, i + 1, ItemBuilder.from(itemStack.clone())
                                .setNbt("disabled-item", true)
                                .asGuiItem());
                    }

                    for (int i = 9; i <= 35; i++) {
                        ItemStack itemStack = map.get(i);
                        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {
                            continue;
                        }
                        gui.setItem(i + 9, ItemBuilder.from(itemStack.clone())
                                .setNbt("disabled-item", true)
                                .asGuiItem());
                    }

                    gui.getFiller()
                            .fillBetweenPoints(0, 0, 2, 9,
                                    ItemBuilder.from(XMaterial.BLACK_STAINED_GLASS_PANE.parseItem())
                                            .asGuiItem());

                    gui.setItem(8, generateItemLore(
                            CreateItemLore.builder()
                                    .key("menus.items.close")
                                    .itemStack(XMaterial.BOOK.parseItem())
                                    .player(player)
                                    .messageHandler(messageHandler)
                                    .build()
                    ).asGuiItem(event -> {
                        gui.close(player);
                    }));

                    menuService.getLastBackHistory(player.getUniqueId())
                            .flatMap(x -> menuService.getMenu(x))
                            .ifPresent(menu -> {
                                gui.setItem(8, generateItemLore(
                                        CreateItemLore.builder()
                                                .key("menus.items.back")
                                                .itemStack(XMaterial.BOOK.parseItem())
                                                .player(player)
                                                .messageHandler(messageHandler)
                                                .replacing(new Object[]{
                                                        "%menu%", menu.getTitle(player)
                                                })
                                                .build()
                                ).asGuiItem(event -> {
                                    menuService.removeLastBackHistory(player.getUniqueId());
                                    menuService.setClickBackMenu(player.getUniqueId());
                                    menu.open(player);
                                }));
                            });

                    gui.setItem(0, generateArmorItem(39, player, map)
                            .asGuiItem());

                    gui.setItem(1, generateArmorItem(38, player, map)
                            .asGuiItem());
                    gui.setItem(9, generateArmorItem(37, player, map)
                            .asGuiItem());
                    gui.setItem(10, generateArmorItem(36, player, map)
                            .asGuiItem());

                    gui.setItem(2, generateArmorItem(40, player, map)
                            .asGuiItem());

                    gui.update();
                });
    }

    public ItemBuilder generateArmorItem(int slot, Player player, Map<Integer, ItemStack> map) {
        ItemStack item = map.get(slot);
        if (item != null && !item.getType().equals(Material.AIR)) {
            return ItemBuilder.from(item.clone())
                    .setNbt("disabled-item", true);
        } else {
            return generateItemLore(CreateItemLore.builder()
                    .key(KEY + "items.none")
                    .messageHandler(messageHandler)
                    .player(player)
                    .itemStack(XMaterial.BARRIER.parseItem())
                    .build());
        }
    }

    @Override
    public void open(Player player, Object... objects) {
        if (objects.length < 1) {
            return;
        }

        Gui gui = Gui.gui()
                .rows(6)
                .disableAllInteractions()
                .title(LegacyComponentSerializer.legacySection().deserialize(messageHandler.get(player, KEY + "title")))
                .create();

        gui.open(player);

        menuService.setActiveGUI(player.getUniqueId(), ActiveMenu.builder()
                .gui(gui)
                .objects(objects)
                .build());

        update(player);
    }

    @Override
    public String getTitle(@Nullable Player player, Object... replace) {
        return null;
    }
}
