package me.endydev.ffa.listeners.game.player;

import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.RegionManager;
import me.endydev.ffa.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import team.unnamed.inject.Inject;

public class GamePlayerPickupListener implements Listener {

    @Inject
    private GameManager gameManager;

    @Inject
    private RegionManager regionManager;

    @Inject
    private Utils utils;

    @Inject
    private ConfigFile configFile;

    @EventHandler
    public void pickupEvent(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if(!gameManager.isBuildMode(player.getUniqueId())) {
            if (event.getPlayer().hasPermission("thepit.admin")) {
                event.setCancelled(false);
                return;
            }
        }

        if (regionManager.getRegion() != null && regionManager.getRegion().contains(event.getPlayer().getLocation())) {
            event.getItem().remove();
            event.setCancelled(false);
            return;
        }

        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack();
        if (itemStack.getType().equals(Material.DIAMOND_CHESTPLATE)) {
            event.getItem().remove();
            utils.playSound(configFile.getConfigurationSection("sounds.pickup-diamond-armor"), player);
            if (player.getInventory().getChestplate().getType() != Material.DIAMOND_CHESTPLATE) {
                player.getInventory().setChestplate(Utils.setUnbreakable(new ItemStack(Material.DIAMOND_CHESTPLATE)));
            }
            event.setCancelled(true);
            return;
        }
        if (itemStack.getType().equals(Material.DIAMOND_BOOTS)) {
            event.getItem().remove();
            utils.playSound(configFile.getConfigurationSection("sounds.pickup-diamond-armor"), player);
            if (player.getInventory().getBoots().getType() != Material.DIAMOND_BOOTS) {
                player.getInventory().setBoots(Utils.setUnbreakable(new ItemStack(Material.DIAMOND_BOOTS)));
            }
            event.setCancelled(true);
            return;
        }
        if (itemStack.getType().equals(Material.DIAMOND_SWORD)) {
            boolean hasItem = false;
            int slot = 0;
            int counter = 0;
            for (ItemStack i : player.getInventory().getContents()) {
                counter++;
                if (i == null) continue;
                if (i.getType().equals(Material.DIAMOND_SWORD)) {
                    hasItem = true;
                    break;
                } else if (i.getType().equals(Material.IRON_SWORD)) {
                    slot = counter - 1;
                }
            }

            event.getItem().remove();

            if (!hasItem) {
                player.getInventory().setItem(slot, Utils.setUnbreakable(new ItemStack(Material.DIAMOND_SWORD)));
            }

            event.setCancelled(true);
            return;
        }
        if (itemStack.getType().equals(Material.ARROW)) {
            int arrow = 0;
            for (ItemStack i : player.getInventory().getContents()) {
                if (i == null) continue;
                if (i.getType().equals(Material.ARROW)) {
                    arrow += i.getAmount();
                }
            }
            if (arrow <= configFile.getInt("max-arrows", 30)) {
                event.setCancelled(false);
            } else {
                event.getItem().remove();
                event.setCancelled(true);
            }
            return;
        }
        if (itemStack.getType().equals(Material.COOKED_BEEF)) {
            int beef = 0;
            for (ItemStack i : player.getInventory().getContents()) {
                if (i == null) continue;
                if (i.getType().equals(Material.COOKED_BEEF)) {
                    beef += i.getAmount();
                }
            }
            if (beef <= 27) {
                event.setCancelled(false);
            } else {
                event.getItem().remove();
                event.setCancelled(true);
            }
            return;
        }
        if (itemStack.getType().equals(Material.GOLDEN_APPLE)) {
            if (itemStack.getAmount() > 1) {
                itemStack.setAmount(1);
            }
            event.setCancelled(false);
            return;
        }

        event.getItem().remove();
        event.setCancelled(true);
    }
}
