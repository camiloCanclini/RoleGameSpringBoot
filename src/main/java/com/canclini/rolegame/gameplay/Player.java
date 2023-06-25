package com.canclini.rolegame.gameplay;

import java.util.ArrayList;

public class Player {
    private String namePlayer;
    private ArrayList<Card> cards = new ArrayList<>();

    public Player(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
}
