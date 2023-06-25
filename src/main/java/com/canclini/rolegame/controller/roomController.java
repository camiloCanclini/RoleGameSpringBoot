package com.canclini.rolegame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class roomController {
    private int num=0;
    @GetMapping("/createRoom")
    public String create(){
        num += 1;
        return "Room created! "+ num;
    }
}
