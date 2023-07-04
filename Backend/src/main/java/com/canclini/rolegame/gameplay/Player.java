package com.canclini.rolegame.gameplay;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Player {
    private String namePlayer;
    private ArrayList<Card> cards = new ArrayList<>();

    public Player(String namePlayer) {
        this.namePlayer = namePlayer;
    }


}
