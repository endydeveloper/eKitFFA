package me.endydev.ffa.duels;

import java.util.UUID;

public class DuelRequest {
    private UUID player1;
    private UUID player2;

    public DuelRequest(UUID player1, UUID player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public UUID getPlayer1() {
        return player1;
    }

    public UUID getPlayer2() {
        return player2;
    }
}
