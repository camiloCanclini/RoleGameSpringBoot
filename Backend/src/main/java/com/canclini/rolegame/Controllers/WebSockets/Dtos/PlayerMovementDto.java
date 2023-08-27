package com.canclini.rolegame.Controllers.WebSockets.Dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerMovementDto {

    public Integer roomId;
    public Integer idPlayer;
    public String targetCard; // Using the List Index
    public String cardUsed;
    public String moveType;

    @JsonCreator
    public PlayerMovementDto(@JsonProperty("roomId") Integer roomId, @JsonProperty("idPlayer") Integer idPlayer, @JsonProperty("targetCard") String targetCard, @JsonProperty("cardUsed") String cardUsed, @JsonProperty("moveType") String moveType) {
        this.roomId = roomId;
        this.idPlayer = idPlayer;
        this.targetCard = targetCard;
        this.cardUsed = cardUsed;
        this.moveType = moveType;
    }
}