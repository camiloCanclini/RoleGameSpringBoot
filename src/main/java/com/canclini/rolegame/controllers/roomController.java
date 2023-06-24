package com.canclini.rolegame.controllers;

import com.canclini.rolegame.gameplay.Player;
import com.canclini.rolegame.gameplay.Room;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rooms")
public class roomController {
    private static Map<Integer, Room> roomList = new HashMap<>();
    @GetMapping
    public Map<Integer, Room> getRooms() {
        return roomList;
    }
    @PostMapping("/create")
    public Map<Integer, Room> createRoom() {
        roomList.put(roomList.size()+1, new Room("Matias Gamer"));
        return roomList;
    }
}
