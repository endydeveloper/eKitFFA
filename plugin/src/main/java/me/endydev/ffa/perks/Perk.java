package me.endydev.ffa.perks;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Perk {
    void runAction(Player player);
    void onPickup(Player player);
    boolean isPerkItem(ItemStack itemStack);
}
