package com.canclini.rolegame.Controllers.WebSockets.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class WsMessageDto {
    public enum Type{
        ERROR,
        ROOMREADY,
        MOVE,
        SHIFT
    }
    public Type type;
    public String message;
    public PlayerMovementDto data;
}