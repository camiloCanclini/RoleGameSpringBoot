package com.canclini.rolegame.controllers;

import com.canclini.rolegame.gameplay.Player;
import com.canclini.rolegame.gameplay.Room;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rooms")
public class RoomController {
    public static Map<Integer, Room> roomList = new HashMap<>();
    private static int roomCounter = 0;
    public Integer generateUniqueId() {
        roomCounter++;
        return roomCounter;
    }
    @GetMapping
    public Map<Integer, Room> getRooms() {
        return roomList;
    }
    @PostMapping("/create")
    public int createRoom() {
        int roomId = generateUniqueId();
        roomList.put(roomId, new Room());
        return roomId;
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoom(@PathVariable int roomId) {
        Room room = roomList.get(roomId);
        if (room != null) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
