package me.endydev.ffa.managers;

import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.api.data.FFAPlayer;
import team.unnamed.inject.Inject;

import java.util.*;

public class PlayerDataManager {

    @Inject
    private FFAPlugin FFAPlugin;
    private final Map<UUID, FFAPlayer> playersData = new HashMap<>();

    public Optional<FFAPlayer> getPlayer(UUID uuid) {
        return Optional.ofNullable(playersData.get(uuid));
    }

    public void addPlayer(FFAPlayer data) {
        playersData.put(data.getUniqueId(), data);
    }

    public void removePlayer(UUID uuid) {
        playersData.remove(uuid);
    }

    public boolean isEmpty() {
        return playersData.isEmpty();
    }

    public Map<UUID, FFAPlayer> getPlayers() {
        return Collections.unmodifiableMap(playersData);
    }
}
