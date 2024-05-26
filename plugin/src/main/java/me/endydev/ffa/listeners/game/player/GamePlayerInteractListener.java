package me.endydev.ffa.listeners.game.player;

import dev.triumphteam.gui.components.util.ItemNbt;
import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.api.version.VersionSupport;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.utils.Utils;
import me.yushust.message.MessageHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import team.unnamed.inject.Inject;

public class GamePlayerInteractListener implements Listener {

    @Inject
    private GameManager gameManager;

    @Inject
    private ConfigFile configFile;

    @Inject
    private MessageHandler messageHandler;

    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private VersionSupport versionSupport;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void interactEntity(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();
        ItemMeta meta = item.getItemMeta();

        if(!gameManager.isBuildMode(player.getUniqueId())) {
            if (event.getPlayer().hasPermission("thepit.admin")) {
                event.setCancelled(false);
                return;
            }
        }

        String value = ItemNbt.getString(item, "golden-head");
        if (value != null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1"))) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 140, 0));

            versionSupport.onUseGoldenHead(player);
            messageHandler.send(player, "misc.golden-head");

            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                player.getInventory().remove(item);
            }
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
            FFAPlayer ffaPlayer = playerDataManager.getPlayer(event.getPlayer().getUniqueId()).orElse(null);
            if (configFile.contains("ender-chests") && configFile.getStringList("ender-chests").size() > 0) {
                for (String loc : configFile.getStringList("enderChests")) {
                    Location location = Utils.getLocationBlock(loc);
                    Block block = location.getBlock();
                    if (!block.getLocation().equals(event.getClickedBlock().getLocation()) || !location.getBlock().getType().equals(Material.ENDER_CHEST)) {
                        continue;
                    }

                    if (ffaPlayer.getLevel() < configFile.getInt("enderchest-level")) {
                        messageHandler.sendReplacing(event.getPlayer(), "level.insufficient", "%level%", configFile.getInt("enderchest-level"));
                        event.setCancelled(true);
                    } else {
                        event.setCancelled(false);
                    }
                    return;
                }
            }
        }
    }
}
