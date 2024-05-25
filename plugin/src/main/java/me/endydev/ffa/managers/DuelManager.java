package me.endydev.ffa.managers;

import lombok.Getter;
import lombok.Setter;
import me.endydev.ffa.FFAPlugin;
import me.endydev.ffa.duels.Duel;
import me.endydev.ffa.duels.DuelRequest;
import team.unnamed.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DuelManager {
    @Inject
    private FFAPlugin plugin;

    @Getter @Setter
    private Duel activeDuel;
    private final List<Duel> duels = new ArrayList<>();
    private final List<DuelRequest> duelsRequests = new ArrayList<>();


    public boolean isInQueue(UUID uuid) {
        return this.duelsRequests.stream().anyMatch(duelRequest -> duelRequest.getPlayer1().equals(uuid) || duelRequest.getPlayer2().equals(uuid));
    }

    public boolean isDueling(UUID uuid) {
        return this.duels.stream().anyMatch(duel -> duel.getPlayer1().equals(uuid) || duel.getPlayer2().equals(uuid));
    }

    public void createDuel(UUID player1, UUID player2) {
        this.duels.add(new Duel(player1, player2));
    }

    // DUELS REQUESTS
    public void createDuelRequest(UUID player1, UUID player2) {
        this.duelsRequests.add(new DuelRequest(player1, player2));
    }

    public void removeDuelRequest(UUID player1, UUID player2) {
        this.duelsRequests.removeIf(duelRequest -> duelRequest.getPlayer1().equals(player1) && duelRequest.getPlayer2().equals(player2));
    }

    public void removeDuelRequest(DuelRequest duelRequest) {
        this.duelsRequests.remove(duelRequest);
    }

    public DuelRequest getDuelRequest(UUID player1, UUID player2) {
        return this.duelsRequests.stream().filter(duelRequest -> duelRequest.getPlayer1().equals(player1) && duelRequest.getPlayer2().equals(player2)).findFirst().orElse(null);
    }

    public DuelRequest getDuelRequest(UUID player) {
        return this.duelsRequests.stream().filter(duelRequest -> duelRequest.getPlayer1().equals(player) || duelRequest.getPlayer2().equals(player)).findFirst().orElse(null);
    }

    public List<DuelRequest> getDuelRequests(UUID player) {
        return this.duelsRequests.stream().filter(duelRequest -> duelRequest.getPlayer1().equals(player) || duelRequest.getPlayer2().equals(player)).collect(Collectors.toList());
    }

    public void removeRequests(UUID player) {
        this.duelsRequests.removeIf(duelRequest -> duelRequest.getPlayer1().equals(player) || duelRequest.getPlayer2().equals(player));
    }

    // DUELS

    public void deleteDuel(UUID player1, UUID player2) {
        this.duels.removeIf(duel -> duel.getPlayer1().equals(player1) && duel.getPlayer2().equals(player2));
    }

    public void deleteDuel(UUID player) {
        this.duels.removeIf(duel -> duel.getPlayer1().equals(player) || duel.getPlayer2().equals(player));
    }

    public void addDuel(Duel duel) {
        this.duels.add(duel);
    }

    public void removeDuel(Duel duel) {
        this.duels.remove(duel);
    }

    public Duel getDuel(UUID player1, UUID player2) {
        return this.duels.stream().filter(duel -> duel.getPlayer1().equals(player1) && duel.getPlayer2().equals(player2)).findFirst().orElse(null);
    }

    public Duel getDuel(UUID player) {
        return this.duels.stream().filter(duel -> duel.getPlayer1().equals(player) || duel.getPlayer2().equals(player)).findFirst().orElse(null);
    }

    public List<Duel> getDuels(UUID player) {
        return this.duels.stream().filter(duel -> duel.getPlayer1().equals(player) || duel.getPlayer2().equals(player)).collect(Collectors.toList());
    }

    public List<Duel> getDuels() {
        return Collections.unmodifiableList(this.duels);
    }
}
