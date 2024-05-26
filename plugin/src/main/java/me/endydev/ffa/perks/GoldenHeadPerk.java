package me.endydev.ffa.perks;

import com.zelicraft.core.spigot.api.utils.Text;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import me.endydev.ffa.utils.SkullUtils;
import me.yushust.message.MessageHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import team.unnamed.inject.Inject;

public class GoldenHeadPerk implements Perk {

    @Inject
    private MessageHandler messageHandler;

    private String GOLDEN_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFiZDcwM2U1YjhjODhkNGIxZmNmYTk0YTkzNmEwZDZhNGY2YWJhNDQ1Njk2NjNkMzM5MWQ0ODgzMjIzYzUifX19";

    @Override
    public void runAction(Player player) {
        ItemStack itemStack = generateItem(player);

        player.getInventory().addItem(itemStack);
        player.updateInventory();
    }

    @Override
    public void onPickup(Player player) {

    }

    @Override
    public boolean isPerkItem(ItemStack itemStack) {
        return false;
    }

    private ItemStack generateItem(Player player) {
        return ItemBuilder
                .skull(SkullUtils.getHead(GOLDEN_HEAD))
                .setName(messageHandler.get(player, "items.golden-head"))
                .setNbt("golden-head", "true")
                .setNbt("localize-name", "items.golden-head")
                .build();
    }
}
