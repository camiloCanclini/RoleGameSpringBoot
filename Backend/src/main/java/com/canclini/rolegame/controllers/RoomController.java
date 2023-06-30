package com.canclini.rolegame.controllers;

import com.canclini.rolegame.controllers.models.RoomModel;
import com.canclini.rolegame.gameplay.Player;
import com.canclini.rolegame.gameplay.Room;

import java.util.HashMap;
import java.util.Map;

import com.canclini.rolegame.gameplay.Stage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/rooms")
public class RoomController {

    public static Map<Integer, Room> roomList = new HashMap<>();

    /* Hardcoded Stages*/

    public static Map<Integer, Stage> stageList = new HashMap<>() {{
        put(1, new Stage("Forest", "http://localhost:8080/stages/images/Forest.jpeg",4, 6, 7, 1));
        put(2, new Stage("Castle", "http://localhost:8080/stages/images/Castle.jpeg", 2, 8, 1,2));
        put(3, new Stage("Temple", "http://localhost:8080/stages/images/Temple.jpeg", 7, 5, 2,1));
    }};

    private static int roomCounter = 0;
    public Integer generateUniqueId() {
        roomCounter++;
        return roomCounter;
    }
    @GetMapping
    public Map<Integer, Room> getRooms() {
        return roomList;
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*") // Allows the client consume this API
    @PostMapping
    public ResponseEntity<Integer> createRoom(@RequestBody @Valid RoomModel request) {
        if (!stageList.containsKey(request.stageId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1);
        }
        Room room = new Room(new Player(request.hostPlayer), stageList.get(request.stageId),true);
        int roomId = generateUniqueId();
        roomList.put(roomId, room);
        return ResponseEntity.ok(roomId);
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
