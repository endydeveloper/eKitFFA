package me.endydev.ffa.perks;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FishingRodPerk implements Perk  {
    @Override
    public void runAction(Player player) {
        player.getInventory().addItem(
                ItemBuilder
                        .from(Material.FISHING_ROD)
                        .unbreakable(true)
                        .flags(ItemFlag.HIDE_UNBREAKABLE)
                        .build()
        );
        player.updateInventory();
    }

    @Override
    public void onPickup(Player player) {

    }


    @Override
    public boolean isPerkItem(ItemStack itemStack) {
        return false;
    }
}
