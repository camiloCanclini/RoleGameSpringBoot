package com.canclini.rolegame.Controllers.WebSockets.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WsResultDto {
    public enum Type{
        ERROR,
        ROOMREADY,
        MOVE,
        SHIFT,
        FINISH
    }
    public Type type;
    public String message;
    public MovementResultDto data;
}