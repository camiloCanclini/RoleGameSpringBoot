package com.canclini.rolegame.controllers;

import com.canclini.rolegame.controllers.models.ActionPlayerWsModel;
import com.canclini.rolegame.controllers.models.InfoUserWsModel;
import com.canclini.rolegame.controllers.models.ResponseWsModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;

import java.util.Set;

@Controller
@Slf4j
public class WebSocketController {
    private enum Roles {
        HOST,
        GUEST
    }
    @AllArgsConstructor
    @Setter
    @Getter
    private class SuscriberPlayer {
        private Roles role;
        private String name;
    }
    public static Map<Integer, Set<SuscriberPlayer>> roomSubscriptions = new HashMap<>();
    @Autowired
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public WebSocketController(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    private void sendResponse (Integer roomId, String message, ResponseWsModel.Type type) {
        ResponseWsModel response = new ResponseWsModel();
        response.setType(type);
        response.setMessage(message);
        try {
            String jsonResponse = objectMapper.writeValueAsString(response);
            messagingTemplate.convertAndSend("/room/" + roomId, jsonResponse);
        } catch (Exception e) {
            // Manejar el error de serialización
            e.printStackTrace();
        }
    }
    @MessageMapping("/room/{roomId}/join")
    public void subscribeToRoomUpdates(@DestinationVariable Integer roomId, SimpMessageHeaderAccessor headerAccessor, @Payload InfoUserWsModel message) {
        log.info("Alguien se suscribio");
        log.info(message.getSuscriberName());
        if (!roomSubscriptions.containsKey(roomId)) {
            sendResponse(roomId, "Error: Room Doesn't Exists", ResponseWsModel.Type.ERROR);
            return;
        }
        if (RoomController.roomList.get(roomId) == null) { // La room no existe
            sendResponse(roomId, "Error: Room Doesn't Exists", ResponseWsModel.Type.ERROR);
            return;
        }
        if (roomSubscriptions.get(roomId).size() >= 2) {
            // Ya hay dos suscriptores en el canal, enviar mensaje de sala llena
            sendResponse(roomId, "Error: There already are 2 suscribers in the channel", ResponseWsModel.Type.ERROR);
            return;
        }

        // Suscription Player (client) to the room
        String sessionId = headerAccessor.getSessionId();

        if (roomSubscriptions.get(roomId).size() == 0) {
            roomSubscriptions.get(roomId).add(new SuscriberPlayer(Roles.HOST, message.getSuscriberName()));
            sendResponse(roomId, "Player Suscribe!: ", ResponseWsModel.Type.OTHER);
        } else{
            roomSubscriptions.get(roomId).add(new SuscriberPlayer(Roles.GUEST, message.getSuscriberName()));
            RoomController.roomList.get(roomId).setFullRoom(true);

            sendResponse(roomId, "Player Suscribed!", ResponseWsModel.Type.OTHER);
            sendResponse(roomId, "The Room Is Ready!", ResponseWsModel.Type.READY);
        }
        String suscribersToString = "";
        for (SuscriberPlayer elem: roomSubscriptions.get(roomId)) {
            suscribersToString += " " + elem.getName();
        }
        sendResponse(roomId, "Suscribeds Players!: "+suscribersToString, ResponseWsModel.Type.OTHER);
    }

    @MessageMapping("/room/{roomId}/interact")
    public void interactWithRoom (@DestinationVariable Integer roomId, @Payload ActionPlayerWsModel message){
        sendResponse(roomId, message.getPlayer()+": "+ message.getMessage(), ResponseWsModel.Type.MESSAGE);
        log.info("Alguien interactuo");
        //return message;
    }
}