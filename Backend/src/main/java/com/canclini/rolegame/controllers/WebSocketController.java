package com.canclini.rolegame.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

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

    @Autowired
    private static SimpMessagingTemplate messagingTemplate;
    private static ObjectMapper objectMapper;

    public WebSocketController(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    @Getter
    public static class WsMessageModel {
        public enum Type{
            ERROR,
            ROOMREADY,
            MOVE,
            SHIFT
        }
        public Type type;
        public PlayerMovement data;
    }

    public static class PlayerMovement{
        public Integer idPlayer;
        public Integer targetCard; // Using the List Index
        public Integer cardUsed;
    }

    public static void sendMessage(Integer roomId, WsMessageModel message) {
        try {
            String jsonResponse = objectMapper.writeValueAsString(message);
            messagingTemplate.convertAndSend("/room/" + roomId, jsonResponse);
        } catch (Exception e) {
            // Manejar el error de serialización
            e.printStackTrace();
        }
    }


    @MessageMapping("/room/{roomId}/interact")
    public void interactWithRoom (@DestinationVariable Integer roomId, @Payload PlayerMovement message){
        WsMessageModel messageToSend = new WsMessageModel();
        messageToSend.type = WsMessageModel.Type.MOVE;
        messageToSend.data = message;
        sendMessage(roomId, messageToSend);
        log.info("Alguien interactuo");
        //return message;
    }
}