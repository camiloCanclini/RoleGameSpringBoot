package com.canclini.rolegame.controllers;

import com.canclini.rolegame.gameplay.Player;
import com.canclini.rolegame.gameplay.Room;

import java.util.HashMap;
import java.util.Map;

import com.canclini.rolegame.gameplay.Stage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController

@RequestMapping("/rooms")
@Slf4j
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

    @Getter
    @Setter
    public static class RoomModel{
        public int stageId;
    }

    @PostMapping
    public ResponseEntity<Integer> createRoom(@RequestBody @Valid RoomModel request) {
        log.info(String.valueOf(request.stageId));
        log.info(String.valueOf(!stageList.containsKey(request.stageId)));
        if (!stageList.containsKey(request.stageId)) {
            return ResponseEntity.badRequest().build();
        }
        Room room = new Room(stageList.get(request.stageId),true);
        int roomId = generateUniqueId();
        roomList.put(roomId, room);
        //WebSocketController.roomSubscriptions.put(roomId, new HashSet<>());
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

    @Getter
    @Setter
    @ToString
    private static class PlayerName{
        @NotNull
        @NotBlank
        private String name;
    }
    @AllArgsConstructor
    @Setter
    @Getter
    @ToString
    private static class PlayerId{
        private int playerId;
    }
    @PutMapping("/{roomId}/join")
    public ResponseEntity<PlayerId> subscribeToRoomUpdates(@PathVariable Integer roomId, @RequestBody @Valid PlayerName playerName) {
        /*log.info("roomid: "+String.valueOf(roomId));
        log.info("RoomList: "+roomList.toString());
        log.info("Body: "+ playerName.toString());
        log.info("Body2: "+ playerName.getName());*/

        if (!roomList.containsKey(roomId)) {
            return ResponseEntity.notFound().build();
        }
        if (roomList.get(roomId) == null) { // La room no existe
            return ResponseEntity.noContent().build();
        }
        /*if (playerName.getName() == null || playerName.getName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }*/

        Room room = roomList.get(roomId);

        if (room.isFullRoom()) {
            return ResponseEntity.badRequest().build();
        }

        if (room.getHostPlayer() == null) { // Se une el Host
            room.setHostPlayer(new Player(playerName.name));
            return ResponseEntity.ok(new PlayerId(1)); // Se le da el ID 1 al HOST y se asigna
        }
        if (room.getGuestPlayer() == null){ // Se une el Host
            room.setGuestPlayer(new Player(playerName.name));

            room.setFullRoom(true); // Empieza la partida y se reparten las cartas

            WebSocketController.WsMessageModel message = new WebSocketController.WsMessageModel();
            message.type = WebSocketController.WsMessageModel.Type.ROOMREADY;
            message.data = null;
            WebSocketController.sendMessage(roomId, message);
            return ResponseEntity.ok(new PlayerId(2)); // Se le da el ID 2 al GUEST y se asigna
        }
        return ResponseEntity.badRequest().build();
    }

}
