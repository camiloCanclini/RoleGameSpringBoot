package com.canclini.rolegame.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/room/{roomId}/join")
    public void joinRoom(@DestinationVariable int roomId) {
        // Lógica para unir al jugador a la sala con el ID proporcionado
    }

    @MessageMapping("/room/{roomId}/start")
    public void startGame(@DestinationVariable int roomId) {
        // Lógica para iniciar el juego en la sala con el ID proporcionado
    }

    // Otros métodos controladores de mensajes

}