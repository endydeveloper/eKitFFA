package me.endydev.ffa.listeners.game.join;

import com.cryptomorin.xseries.XMaterial;
import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.api.handler.level.LevelHandler;
import me.endydev.ffa.cache.TagPlayer;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.managers.GameManager;
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
    private ConfigFile configFile;

    @Inject
    private Utils utils;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        FFAPlayer ffaPlayer = ffaPlayerRepository.createPlayer(player);
        playerDataManager.addPlayer(ffaPlayer);

        boolean hasImportantItem = false;

        for (ItemStack item : player.getInventory().getArmorContents()) {
            if(item == null) continue;
            if(item.getType().equals(Material.DIAMOND_CHESTPLATE) || item.getType().equals(Material.DIAMOND_BOOTS)) {
                hasImportantItem = true;
                break;
            }
        }

        for (ItemStack item : player.getInventory().getContents()) {
            if(item == null) continue;
            if(item.getType().equals(Material.DIAMOND_SWORD)
                    || item.getType().equals(Material.FISHING_ROD)
                    || item.getType().equals(XMaterial.PLAYER_HEAD.parseMaterial()) && (item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(Utils.CC("&6&lCabeza de oro")))
                    || item.getType().equals(Material.DIAMOND_CHESTPLATE)
                    || item.getType().equals(Material.DIAMOND_BOOTS)
                    || item.getType().equals(Material.OBSIDIAN) || item.getType().equals(Material.LAVA_BUCKET)) {
                hasImportantItem = true;
                break;
            }
        }

        if(!hasImportantItem) {
            gameManager.setKitToPlayer(player);
        }

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);

        if(configFile.contains("spawn")) {
            player.teleport(utils.getLocationSection(configFile.getConfigurationSection("spawn")));
        }

        if(ffaPlayer != null) {
            int level = levelHandler.getLevelFromExperience(ffaPlayer.getXP());
            player.setLevel(level);
            player.setExp(calculatePercentage((float) ffaPlayer.getXP(), (float) levelHandler.getExperienceForLevel(level+1)));
        }

        gameManager.addTag(new TagPlayer(player));
    }

    public static float calculatePercentage(float value, float max) {
        if (max == 0) {
            throw new IllegalArgumentException("El valor máximo no puede ser cero");
        }

        return value / max;
    }
}

