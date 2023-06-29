package com.canclini.rolegame.gameplay;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {
    private Player hostPlayer;
    private Player guestPlayer = null;
    private Stage stage;
    private int movementsCounter = 0;
    private final int maxMoves = 7;
    private boolean turn = true; // true -> playerOne, false -> playerTwo
    private boolean fullRoom = false;

    public Room(Player hostPlayer, Stage stage, boolean turn) {
        this.hostPlayer = hostPlayer;
        this.stage = stage;
        this.turn = turn;
    }
}
