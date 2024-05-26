package me.endydev.ffa.api.data.kit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor()
@Builder(builderClassName = "Builder")
@Getter
public class KitInfo {
    private String name;
    @lombok.Builder.Default
    private int level = 0;
    @lombok.Builder.Default
    private int price = 0;
    @lombok.Builder.Default
    private ItemStack mainItem = new ItemStack(Material.DIAMOND_SWORD);
    @lombok.Builder.Default
    private boolean needPermission = false;
    @lombok.Builder.Default
    private int slot = 0;
    @lombok.Builder.Default
    private int page = 1;
    @lombok.Builder.Default
    private boolean onlyDuel = false;

    @lombok.Builder.Default
    private Map<Integer, ItemStack> inventory = new HashMap<>();

    public void load(Player player) {
        if(inventory.size() < 1) {
            return;
        }

        player.getInventory().clear();

        for (Map.Entry<Integer, ItemStack> entry : inventory.entrySet()) {
            int slot = entry.getKey();
            player.getInventory().setItem(slot, entry.getValue());
        }

        player.updateInventory();
    }
}
