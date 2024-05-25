package me.endydev.ffa.api.events.xp;

import me.endydev.ffa.api.data.FFAPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PlayerExperienceAddEvent extends Event implements ExperienceEvent {

    private final UUID uuid;
    private final Player player;
    private final FFAPlayer user;
    private final double previousXP;
    private final double currentXP;
    private final int previousLevel;
    private final int currentLevel;
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public PlayerExperienceAddEvent(
            Player player,
            FFAPlayer user,
            double previousXP,
            double currentXP,
            int previousLevel,
            int currentLevel
    ) {
        this.uuid = player.getUniqueId();
        this.player = player;
        this.user = user;
        this.previousXP = previousXP;
        this.currentXP = currentXP;
        this.previousLevel = previousLevel;
        this.currentLevel = currentLevel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player getPlayer() {
        return player;
    }

    public FFAPlayer getUser() {
        return user;
    }

    @Override
    public double getPreviousXP() {
        return previousXP;
    }

    @Override
    public double getCurrentXP() {
        return currentXP;
    }

    @Override
    public int getPreviousLevel() {
        return previousLevel;
    }

    @Override
    public int getCurrentLevel() {
        return currentLevel;
    }
}