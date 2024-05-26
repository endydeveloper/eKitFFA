package me.endydev.ffa.listeners.game.quit;

import me.endydev.ffa.FFAPlugin;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.util.ItemNbt;
import me.endydev.ffa.cache.TagPlayer;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.KitManager;
import me.endydev.ffa.managers.RegionManager;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import team.unnamed.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public class GameItemsQuitListener implements Listener {

    @Inject
    private GameManager gameManager;

    @Inject
    private FFAPlugin plugin;

    @Inject
    private RegionManager regionManager;

    @Inject
    private KitManager kitManager;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (gameManager.containsRegion(player.getLocation())) {
            return;
        }

        List<Item> items = new ArrayList<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) {
                continue;
            }

            items.add(player.getWorld().dropItem(player.getLocation().add(0, 1, 0), item));
        }

        gameManager.addDroppedItem(items, 10000);
        player.getInventory().clear();
    }
}
