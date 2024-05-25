package me.endydev.ffa.listeners.game.player;

import me.yushust.message.MessageHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import team.unnamed.inject.Inject;

import java.util.Arrays;
import java.util.List;

public class GameInteractEnderChestListener implements Listener {

    @Inject
    private MessageHandler messageHandler;

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteractEnderChest(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getInventory().getType().equals(InventoryType.ENDER_CHEST)) return;
        Player player = (Player) event.getWhoClicked();

        List<Material> blacklistedItems = Arrays.asList(
                Material.FISHING_ROD,
                Material.BOW,
                Material.ARROW,
                Material.COOKED_BEEF,
                Material.IRON_SWORD,
                Material.IRON_HELMET,
                Material.IRON_CHESTPLATE,
                Material.CHAINMAIL_LEGGINGS,
                Material.CHAINMAIL_BOOTS
        );

        if (blacklistedItems.contains(event.getCurrentItem().getType())) {
            messageHandler.send(player, "ender-chest.cant-save");
            event.setCancelled(true);
        }
    }
}
