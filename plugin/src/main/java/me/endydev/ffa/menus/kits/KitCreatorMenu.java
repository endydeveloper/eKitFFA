package me.endydev.ffa.menus.kits;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.zelicraft.commons.shared.cache.ObjectCache;
import com.zelicraft.core.spigot.api.menus.dto.CreateItemLore;
import com.zelicraft.core.spigot.api.utils.Text;
import dev.triumphteam.gui.guis.Gui;
import me.endydev.ffa.api.data.kit.KitCreate;
import me.endydev.ffa.api.menus.CoreBaseMenu;
import me.endydev.ffa.managers.KitManager;
import me.endydev.ffa.service.menu.MenuService;
import me.endydev.ffa.service.menu.dto.ActiveMenu;
import me.yushust.message.MessageHandler;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

import java.util.UUID;


@Singleton
public class KitCreatorMenu extends CoreBaseMenu {
    @Inject
    private MessageHandler messageHandler;

    @Inject
    private MenuService menuService;

    @Inject
    private KitManager kitManager;

    @Inject
    private KitCreatorSlotMenu slotMenu;

    @Inject
    private ObjectCache<UUID, KitCreate> kitCache;

    private final String KEY = "menus.kits.creator.";

    @Override
    public void update(Player player) {
        ActiveMenu activeMenu = menuService.getActiveGUI(player.getUniqueId()).orElse(null);
        if (activeMenu == null) {
            return;
        }

        String name = activeMenu.getObject(0);
        Gui gui = (Gui) activeMenu.getGui();
        KitCreate kitCreate = kitCache.find(player.getUniqueId()).orElse(KitCreate.of(player, name));

        gui.getGuiItems().clear();

        gui.setItem(2, 2, generateItemLore(
                CreateItemLore.builder()
                        .player(player)
                        .itemStack(kitCreate.getMainItem().clone())
                        .messageHandler(messageHandler)
                        .key(KEY + "items.item")
                        .build()
        ).asGuiItem(event -> {
            if (event.getCursor() == null || event.getCursor().getType().equals(Material.AIR)) {
                return;
            }

            kitCreate.setMainItem(event.getCursor().clone());
            event.setCursor(new ItemStack(Material.AIR));
            event.setCurrentItem(new ItemStack(Material.AIR));
            update(player);
        }));

        gui.setItem(2, 3, generateItemLore(
                CreateItemLore.builder()
                        .player(player)
                        .messageHandler(messageHandler)
                        .itemStack(kitCreate.isPermission() ? XMaterial.GREEN_STAINED_GLASS_PANE.parseItem() : XMaterial.RED_STAINED_GLASS_PANE.parseItem())
                        .key(KEY + "items.permission." + (kitCreate.isPermission() ? "enabled" : "disabled"))
                        .build()
        ).asGuiItem(event -> {
            kitCreate.setPermission(!kitCreate.isPermission());
            update(player);
        }));

        gui.setItem(2, 4, generateItemLore(
                CreateItemLore.builder()
                        .player(player)
                        .messageHandler(messageHandler)
                        .itemStack(XMaterial.EXPERIENCE_BOTTLE.parseItem())
                        .key(KEY + "items.level")
                        .replacing(new Object[]{
                                "%value%", kitCreate.getLevel()
                        })
                        .build()
        ).asGuiItem(event -> {
            int level = kitCreate.getLevel();
            ClickType clickType = event.getClick();

            switch (clickType) {
                case SHIFT_LEFT: {
                    kitCreate.setLevel(level+10);
                    break;
                }
                case SHIFT_RIGHT: {
                    kitCreate.setLevel(Math.max(level-10, 0));
                    break;
                }
                case LEFT: {
                    kitCreate.setLevel(level+1);
                    break;
                }
                case RIGHT: {
                    kitCreate.setLevel(Math.max(level-1, 0));
                    break;
                }
            }
            update(player);
        }));

        gui.setItem(2, 6, generateItemLore(
                CreateItemLore.builder()
                        .player(player)
                        .messageHandler(messageHandler)
                        .itemStack(XMaterial.SUNFLOWER.parseItem())
                        .key(KEY + "items.price")
                        .replacing(new Object[]{
                                "%value%", kitCreate.getPrice()
                        })
                        .build()
        ).asGuiItem(event -> {
            int price = kitCreate.getPrice();
            ClickType clickType = event.getClick();

            switch (clickType) {
                case SHIFT_LEFT: {
                    kitCreate.setPrice(price+10);
                    break;
                }
                case SHIFT_RIGHT: {
                    kitCreate.setPrice(Math.max(price-10, 1));
                    break;
                }
                case LEFT: {
                    kitCreate.setPrice(price+1);
                    break;
                }
                case RIGHT: {
                    kitCreate.setPrice(Math.max(price-1, 1));
                    break;
                }
            }
            update(player);
        }));

        gui.setItem(2, 7, generateItemLore(
                CreateItemLore.builder()
                        .player(player)
                        .messageHandler(messageHandler)
                        .itemStack(XMaterial.PAPER.parseItem())
                        .key(KEY + "items.slot")
                        .replacing(new Object[]{
                                "%value%", kitCreate.getSlot() < 0 ? Text.colorize("&cN/A") : kitCreate.getSlot()
                        })
                        .build()
        ).asGuiItem(event -> {
            menuService.addBackHistory(player.getUniqueId(), KitCreatorMenu.class);
            menuService.setIgnoreClickMenu(player.getUniqueId());
            slotMenu.open(player);
        }));

        gui.setItem(2, 8, generateItemLore(
                CreateItemLore.builder()
                        .player(player)
                        .messageHandler(messageHandler)
                        .itemStack(XMaterial.PAPER.parseItem())
                        .key(KEY + "items.page")
                        .replacing(new Object[]{
                                "%value%", kitCreate.getPage()
                        })
                        .build()
        ).asGuiItem(event -> {
            int page = kitCreate.getPage();
            ClickType clickType = event.getClick();

            switch (clickType) {
                case SHIFT_LEFT: {
                    kitCreate.setPage(page+10);
                    break;
                }
                case SHIFT_RIGHT: {
                    kitCreate.setPage(Math.max(page-10, 0));
                    break;
                }
                case LEFT: {
                    kitCreate.setPage(page+1);
                    break;
                }
                case RIGHT: {
                    kitCreate.setPage(Math.max(page-1, 0));
                    break;
                }
            }
            kitCreate.setSlot(-1);
            update(player);
        }));

        gui.setItem(gui.getRows(), 5, generateItemLore(
                CreateItemLore.builder()
                        .player(player)
                        .messageHandler(messageHandler)
                        .itemStack(XMaterial.BOOK.parseItem())
                        .key(KEY + "items.save")
                        .build()
        ).asGuiItem(event -> {
            if(kitCreate.getSlot() < 0) {
                XSound.ENTITY_VILLAGER_NO.play(player);
                return;
            }

            kitManager.createKitFile(name);
            kitManager.setInfoKit(kitCreate);
            gui.close(player);
            messageHandler.sendReplacing(player, "kit.create.save", "%name%", kitCreate.getName());
        }));

        gui.update();
    }

    @Override
    public void open(Player player, Object... objects) {
        if (objects.length < 1) {
            return;
        }

        Gui gui = Gui.gui()
                .rows(4)
                .disableAllInteractions()
                .title(LegacyComponentSerializer.legacySection().deserialize(messageHandler.get(player, KEY + "title")))
                .create();

        gui.open(player);

        if (!kitCache.contains(player.getUniqueId())) {
            kitCache.add(player.getUniqueId(), KitCreate.of(player, objects[0].toString()));
        } else {
            kitCache.find(player.getUniqueId())
                    .ifPresent(kit -> kit.setEditType(KitCreate.EditType.NONE));
        }

        menuService.setActiveGUI(player.getUniqueId(), ActiveMenu.builder()
                .gui(gui)
                .objects(objects)
                .closeAction(() -> {
                    if (!menuService.hasClickBackMenu(player.getUniqueId())) {
                        kitCache.remove(player.getUniqueId());
                    }
                })
                .build());

        update(player);
    }

    @Override
    public String getTitle(@Nullable Player player, Object... replace) {
        return null;
    }
}
