package me.endydev.ffa.api.menus;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.zelicraft.commons.spigot.menus.BaseMenu;
import com.zelicraft.core.spigot.api.menus.dto.CreateItemLore;
import com.zelicraft.core.spigot.api.menus.dto.CreateItemSkullLore;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.builder.item.SkullBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.BaseGui;
import me.clip.placeholderapi.PlaceholderAPI;
import me.yushust.message.MessageHandler;
import me.yushust.message.util.StringList;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class CoreBaseMenu implements BaseMenu {

    public int getSlotFromRowCol(final int row, final int col) {
        return (col + (row - 1) * 9) - 1;
    }

    public void checkBackHistory() {

    }

    public void sendUnvailableOption(Player player, MessageHandler messageHandler) {
        messageHandler.send(player, "misc.unavailable");
        XSound.ENTITY_VILLAGER_NO.play(player);
    }

    public boolean validateSlot(int slot, BaseGui gui) {
        final int limit = gui.guiType().getLimit();

        if (gui.guiType() == GuiType.CHEST) {
            return slot >= 0 && slot < gui.getRows() * limit;
        }

        return slot >= 0 && slot <= limit;
    }

    public SkullBuilder generateItemLore(CreateItemSkullLore dto) {
        MessageHandler mh = dto.getMessageHandler();
        String key = dto.getKey();
        SkullBuilder itemBuilder = ItemBuilder.skull()
                .flags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON)
                .setName(PlaceholderAPI.setPlaceholders(dto.getPlayer(), mh.replacing(dto.getPlayer(), key + ".name", dto.getReplacing())));

        if(dto.getTexture() != null) {
            itemBuilder.texture(dto.getTexture());
        }

        StringList stringList = mh.replacingMany(dto.getPlayer(), key + ".lore", dto.getReplacing());
        if(stringList != null && !stringList.isEmpty() && !stringList.get(0).contains(key + ".lore")) {
            if(dto.getReplacingList() != null && dto.getReplacingList().size() > 0) {
                Set<Integer> toRemove = new HashSet<>();
                for (int i = 0; i < stringList.size(); i++) {
                    String s = stringList.get(i);
                    if(dto.getReplacingList().containsKey(s)) {
                        toRemove.add(i);
                        ((List<String>) stringList).addAll(dto.getReplacingList().get(s));
                    }
                }
                toRemove.forEach(i -> stringList.remove(i.intValue()));
            }
            itemBuilder.setLore(PlaceholderAPI.setPlaceholders(dto.getPlayer(), stringList));
        }

        return itemBuilder;
    }

    public ItemBuilder generateItemLore(CreateItemLore dto) {
        MessageHandler mh = dto.getMessageHandler();
        String key = dto.getKey();
        ItemBuilder itemBuilder = ItemBuilder.from(dto.getItemStack())
                .flags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON)
                .setName(PlaceholderAPI.setPlaceholders(dto.getPlayer(), mh.replacing(dto.getPlayer(), key + ".name", dto.getReplacing())));

        StringList stringList = mh.replacingMany(dto.getPlayer(), key + ".lore", dto.getReplacing());
        if(stringList != null && !stringList.isEmpty() && !stringList.get(0).contains(key + ".lore")) {
            if(dto.getReplacingList() != null && dto.getReplacingList().size() > 0) {
                Set<Integer> toRemove = new HashSet<>();
                for (int i = 0; i < stringList.size(); i++) {
                    String s = stringList.get(i);
                    if(dto.getReplacingList().containsKey(s)) {
                        toRemove.add(i);
                        ((List<String>) stringList).addAll(dto.getReplacingList().get(s));
                    }
                }
                toRemove.forEach(i -> stringList.remove(i.intValue()));
            }
            itemBuilder.setLore(PlaceholderAPI.setPlaceholders(dto.getPlayer(), stringList));
        }

        return itemBuilder;
    }

    public ItemBuilder generateItemLore(ItemStack itemStack, String key, MessageHandler mh, Player p) {
        ItemBuilder itemBuilder = ItemBuilder.from(itemStack)
                .flags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON)
                .setName(mh.get(p, key + ".name"));

        StringList stringList = mh.getMany(p, key + ".lore");
        if(stringList != null && !stringList.isEmpty() && !stringList.get(0).contains(key + ".lore")) {
            itemBuilder.setLore(stringList);
        }

        return itemBuilder;
    }

    public void generatePreviousPage(BaseGui gui, int row, int col, int page, MessageHandler messageHandler, Player player, GuiAction<InventoryClickEvent> action) {
        gui.setItem(row, col, generateItemLore(XMaterial.ARROW.parseItem(), "menus.items.previous", messageHandler, player,
                        "%page%", page,
                        "%previous_page%", page - 1)
                .asGuiItem(action));
    }

    public void generateNextPage(BaseGui gui, int row, int col, int page, MessageHandler messageHandler, Player player, GuiAction<InventoryClickEvent> action) {
        gui.setItem(row, col, generateItemLore(XMaterial.ARROW.parseItem(), "menus.items.next", messageHandler, player,
                "%page%", page,
                "%next_page%", page + 1)
                .asGuiItem(action));
    }

    public ItemBuilder generateItemLore(ItemStack itemStack, String key, MessageHandler mh, Player p, Object... replacing) {
        ItemBuilder itemBuilder = ItemBuilder.from(itemStack)
                .flags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON)
                .setName(mh.replacing(p, key + ".name", replacing));

        StringList stringList = mh.replacingMany(p, key + ".lore", replacing);
        if(stringList != null && !stringList.isEmpty() && !stringList.get(0).contains(key + ".lore")) {
            itemBuilder.setLore(stringList);
        }

        return itemBuilder;
    }

    public int fixSlot(String slot) {
        if(slot.contains(",")) {
            String[] split = slot.split(",");
            if(split.length > 0) {
                return getSlotFromRowCol(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            }
        }

        return Integer.parseInt(slot.split(",")[0]);
    }
}
