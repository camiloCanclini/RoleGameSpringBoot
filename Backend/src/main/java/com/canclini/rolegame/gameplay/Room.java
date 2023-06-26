package com.canclini.rolegame.gameplay;

public class Room {
    private String playerOne;
    private Player playerTwo;
    private String backgroundSrc;
    private int moves;
    private final int maxMoves = 7;
    private boolean turn = true; // true -> playerOne, false -> playerTwo

    public String getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }
}
