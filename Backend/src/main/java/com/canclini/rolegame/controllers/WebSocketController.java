package com.canclini.rolegame.controllers;

import com.canclini.rolegame.gameplay.Card;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.canclini.rolegame.gameplay.Room;

import java.util.ArrayList;
import java.util.Objects;


@Controller
@Slf4j
public class WebSocketController {

    @Autowired
    private static SimpMessagingTemplate messagingTemplate;
    @Autowired
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
        public String message;
        public PlayerMovement data;
    }

    @Getter
    @Setter
    public static class PlayerMovement{

        public Integer roomId;
        public Integer idPlayer;
        public String targetCard; // Using the List Index
        public String cardUsed;
        public String moveType;

        @JsonCreator
        public PlayerMovement(@JsonProperty("roomId") Integer roomId,@JsonProperty("idPlayer") Integer idPlayer, @JsonProperty("targetCard") String targetCard, @JsonProperty("cardUsed") String cardUsed, @JsonProperty("moveType") String moveType) {
            this.roomId = roomId;
            this.idPlayer = idPlayer;
            this.targetCard = targetCard;
            this.cardUsed = cardUsed;
            this.moveType = moveType;
        }
    }

    public static void sendMessage(Integer roomId, WsMessageModel message) throws MessageConversionException{
        try {
            String jsonResponse = objectMapper.writeValueAsString(message);
            messagingTemplate.convertAndSend("/room/" + roomId, jsonResponse);
        } catch (Exception e) {
            // Manejar el error de serialización
            e.printStackTrace();
        }
    }

    public static String processingMovement(PlayerMovement serializedMessage){
        Room room = RoomController.roomList.get(serializedMessage.roomId);
        String cardPrefix = serializedMessage.idPlayer == 1 ? "HC" : "GC"; // 1 -> host, 2 -> guest
        String playerName;
        String cardUsedName = "";
        String cardTargetName = "";
        String moveType = null;

        if (cardPrefix.equals("HC")) {
            playerName = room.getHostPlayer().getNamePlayer();
        } else {
            playerName = room.getGuestPlayer().getNamePlayer();
        }

        ArrayList<Card> hostCards = room.getHostPlayer().getCards();
        ArrayList<Card> guestCards = room.getGuestPlayer().getCards();

        if (Objects.equals(serializedMessage.cardUsed.split("-")[0], "HC")){

            int cardIndex = Integer.parseInt(serializedMessage.cardUsed.split("-")[1]);
            Card cardUsed = hostCards.get(cardIndex);
            cardUsedName = cardUsed.getName();
        }else{

            int cardIndex = Integer.parseInt(serializedMessage.cardUsed.split("-")[1]);
            Card cardUsed = guestCards.get(cardIndex);
            cardUsedName = cardUsed.getName();
        }

        if(Objects.equals(serializedMessage.targetCard.split("-")[0], "HC")){

            int cardIndex = Integer.parseInt(serializedMessage.targetCard.split("-")[1]);
            Card cardTarget = hostCards.get(cardIndex);
            cardTargetName = cardTarget.getName();
        }else{

            int cardIndex = Integer.parseInt(serializedMessage.targetCard.split("-")[1]);
            Card cardTarget = guestCards.get(cardIndex);
            cardTargetName = cardTarget.getName();
        }

        if (Objects.equals(serializedMessage.moveType, "HIT")){
            moveType = "HITS";
        }
        if (Objects.equals(serializedMessage.moveType, "DEFEND")){
            moveType = "DEFENDS";
        }
        if (Objects.equals(serializedMessage.moveType, "SPELL")){
            moveType = "CASTS a SPELL on";
        }

        return "("+playerName+") "+ moveType +" "+cardTargetName+ " with "+ cardUsedName;
    }

    private int testFlag;
    @MessageMapping("/room/{roomId}/interact")
    public void interactWithRoom (@DestinationVariable Integer roomId, @Payload PlayerMovement serializedMessage) throws Exception {
        testFlag++;
        log.info(String.valueOf(testFlag));
        WsMessageModel messageToSend = new WsMessageModel();
        try {
            messageToSend.type = WsMessageModel.Type.MOVE;
            messageToSend.data = serializedMessage;
            messageToSend.message = processingMovement(serializedMessage);
            sendMessage(roomId, messageToSend);
        } catch (Exception e) {
            messageToSend.type = WsMessageModel.Type.ERROR;
            messageToSend.data = null;
            messageToSend.message = "Error Parsing the Message!";
            sendMessage(roomId, messageToSend);
            e.printStackTrace();
        }
        log.info("Alguien interactuó");
    }
}