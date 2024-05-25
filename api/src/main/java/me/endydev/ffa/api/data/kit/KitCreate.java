package me.endydev.ffa.api.data.kit;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Data
@RequiredArgsConstructor(staticName = "of")
public class KitCreate {
    private final Player player;
    private final String name;
    private ItemStack mainItem = new ItemStack(Material.DIAMOND_SWORD);
    private boolean permission = true;
    private int level = 0;
    private int price = 0;
    private int slot = -1;
    private int page = 1;
    private EditType editType = EditType.NONE;

    public enum EditType {
        INVENTORY,
        NONE;
    }
}
