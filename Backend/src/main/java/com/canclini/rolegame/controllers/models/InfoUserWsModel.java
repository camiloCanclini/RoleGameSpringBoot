package com.canclini.rolegame.controllers.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoUserWsModel {

    @NotBlank
    private String suscriberName;
}
