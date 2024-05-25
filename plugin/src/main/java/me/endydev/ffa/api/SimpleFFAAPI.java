package me.endydev.ffa.api;

import com.google.common.base.Preconditions;
import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.api.events.xp.PlayerExperienceAddEvent;
import me.endydev.ffa.api.events.xp.PlayerExperienceRemoveEvent;
import me.endydev.ffa.api.events.xp.PlayerExperienceSetEvent;
import me.endydev.ffa.api.handler.level.LevelHandler;
import me.endydev.ffa.api.perks.PerkType;
import me.endydev.ffa.managers.PlayerDataManager;
import me.endydev.ffa.repositories.FFAPlayerRepository;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class SimpleFFAAPI implements FFAAPI {
    @Inject
    private FFAPlayerRepository ffaPlayerRepository;

    @Inject
    private PlayerDataManager playerDataManager;

    @Inject
    private LevelHandler levelHandler;

    @Inject
    private FFAPlugin plugin;

    @Override
    public Optional<FFAPlayer> getPlayer(UUID uuid) {
        return Optional.ofNullable(playerDataManager.getPlayer(uuid)
                .orElse(ffaPlayerRepository.getPlayer(uuid)));
    }

    @Override
    public Set<PerkType> getPerks(UUID uuid) {
        return ffaPlayerRepository.getPerks(uuid);
    }

    @Override
    public boolean hasPerk(UUID uuid, PerkType perk) {
        return ffaPlayerRepository.hasPerk(uuid, perk);
    }

    @Override
    public double getXP(Player player) {
        return getPlayer(player.getUniqueId())
                .map(FFAPlayer::getXP)
                .orElse(0D);
    }

    @Override
    public int getLevel(Player player) {
        return levelHandler.getLevelFromExperience(getXP(player));
    }

    @Override
    public void addXP(Player player, double amount) {
        Preconditions.checkNotNull(player, "player cannot be null");
        Preconditions.checkArgument(player.isOnline(), "player must be online");
        Preconditions.checkArgument(amount > 0, "xp to add must be greater than 0");

        FFAPlayer ffaPlayer = playerDataManager.getPlayer(player.getUniqueId())
                .orElseThrow(() -> new IllegalStateException("unable to find player on cache"));

        double xp = ffaPlayer.getXP();
        ffaPlayer.addXP(amount);

        double newXP = ffaPlayer.getXP();

        plugin.getServer().getPluginManager().callEvent(new PlayerExperienceAddEvent(
                player, ffaPlayer, xp, newXP,
                levelHandler.getLevelFromExperience(xp),
                levelHandler.getLevelFromExperience(xp + amount)
        ));
    }

    @Override
    public void setXP(Player player, double amount) {
        Preconditions.checkNotNull(player, "player cannot be null");
        Preconditions.checkArgument(player.isOnline(), "player must be online");

        FFAPlayer ffaPlayer = playerDataManager.getPlayer(player.getUniqueId())
                .orElseThrow(() -> new IllegalStateException("unable to find player on cache"));

        double xp = ffaPlayer.getXP();
        ffaPlayer.setXP(amount);


        plugin.getServer().getPluginManager().callEvent(new PlayerExperienceSetEvent(
                player, ffaPlayer, xp, amount,
                levelHandler.getLevelFromExperience(xp),
                levelHandler.getLevelFromExperience(amount)
        ));
    }

    @Override
    public void removeXP(Player player, double amount) {
        Preconditions.checkNotNull(player, "player cannot be null");
        Preconditions.checkArgument(player.isOnline(), "player must be online");
        Preconditions.checkArgument(amount > 0, "xp to remove must be greater than 0");

        FFAPlayer ffaPlayer = playerDataManager.getPlayer(player.getUniqueId())
                .orElseThrow(() -> new IllegalStateException("unable to find player on cache"));

        double xp = ffaPlayer.getXP();
        ffaPlayer.removeXP(amount);

        double newXP = ffaPlayer.getXP();

        plugin.getServer().getPluginManager().callEvent(new PlayerExperienceRemoveEvent(
                player, ffaPlayer, xp, newXP,
                levelHandler.getLevelFromExperience(xp),
                levelHandler.getLevelFromExperience(xp - amount)
        ));
    }

    @Override
    public void resetXP(Player player) {
        Preconditions.checkNotNull(player, "player cannot be null");
        Preconditions.checkArgument(player.isOnline(), "player must be online");

        FFAPlayer ffaPlayer = playerDataManager.getPlayer(player.getUniqueId())
                .orElseThrow(() -> new IllegalStateException("unable to find player on cache"));

        double xp = ffaPlayer.getXP();
        ffaPlayer.setXP(0);

        plugin.getServer().getPluginManager().callEvent(new PlayerExperienceRemoveEvent(
                player, ffaPlayer, xp, 0, levelHandler.getLevelFromExperience(xp), 0
        ));
    }
}
