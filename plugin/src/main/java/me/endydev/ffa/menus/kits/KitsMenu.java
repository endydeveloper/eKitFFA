package me.endydev.ffa.menus.kits;

import com.zelicraft.core.spigot.api.menus.dto.CreateItemLore;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import me.endydev.ffa.api.FFAAPI;
import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.api.data.kit.KitCreate;
import me.endydev.ffa.api.data.kit.KitInfo;
import me.endydev.ffa.api.handler.level.LevelHandler;
import me.endydev.ffa.api.menus.CoreBaseMenu;
import me.endydev.ffa.managers.KitManager;
import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.service.menu.MenuService;
import me.endydev.ffa.service.menu.dto.ActiveMenu;
import me.yushust.message.MessageHandler;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Singleton
public class KitsMenu extends CoreBaseMenu {
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

    @Inject
    private KitsPreviewMenu kitsPreviewMenu;

    private final String KEY = "menus.kits.list.";

    private final Map<UUID, Integer> pageMap = new HashMap<>();

    public String getLocalizedName(Player player, KitInfo kit) {
        String keyPath = KEY + "kits." + kit.getName().toLowerCase();
        String kitName = messageHandler.get(player, keyPath + ".name");
        if (kitName.equalsIgnoreCase(keyPath + ".name")) {
            kitName = kit.getName();
        }
        return kitName;
    }

    @Override
    public void update(Player player) {
        ActiveMenu activeMenu = menuService.getActiveGUI(player.getUniqueId()).orElse(null);
        if (activeMenu == null) {
            return;
        }

        Gui gui = (Gui) activeMenu.getGui();
        gui.getGuiItems().clear();

        int page = pageMap.getOrDefault(player.getUniqueId(), 0);
        Set<KitInfo> kits = kitManager.getKitsPage(page + 1);

        FFAPlayer ffaPlayer = playerDataManager.getPlayer(player.getUniqueId()).orElse(null);
        if(ffaPlayer == null) {
            return;
        }

        int totalPages = kitManager.getMaxPage();
        if(totalPages > 1 && page+1 > 1) {
            generatePreviousPage(gui, 6, 1, page+1, messageHandler, player, event -> {
                pageMap.put(player.getUniqueId(), page-1);
                update(player);
            });
        }

        if(page+1 < totalPages) {
            generateNextPage(gui, 6, 9, page+1, messageHandler, player, event -> {
                pageMap.put(player.getUniqueId(), page+1);
                update(player);
            });
        }


        for (KitInfo kit : kits) {
            String kitName = getLocalizedName(player, kit);

            if(kit.getLevel() > 0 && levelHandler.getLevelFromExperience(ffaPlayer.getXP()) < kit.getLevel()) {
                gui.setItem(kit.getSlot(), generateItemLore(
                        CreateItemLore.builder()
                                .key(KEY + "items.no-level")
                                .player(player)
                                .messageHandler(messageHandler)
                                .itemStack(kit.getMainItem())
                                .replacing(new Object[]{
                                        "%name%", kitName,
                                        "%level%", kit.getLevel(),
                                        "%price%", kit.getPrice()
                                })
                                .build()
                ).asGuiItem(event -> {
                    if(event.isRightClick()) {
                        previewKit(player, kit);
                        return;
                    }
                }));
                continue;
            }

            if(kit.getPrice() > 0 && ffaPlayer.getCoins() < kit.getPrice()) {
                gui.setItem(kit.getSlot(), generateItemLore(
                        CreateItemLore.builder()
                                .key(KEY + "items.insufficient")
                                .player(player)
                                .messageHandler(messageHandler)
                                .itemStack(kit.getMainItem())
                                .replacing(new Object[]{
                                        "%name%", kitName,
                                        "%price%", kit.getPrice()
                                })
                                .build()
                ).asGuiItem(event -> {
                    if(event.isRightClick()) {
                        previewKit(player, kit);
                        return;
                    }
                }));
                continue;
            }

            if(kit.getPrice() == 0 && levelHandler.getLevelFromExperience(ffaPlayer.getLevel()) >= kit.getLevel() || player.hasPermission("kitffa.kit." + kit.getName().toLowerCase())) {
                gui.setItem(kit.getSlot(), generateItemLore(
                        CreateItemLore.builder()
                                .key(KEY + "items.has")
                                .player(player)
                                .messageHandler(messageHandler)
                                .itemStack(kit.getMainItem())
                                .replacing(new Object[]{
                                        "%name%", kitName,
                                        "%level%", kit.getLevel()
                                })
                                .build()
                ).asGuiItem(event -> {
                    if(event.isRightClick()) {
                        previewKit(player, kit);
                        return;
                    }
                }));
                continue;
            }

            gui.setItem(kit.getSlot(), generateItemLore(
                    CreateItemLore.builder()
                            .key(KEY + "items.has")
                            .player(player)
                            .messageHandler(messageHandler)
                            .itemStack(kit.getMainItem())
                            .replacing(new Object[]{
                                    "%name%", kitName,
                            })
                            .build()
            ).asGuiItem(event -> {
                if(event.isRightClick()) {
                    previewKit(player, kit);
                    return;
                }

                kitManager.getKit(kit.getName())
                        .ifPresent(k -> {
                            k.load(player);
                            messageHandler.sendReplacing(player, "kit.use.kit", "%name%", getLocalizedName(player, k));
                        });
                gui.close(player);
            }));
        }

        gui.update();
    }

    private void previewKit(Player player, KitInfo kit) {
        menuService.addBackHistory(player.getUniqueId(), KitsMenu.class);
        menuService.setClickBackMenu(player.getUniqueId());
        kitsPreviewMenu.open(player, kit.getName());
    }

    @Override
    public void open(Player player, Object... objects) {
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
        return "";
    }
}
