package me.endydev.ffa.duels;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class Duel {
    private UUID player1;
    private UUID player2;
    @Setter
    private boolean started;

    public Duel(UUID player1, UUID player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.started = false;
    }
}