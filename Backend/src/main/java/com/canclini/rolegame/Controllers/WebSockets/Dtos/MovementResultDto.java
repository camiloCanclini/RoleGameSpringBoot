package com.canclini.rolegame.Controllers.WebSockets.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovementResultDto{
    @Setter
    @Getter
    @AllArgsConstructor
    public static class PlayerResult{
        private String targetCardId;
        private String cardUsedId;
        private String moveType;
        private Integer damageReceived;
        private Integer damageDone;
    }
    private PlayerResult hostResult;
    private PlayerResult guestResult;
    private Integer whoWins; // 1 -> host | 2 -> guest | 3 -> nothing
}
