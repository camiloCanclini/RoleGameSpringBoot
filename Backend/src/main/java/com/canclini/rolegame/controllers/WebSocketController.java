package com.canclini.rolegame.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;

import java.util.Set;

@Controller
public class WebSocketController {

    private Map<Integer, Set<String>> roomSubscriptions = new HashMap<>();
    private SimpMessagingTemplate messagingTemplate;

    public void RoomWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    @SubscribeMapping("/room/{roomId}")
    public void subscribeToRoomUpdates(@DestinationVariable Integer roomId, SimpMessageHeaderAccessor headerAccessor) {
        Set<String> subscribers = roomSubscriptions.getOrDefault(roomId, new HashSet<>());
        if (RoomController.roomList.get(roomId) == null) {
            // La room no existe
            String errorMessage = "La sala no existe!";
            messagingTemplate.convertAndSend("/room/" + roomId, errorMessage);
            return;
        }
        if (subscribers.size() >= 2) {
            // Ya hay dos suscriptores en el canal, enviar mensaje de sala llena
            String errorMessage = "La sala está llena. No se permite más jugadores.";
            messagingTemplate.convertAndSend("/room/" + roomId, errorMessage);
            return;
        }

        // Suscription Player (client) to the room
        String sessionId = headerAccessor.getSessionId();
        subscribers.add(sessionId);
        roomSubscriptions.put(roomId, subscribers);

    }



}