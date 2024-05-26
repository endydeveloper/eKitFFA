package me.endydev.ffa.managers;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.api.data.kit.KitCreate;
import me.endydev.ffa.api.data.kit.KitInfo;
import me.endydev.ffa.configuration.ConfigFile;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Named;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class KitManager {

    @Inject
    @Named("kitsFolder")
    private File kitsFolder;

    @Inject
    private FFAPlugin plugin;

    private final Map<String, ConfigFile> kitsFile = new HashMap<>();
    private final Map<Integer, Set<KitInfo>> kitsPages = new HashMap<>();
    private final Map<UUID, String> lastKit = new HashMap<>();
    private final Map<String, KitInfo> kits = new HashMap<>();

    private KitInfo loadKitInfo(String name) {
        ConfigFile kitFile = new ConfigFile(plugin, name.toLowerCase(), ".yml", kitsFolder);
        KitInfo.Builder builder = KitInfo.builder()
                .level(kitFile.getInt("info.level", 1))
                .needPermission(kitFile.getBoolean("info.need-permission", false))
                .price(kitFile.getInt("info.price"))
                .slot(kitFile.getInt("info.slot", 0))
                .page(kitFile.getInt("info.page", 1));
        if(kitFile.contains("info.name")) {
            builder.name(kitFile.getString("info.name"));
        }
        if(kitFile.contains("info.item")) {
            builder.mainItem(ItemStack.deserialize(kitFile.getConfigurationSection("info.item").getValues(true)));
        }

        Map<Integer, ItemStack> itemMap = new HashMap<>();
        ConfigurationSection section = kitFile.getConfigurationSection("inventory");
        for (String key : section.getKeys(false)) {
            int slot = Integer.parseInt(key);
            Map<String, Object> map = section.getConfigurationSection(key).getValues(true);
            ItemStack itemStack = ItemStack.deserialize(map);
            if(itemStack.getType().equals(Material.AIR)) {
                continue;
            }
            itemMap.put(slot, itemStack);
        }
        builder.inventory(itemMap);


        KitInfo kitInfo = builder.build();
        kits.put(kitInfo.getName(), kitInfo);
        Set<KitInfo> kitList = kitsPages.getOrDefault(kitInfo.getPage(), new HashSet<>());
        kitList.add(kitInfo);
        kitsPages.put(kitInfo.getPage(), kitList);

        kitsFile.put(name.toLowerCase(), kitFile);
        return kitInfo;
    }
    public void loadKits() {
        kitsFile.clear();
        kitsPages.clear();
        kits.clear();
        for (File file : kitsFolder.listFiles(File::isFile)) {
            String name = file.getName().replace(".yml", "").toLowerCase();
            loadKitInfo(name);
        }
    }

    public int getMaxPage() {
        int max = 1;
        for (Integer integer : kitsPages.keySet()) {
            if(integer > max) {
                max = integer;
            }
        }

        return max;
    }

    public ConfigFile createKitFile(String name) {
        ConfigFile kitFile = new ConfigFile(plugin, name.toLowerCase(), ".yml", kitsFolder);
        kitFile.create();

        if(!kitFile.contains("info")) {
            kitFile.createSection("info");
        }

        if (!kitFile.contains("inventory")) {
            kitFile.createSection("inventory");
        }

        kitFile.save();
        try {
            kitFile.reload();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        kitsFile.put(name.toLowerCase(), kitFile);
        return kitFile;
    }

    public void setInfoKit(KitCreate kitCreate) {
        getKitFile(kitCreate.getName().toLowerCase())
                .ifPresent(file -> {
                    ItemBuilder itemBuilder = ItemBuilder.from(kitCreate.getMainItem().getType())
                            .setNbt("menu-item", kitCreate.getName())
                            .flags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
                    file.set("info.name", kitCreate.getName());
                    file.set("info.price", kitCreate.getPrice());
                    file.set("info.slot", kitCreate.getSlot());
                    file.set("info.page", kitCreate.getPage());
                    file.set("info.need-permission", kitCreate.isPermission());
                    file.set("info.level", kitCreate.getLevel());
                    file.set("info.duel", kitCreate.isOnlyDuel());
                    file.set("info.item", itemBuilder.build().serialize());

                    PlayerInventory inventory = kitCreate.getPlayer().getInventory();
                    for (int i = 0; i < inventory.getContents().length; i++) {
                        ItemStack itemStack = inventory.getContents()[i];
                        if(itemStack == null || itemStack.getType().equals(Material.AIR)) {
                            file.set("inventory."+i, new ItemStack(Material.AIR).serialize());
                            continue;
                        }
                        file.set("inventory."+i, itemStack.serialize());
                    }

                    file.save();
                    try {
                        file.reload();
                        loadKitInfo(kitCreate.getName());
                    } catch (IOException | InvalidConfigurationException e) {
                        e.printStackTrace();
                    }
                    loadKits();
                });
    }

    public void loadKitToPlayer(String name, Player player) {
        getKit(name)
                .ifPresent(kit -> {
                    kit.load(player);
                });
    }

    public void clearDataPlayer(UUID uuid) {
        lastKit.remove(uuid);
    }

    public void setContents(Player player, String name) {
        getKitFile(name)
                .ifPresent(file -> {
                    PlayerInventory inventory = player.getInventory();
                    for (int i = 0; i < inventory.getContents().length; i++) {
                        ItemStack itemStack = inventory.getContents()[i];
                        if(itemStack == null || itemStack.getType().equals(Material.AIR)) {
                            file.set("inventory."+i, new ItemStack(Material.AIR).serialize());
                            continue;
                        }
                        file.set("inventory."+i, itemStack.serialize());
                    }

                    file.save();
                    try {
                        file.reload();
                    } catch (IOException | InvalidConfigurationException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void loadKitToUser(Player player, String name) {
        getKitFile(name)
                .ifPresent(file -> {
                    if(!file.contains("inventory")) {
                        return;
                    }

                    player.getInventory().clear();

                    ConfigurationSection section = file.getConfigurationSection("inventory");
                    for (String key : section.getKeys(false)) {
                        int slot = Integer.parseInt(key);
                        Map<String, Object> map = section.getConfigurationSection(key).getValues(true);
                        ItemStack itemStack = ItemStack.deserialize(map);
                        player.getInventory().setItem(slot, itemStack);
                    }
                    player.updateInventory();
                });
    }

    public Optional<KitInfo> getKit(String name) {
        return Optional.ofNullable(kits.get(name.toLowerCase()));
    }

    public Map<String, KitInfo> getKits() {
        return Collections.unmodifiableMap(kits);
    }

    public Set<KitInfo> getKitsPage(int page) {
        return kitsPages.getOrDefault(page, new HashSet<>());
    }

    public Map<Integer, Set<KitInfo>> getPagesKits() {
        return Collections.unmodifiableMap(kitsPages);
    }

    public Optional<ConfigFile> getKitFile(String name) {
        return Optional.ofNullable(kitsFile.get(name.toLowerCase()));
    }

    public void removeKit(String name) {
        File kitFile = new File(kitsFolder, name.toLowerCase() + ".yml");
        try {
            Files.deleteIfExists(kitFile.toPath());
            kitsFile.remove(name.toLowerCase());
            kits.remove(name.toLowerCase());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
