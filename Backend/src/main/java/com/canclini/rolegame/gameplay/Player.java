package com.canclini.rolegame.gameplay;

import com.canclini.rolegame.controllers.WebSocketController;
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
    private WebSocketController.PlayerMovement movement = null;

    public Player(String namePlayer) {
        this.namePlayer = namePlayer;
    }

}
