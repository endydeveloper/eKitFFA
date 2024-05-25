package me.endydev.ffa.perks;

import com.zelicraft.core.spigot.api.utils.Text;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.endydev.ffa.utils.SkullUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GoldenHeadPerk implements Perk {

    private String GOLDEN_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFiZDcwM2U1YjhjODhkNGIxZmNmYTk0YTkzNmEwZDZhNGY2YWJhNDQ1Njk2NjNkMzM5MWQ0ODgzMjIzYzUifX19";

    @Override
    public void runAction(Player player) {
        ItemStack itemStack = generateItem();

        player.getInventory().addItem(itemStack);
        player.updateInventory();
    }

    private ItemStack generateItem() {
        return ItemBuilder
                .skull(SkullUtils.getHead(GOLDEN_HEAD))
                .setName(Text.colorize("&6&lCabeza de oro"))
                .build();
    }
}
