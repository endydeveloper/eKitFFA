//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.endydev.ffa.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Text {
    private String message;

    public Text(String message) {
        this.message = message;
    }

    public String colorize() {
        return ChatColor.translateAlternateColorCodes('&', this.message);
    }

    public String placeholder(Player player) {
        return PlaceholderAPI.setPlaceholders(player, this.colorize());
    }

    public static List<String> placeholder(Player player, List<String> lore) {
        return PlaceholderAPI.setPlaceholders(player, lore.stream().map(Text::translate).collect(Collectors.toList()));
    }

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getMessage() {
        return this.message;
    }
}
