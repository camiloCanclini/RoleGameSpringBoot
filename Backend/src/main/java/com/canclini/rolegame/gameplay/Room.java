package com.canclini.rolegame.gameplay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {
    private Player playerOne;
    private Player playerTwo;
    private Stage stage;
    private int movementsCounter = 0;
    private final int maxMoves = 7;
    private boolean turn = true; // true -> playerOne, false -> playerTwo
    private boolean fullRoom = false;

    public Room(Player playerOne, Player playerTwo, Stage stage, boolean turn) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.stage = stage;
        this.turn = turn;
    }
}
