package me.endydev.ffa.service.menu;

import lombok.var;
import me.endydev.ffa.FFAPlugin;
import com.zelicraft.commons.shared.cache.ObjectCache;
import com.zelicraft.commons.shared.services.Service;
import com.zelicraft.core.spigot.api.CoreAPI;
import dev.triumphteam.gui.guis.BaseGui;
import me.endydev.ffa.api.menus.CoreBaseMenu;
import me.endydev.ffa.service.menu.dto.ActiveMenu;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Injector;
import team.unnamed.inject.Named;

import java.util.*;

public class MenuService implements Service {

    @Inject
    private FFAPlugin plugin;

    @Inject
    private CoreAPI coreAPI;


    @Inject
    private ObjectCache<UUID, ActiveMenu> activeMenus;

    @Inject
    private Map<UUID, List<Class<? extends CoreBaseMenu>>> backHistory;

    @Inject
    @Named("click")
    private List<UUID> clickBackMenu;

    @Inject
    @Named("ignoreClick")
    private List<UUID> ignoreClickMenu;

    @Inject
    private ObjectCache<UUID, Object[]> guiObjects;

    @Inject
    private Injector injector;

    @Override
    public void start() {

    }

    public void setIgnoreClickMenu(UUID uuid) {
        if (ignoreClickMenu.contains(uuid)) {
            return;
        }

        ignoreClickMenu.add(uuid);
    }

    public void removeIgnoreClickMenu(UUID uuid) {
        ignoreClickMenu.remove(uuid);
    }

    public boolean hasIgnoreClickMenu(UUID uuid) {
        return ignoreClickMenu.contains(uuid);
    }

    public void setClickBackMenu(UUID uuid) {
        if (clickBackMenu.contains(uuid)) {
            return;
        }

        clickBackMenu.add(uuid);
    }

    public void removeClickBackMenu(UUID uuid) {
        clickBackMenu.remove(uuid);
    }

    public boolean hasClickBackMenu(UUID uuid) {
        return clickBackMenu.contains(uuid);
    }

    public void addBackHistory(UUID uuid, Class<? extends CoreBaseMenu> clazz) {
        List<Class<? extends CoreBaseMenu>> history = backHistory.getOrDefault(uuid, new ArrayList<>());
        if (!history.contains(clazz)) {
            for (int i = 0; i < history.size(); i++) {
                var element = history.get(i);
                if (element.equals(clazz)) {
                    if (i == 0 && i < history.size() - 1) {
                        var nextElement = history.get(i + 1);
                        if (nextElement.equals(clazz)) {
                            return;
                        }
                    }

                    if (i > 0) {
                        var previousElement = history.get(i-1);
                        if (previousElement.equals(clazz)) {
                            return;
                        }
                    }
                }
            }
            history.add(clazz);
            backHistory.put(uuid, history);
        }
    }

    public Optional<Class<? extends CoreBaseMenu>> getLastBackHistory(UUID uuid) {
        List<Class<? extends CoreBaseMenu>> history = backHistory.getOrDefault(uuid, new ArrayList<>());
        if (history.size() > 0) {
            return Optional.ofNullable(history.get(history.size() - 1));
        }

        return Optional.empty();
    }

    public void removeLastBackHistory(UUID uuid) {
        List<Class<? extends CoreBaseMenu>> history = backHistory.getOrDefault(uuid, new ArrayList<>());
        if (history.size() > 0) {
            history.remove(history.size() - 1);
        }
    }

    public void clearBackHistory(UUID uuid) {
        List<Class<? extends CoreBaseMenu>> history = backHistory.getOrDefault(uuid, new ArrayList<>());
        history.clear();
        backHistory.put(uuid, history);
    }

    public void setActiveGUI(UUID uuid, ActiveMenu activeMenu) {
        this.activeMenus.add(uuid, activeMenu);

        activeMenu.getGui().setCloseGuiAction(g -> {
            removeActiveGUI(g.getPlayer().getUniqueId());
            if (activeMenu.getCloseAction() != null) {
                activeMenu.getCloseAction().run();
            }
        });
    }

    public void setActiveGUI(UUID uuid, BaseGui gui, Runnable closeAction, Object... objects) {
        setActiveGUI(uuid, ActiveMenu.builder()
                .gui(gui)
                .objects(objects)
                .closeAction(closeAction)
                .build());
    }

    public void setActiveGUI(UUID uuid, BaseGui gui, Object... objects) {
        setActiveGUI(uuid, gui, () -> {
        }, objects);
    }

    public Optional<ActiveMenu> getActiveGUI(UUID uuid) {
        return this.activeMenus.find(uuid);

    }

    public void removeActiveGUI(UUID uuid) {
        this.activeMenus.remove(uuid);
    }

    public Optional<CoreBaseMenu> getMenu(Class<? extends CoreBaseMenu> menu) {
        return Optional.ofNullable(injector.getInstance(menu));
    }
}
