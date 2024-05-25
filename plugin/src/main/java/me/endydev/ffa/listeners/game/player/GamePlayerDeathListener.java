package me.endydev.ffa.listeners.game.player;

import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.managers.RegionManager;
import me.endydev.ffa.utils.Utils;
import me.yushust.message.MessageHandler;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import team.unnamed.inject.Inject;

public class GamePlayerDeathListener implements Listener {

    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private RegionManager regionManager;

    @Inject
    private Utils utils;

    @Inject
    private ConfigFile configFile;

    @Inject
    private GameManager gameManager;

    @Inject
    private MessageHandler messageHandler;

    @Inject
    private FFAPlugin plugin;

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player player = event.getEntity();

        FFAPlayer ffaPlayer = playerDataManager.getPlayer(player.getUniqueId()).orElse(null);
        if(ffaPlayer != null) {
            ffaPlayer.setKillStreak(0);
            ffaPlayer.addDeaths();
        }

        if (regionManager.getRegion() != null && !regionManager.getRegion().contains(player.getLocation())) {

            for (ItemStack item : player.getInventory().getArmorContents()) {
                event.getDrops().remove(item);
                switch (item.getType()) {
                    case DIAMOND_CHESTPLATE:
                        dropItem(player, Material.DIAMOND_CHESTPLATE, item);
                        break;
                    case DIAMOND_BOOTS:
                        dropItem(player, Material.DIAMOND_BOOTS, item);
                        break;
                    default:
                        event.getDrops().remove(item);
                        item.setType(Material.AIR);
                        break;
                }
            }

            int beef = 0;
            if (gameManager.getPlayerTag(player).hasAttackers()) {
                for (ItemStack item : gameManager.getPlayerTag(player).getLastAttacker().getInventory().getContents()) {
                    if (item == null) continue;
                    if (item.getType().equals(Material.COOKED_BEEF)) {
                        beef += item.getAmount();
                    }
                }
            }

            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null) continue;

                Material itemType = item.getType();
                switch (itemType) {
                    case COOKED_BEEF:
                        event.getDrops().remove(item);
                        if (gameManager.getPlayerTag(player).hasAttackers() && beef < 28) {
                            Item beefItem = player.getWorld().dropItem(player.getLocation().add(0, 1, 0), new ItemStack(Material.COOKED_BEEF, 5));
                            scheduleItemRemoval(beefItem);
                            gameManager.addDroppedItem(beefItem);
                        }
                        break;
                    case SKULL_ITEM:
                        if (item.hasItemMeta() && Utils.CC("&6&lCabeza de oro").equals(item.getItemMeta().getDisplayName())) {
                            event.getDrops().remove(item);
                            Item skullItem = player.getWorld().dropItem(player.getLocation().add(0, 1, 0), new ItemStack(Material.GOLDEN_APPLE, 2));
                            scheduleItemRemoval(skullItem);
                            gameManager.addDroppedItem(skullItem);
                        }
                        break;
                    case DIAMOND_CHESTPLATE:
                    case DIAMOND_BOOTS:
                    case DIAMOND_SWORD:
                        event.getDrops().remove(item);
                        Item diamondItem = player.getWorld().dropItem(player.getLocation().add(0, 1, 0), Utils.setUnbreakable(new ItemStack(itemType, 1)));
                        scheduleItemRemoval(diamondItem);
                        gameManager.addDroppedItem(diamondItem);
                        break;
                    case ARROW:
                        event.getDrops().remove(item);
                        Item arrowItem = player.getWorld().dropItem(player.getLocation().add(0, 1, 0), new ItemStack(Material.ARROW, 10));
                        scheduleItemRemoval(arrowItem);
                        gameManager.addDroppedItem(arrowItem);
                        break;
                    default:
                        event.getDrops().remove(item);
                        item.setType(Material.AIR);
                        break;
                }
            }
        } else {
            event.getDrops().clear();
        }

        if(ffaPlayer != null) {
            player.setLevel(ffaPlayer.getLevel());
        }


        gameManager.removePlayerTag(player);
    }

    private void scheduleItemRemoval(Item item) {
        new BukkitRunnable() {
            @Override
            public void run() {
                gameManager.removeDroppedItem(item);
                item.remove();
            }
        }.runTaskLater(plugin, 20L * 60L);
    }

    private void dropItem(Player player, Material material, ItemStack item) {
        Item itemDrop = player.getWorld().dropItem(player.getLocation().add(0, 1, 0), Utils.setUnbreakable(new ItemStack(material, 1)));
        itemDrop.setPickupDelay(40);
        gameManager.addDroppedItem(itemDrop);
        new BukkitRunnable() {
            @Override
            public void run() {
                gameManager.removeDroppedItem(itemDrop);
                itemDrop.remove();
            }
        }.runTaskLater(plugin, 20L * 60L);
    }
}
