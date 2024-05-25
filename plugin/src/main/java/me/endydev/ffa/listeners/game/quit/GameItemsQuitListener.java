package me.endydev.ffa.listeners.game.quit;

import me.endydev.ffa.FFAPlugin;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.util.ItemNbt;
import me.endydev.ffa.cache.TagPlayer;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.RegionManager;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import team.unnamed.inject.Inject;

public class GameItemsQuitListener implements Listener {

    @Inject
    private GameManager gameManager;

    @Inject
    private FFAPlugin plugin;

    @Inject
    private RegionManager regionManager;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        TagPlayer tagPlayer = gameManager.getPlayerTag(player);
        if (tagPlayer == null) {
            return;
        }
        if (tagPlayer.isTagged()) {
            gameManager.setKitToPlayer(player);
            return;
        }

        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item.getType().equals(Material.DIAMOND_CHESTPLATE) || item.getType().equals(Material.DIAMOND_BOOTS)) {
                Item itemDrop = player.getWorld().dropItem(player.getLocation().add(0, 1, 0), ItemBuilder.from(item.getType())
                        .unbreakable(true)
                        .flags(ItemFlag.HIDE_UNBREAKABLE)
                        .build());

                createTask(itemDrop);
                continue;
            }
            item.setType(Material.AIR);
        }
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) {
                continue;
            }

            Item itemDrop = player.getWorld().dropItem(player.getLocation().add(0, 1, 0), ItemBuilder.from(Material.GOLDEN_APPLE).build());

            switch (item.getType()) {
                case GOLDEN_APPLE: {
                    itemDrop = player.getWorld().dropItem(player.getLocation().add(0, 1, 0), ItemBuilder.from(Material.GOLDEN_APPLE).build());
                    break;
                }
                case SKULL_ITEM: {
                    String goldenHead = ItemNbt.getString(item, "golden_head");
                    if(goldenHead == null || goldenHead.equalsIgnoreCase("false")) {
                        break;
                    }
                    itemDrop = player.getWorld().dropItem(player.getLocation().add(0, 1, 0), ItemBuilder.from(Material.GOLDEN_APPLE).amount(3).build());
                    break;
                }
                case DIAMOND_SWORD:
                case DIAMOND_BOOTS:
                case DIAMOND_CHESTPLATE: {
                    itemDrop = player.getWorld().dropItem(player.getLocation().add(0, 1, 0), ItemBuilder
                            .from(item.getType()).unbreakable(true).flags(ItemFlag.HIDE_UNBREAKABLE).build());
                    break;
                }
                case ARROW: {
                    itemDrop = player.getWorld().dropItem(player.getLocation().add(0, 1, 0), ItemBuilder.from(Material.ARROW).amount(Math.min(item.getAmount(), 10)).build());
                    break;
                }
                default: {
                    item.setType(Material.AIR);
                }
            }
            if(itemDrop != null) {
                createTask(itemDrop);
            }
        }

        if (gameManager.containsRegion(player.getLocation())) {
            return;
        }

        gameManager.setKitToPlayer(player);
    }

    private void createTask(Item itemDrop) {
        new BukkitRunnable() {
            @Override
            public void run() {
                gameManager.removeDroppedItem(itemDrop);
                itemDrop.remove();
            }
        }.runTaskLater(plugin, 20L * 60L);
        gameManager.addDroppedItem(itemDrop);
    }
}
