package me.endydev.ffa.api;

import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.api.perks.PerkType;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("unused")
public interface FFAAPI {
    Optional<FFAPlayer> getPlayer(UUID uuid);
    Set<PerkType> getPerks(UUID uuid);
    boolean hasPerk(UUID uuid, PerkType perk);

    double getXP(Player player);

    int getLevel(Player player);

    void addXP(Player player, double amount);

    void setXP(Player player, double amount);

    void removeXP(Player player, double amount);

    void resetXP(Player player);
}
