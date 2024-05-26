package me.endydev.ffa.service;

import com.zelicraft.commons.shared.cache.ObjectCache;
import me.endydev.ffa.FFAPlugin;
import com.zelicraft.commons.shared.services.Service;
import me.endydev.ffa.api.data.TemporalBlock;
import me.endydev.ffa.api.data.TemporalDropItem;
import me.endydev.ffa.configuration.ConfigFile;
import me.endydev.ffa.database.Database;
import me.endydev.ffa.managers.GameManager;
import me.endydev.ffa.managers.KitManager;
import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.managers.RegionManager;
import me.endydev.ffa.service.repository.RepositoryService;
import me.endydev.ffa.utils.Cuboid;
import me.endydev.ffa.utils.PapiHook;
import me.endydev.ffa.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.io.File;
import java.lang.reflect.Field;
import java.util.UUID;

public class MainService implements Service {

    @Inject
    private PapiHook papiHook;

    @Inject
    @Named("kitsFolder")
    private File kitsFolder;

    @Inject
    @Named("listener")
    private Service listenerService;

    @Inject
    @Named("api")
    private Service apiService;

    @Inject
    private FFAPlugin plugin;

    @Inject
    @Named("command")
    private Service commandService;

    @Inject
    private ConfigFile configFile;

    @Inject
    @Named("task")
    private Service taskService;

    @Inject
    @Named("repository")
    private Service repositoryService;

    @Inject
    private Utils utils;

    @Inject
    private RegionManager regionManager;

    @Inject
    private GameManager gameManager;

    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private Database database;

    @Inject
    private KitManager kitManager;

    @Override
    public void start() {
        startServices(
                listenerService,
                commandService,
                apiService,
                taskService,
                repositoryService
        );
        if(plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            papiHook.register();
        }

        if(configFile.contains("spawn")) {
            Location location = Utils.getLocationSection(configFile.getConfigurationSection("spawn"));
            if(location == null) {
                return;
            }

            World world = location.getWorld();
            for (Entity entity : world.getEntities()) {
                if(entity instanceof Player || entity instanceof ArmorStand) {
                    continue;
                }

                entity.remove();
            }

            for (LivingEntity entity : world.getLivingEntities()) {
                if(entity instanceof Player || entity instanceof ArmorStand) {
                    continue;
                }

                entity.remove();
            }
        }

        kitManager.loadKits();
        setupRegionManager();
    }

    @Override
    public void stop() {
        stopServices(
                listenerService,
                commandService,
                apiService,
                repositoryService
        );

        if(!playerDataManager.isEmpty()) {
            playerDataManager.getPlayers().values().forEach(p -> gameManager.savePlayer(p));
        }

        if(configFile.contains("spawn")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.teleport(utils.getLocationSection(configFile.getConfigurationSection("spawn")));
            }
        }

        database.close();
        for (TemporalBlock block : gameManager.getBlocks().values()) {
            block.getBlock().setType(Material.AIR);
        }
        gameManager.getBlocks().clear();

        for (TemporalDropItem item : gameManager.getDroppedItems().values()) {
            item.getItem().remove();
            gameManager.removeDroppedItem(item.getUuid());
        }
    }

    private void startServices(Service... services) {
        for (Service service : services) {
            service.start();
        }
    }

    private void stopServices(Service... services) {
        for (Service service : services) {
            service.stop();
        }
    }

    private void setupRegionManager() {
        if(configFile.contains("region-spawn.first") && configFile.contains("region-spawn.second")) {
            Location first = utils.getLocationSection(configFile.getConfigurationSection("region-spawn.first"));
            Location second = utils.getLocationSection(configFile.getConfigurationSection("region-spawn.second"));
            assert first != null;
            assert second != null;
            regionManager.setRegion(new Cuboid(first, second));
        }
    }
}
