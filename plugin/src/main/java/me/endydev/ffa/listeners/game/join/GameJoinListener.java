package me.endydev.ffa.listeners.game.join;

import com.cryptomorin.xseries.XMaterial;
import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.api.handler.level.LevelHandler;
import me.endydev.ffa.cache.TagPlayer;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.KitManager;
import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.repositories.FFAPlayerRepository;
import me.endydev.ffa.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import team.unnamed.inject.Inject;

public class GameJoinListener implements Listener {

    @Inject
    private GameManager gameManager;

    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private FFAPlayerRepository ffaPlayerRepository;

    @Inject
    private LevelHandler levelHandler;

    @Inject
    private KitManager kitManager;

    @Inject
    private ConfigFile configFile;

    @Inject
    private Utils utils;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        FFAPlayer ffaPlayer = ffaPlayerRepository.createPlayer(player);
        playerDataManager.addPlayer(ffaPlayer);

        boolean hasItem = false;

        for (ItemStack item : player.getInventory().getContents()) {
            if(item == null) {
                continue;
            }

            hasItem = true;
            break;
        }

        if(!hasItem) {
            kitManager.loadKitToPlayer("default", player);
        }

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);

        if(configFile.contains("spawn")) {
            player.teleport(utils.getLocationSection(configFile.getConfigurationSection("spawn")));
        }

        gameManager.setLevelBar(player);

        gameManager.addTag(new TagPlayer(player));
    }

}

