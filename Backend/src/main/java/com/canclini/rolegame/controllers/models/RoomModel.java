package com.canclini.rolegame.controllers.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class RoomModel {

    @NotBlank(message = "No Player Host Name")
    public String playerOne;

    @NotBlank(message = "No Player Two Name")
    public String playerTwo;
    @NotNull(message = "Id Cannot be Null")
    public int stageId;

    // Getters y setters
}

