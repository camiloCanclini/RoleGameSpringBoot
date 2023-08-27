package com.canclini.rolegame.Game.Logic;

import com.canclini.rolegame.Game.Entities.Cards.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayerMovement{
    private Integer idPlayer;
    private String targetCardId;
    private Card targetCard;
    private String cardUsedId;
    private Card cardUsed;
    private String moveType;
}
