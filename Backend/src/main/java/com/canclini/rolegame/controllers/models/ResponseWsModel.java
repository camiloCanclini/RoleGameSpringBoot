package com.canclini.rolegame.controllers.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseWsModel {
    public enum Type{
        ERROR,
        READY,
        MESSAGE,
        OTHER
    }
    private Type type;
    private String message;
    private String data;

}
