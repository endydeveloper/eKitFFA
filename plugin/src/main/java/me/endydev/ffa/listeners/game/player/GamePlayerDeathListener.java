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

import java.util.ArrayList;
import java.util.List;

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
        event.setDroppedExp(0);
        event.getDrops().clear();

        Player player = event.getEntity();

        FFAPlayer ffaPlayer = playerDataManager.getPlayer(player.getUniqueId()).orElse(null);
        if(ffaPlayer != null) {
            ffaPlayer.setKillStreak(0);
            ffaPlayer.addDeaths();
        }


        if (!gameManager.containsRegion(player.getLocation())) {
            List<Item> items = new ArrayList<>();
            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null) {
                    continue;
                }

                items.add(player.getWorld().dropItem(player.getLocation().add(0, 1, 0), item));
            }

            gameManager.addDroppedItem(items, 10000);
        }

        gameManager.setLevelBar(player);

        gameManager.removePlayerTag(player);
    }
}
