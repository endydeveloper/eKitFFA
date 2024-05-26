package me.endydev.ffa.perks;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArrowPerk implements Perk {
    @Override
    public void runAction(Player player) {
        ItemBuilder itemBuilder = ItemBuilder.from(Material.ARROW).amount(1);

        player.getInventory().addItem(itemBuilder.build());
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
