package com.canclini.rolegame.gameplay;

import com.canclini.rolegame.controllers.RoomController;
import lombok.Getter;

@Getter
public class Stage {

    private String stageName;
    private int heat;
    private int wind;
    private int humidity;
    private int magicBoost;


    public Stage(String stageName, int heat, int wind, int humidity, int magicBoost){
        this.stageName = stageName;
        this.setHeat(heat);
        this.setWind(wind);
        this.setHumidity(humidity);
        this.setMagicBoost(magicBoost);
    }

    private void setStageName(String stageName){
        this.stageName = stageName;
    }
    private void setHeat(int heat){
        if (heat <= 0 || heat > 10) {
            throw new Error("Error Trying to set the Heat Value!");
        }
        this.heat = heat;
    }
    private void setWind(int wind){
        if (wind <= 0 || wind > 10) {
            throw new Error("Error Trying to set the Wind Value!");
        }
        this.wind = wind;
    }
    private void setHumidity(int humidity){
        if (humidity <= 0 || humidity > 10) {
            throw new Error("Error Trying to set the Humidity Value!");
        }
        this.humidity = humidity;
    }
    private void setMagicBoost(int magicBoost){
        if (magicBoost <= 0 || magicBoost >= 3) {
            throw new IllegalArgumentException("Error Trying to set the Magic Boost Value!");
        }
        this.magicBoost = magicBoost;
    }
}
