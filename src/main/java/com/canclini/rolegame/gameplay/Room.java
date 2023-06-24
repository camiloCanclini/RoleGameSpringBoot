package com.canclini.rolegame.gameplay;

public class Room {
    private byte roomId;
    private String playerOne;
    private Player playerTwo;
    private String backgroundSrc;

    public Room(String playerOne) {
        this.playerOne = playerOne;
    }

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
