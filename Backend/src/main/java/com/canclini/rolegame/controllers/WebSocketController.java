package com.canclini.rolegame.controllers;

import com.canclini.rolegame.gameplay.Card;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Controller
@Slf4j
public class WebSocketController {

    @Autowired
    private static SimpMessagingTemplate messagingTemplate;
    @Autowired
    private static ObjectMapper objectMapper;

    @Getter
    @Setter
    @AllArgsConstructor
    private static class PlayerMovement{
        private Integer idPlayer;
        private String targetCardId;
        private Card targetCard;
        private String cardUsedId;
        private Card cardUsed;
        private String moveType;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class Movements{
        private PlayerMovement hostMovement;
        private PlayerMovement guestMovement;
    }
    public static Map<Integer, Movements> roomMovements = new HashMap<>();

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
        public ModelPlayerMovement data;
    }

    @Getter
    @Setter
    public static class  ModelPlayerMovement {

        public Integer roomId;
        public Integer idPlayer;
        public String targetCard; // Using the List Index
        public String cardUsed;
        public String moveType;

        @JsonCreator
        public ModelPlayerMovement(@JsonProperty("roomId") Integer roomId, @JsonProperty("idPlayer") Integer idPlayer, @JsonProperty("targetCard") String targetCard, @JsonProperty("cardUsed") String cardUsed, @JsonProperty("moveType") String moveType) {
            this.roomId = roomId;
            this.idPlayer = idPlayer;
            this.targetCard = targetCard;
            this.cardUsed = cardUsed;
            this.moveType = moveType;
        }
    }

    @Getter
    @Setter
    public class MovementsResult{
        @Setter
        @Getter
        @AllArgsConstructor
        private class PlayerResult{
            private String targetCardId;
            private String cardUsedId;
            private String moveType;
            private Integer damageReceived;
            private Integer damageDone;
        }
        private PlayerResult hostResult;
        private PlayerResult guestResult;
        private Integer whoWins; // 1 -> host | 2 -> guest
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

    public static void saveMovements(ModelPlayerMovement serializedMessage){
        Room room = RoomController.roomList.get(serializedMessage.roomId);
        Integer playerId = serializedMessage.idPlayer; // 1 -> host, 2 -> guest
        Card targetCard = null;
        Card cardUsed = null;
        String moveType = null;

        ArrayList<Card> hostCards = room.getHostPlayer().getCards();
        ArrayList<Card> guestCards = room.getGuestPlayer().getCards();

        if (Objects.equals(serializedMessage.cardUsed.split("-")[0], "HC")){

            int cardIndex = Integer.parseInt(serializedMessage.cardUsed.split("-")[1]);
            cardUsed = hostCards.get(cardIndex);
        }else{

            int cardIndex = Integer.parseInt(serializedMessage.cardUsed.split("-")[1]);
            cardUsed = guestCards.get(cardIndex);
        }

        if(Objects.equals(serializedMessage.targetCard.split("-")[0], "HC")){

            int cardIndex = Integer.parseInt(serializedMessage.targetCard.split("-")[1]);
            targetCard = hostCards.get(cardIndex);
        }else{

            int cardIndex = Integer.parseInt(serializedMessage.targetCard.split("-")[1]);
            targetCard = guestCards.get(cardIndex);
        }

        if (serializedMessage.moveType == "HIT" || serializedMessage.moveType == "DEFEND" || serializedMessage.moveType == "SPELL"){
            moveType = serializedMessage.moveType;
        }else{
            throw new RuntimeException();
        }

        PlayerMovement movement = new PlayerMovement(playerId, serializedMessage.targetCard, targetCard, serializedMessage.cardUsed,cardUsed, moveType);
        Movements playerMovements = roomMovements.get(serializedMessage.roomId);
        if (playerId == 1) {
            playerMovements.setHostMovement(movement);
        } else {
            playerMovements.setGuestMovement(movement);
        }
    }

    public String createLog(ModelPlayerMovement serializedMessage){
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

    public static void processingTurn(Integer roomId){
        Movements movements = roomMovements.get(roomId);
        PlayerMovement hostMove = movements.getHostMovement();
        PlayerMovement guestMove = movements.getGuestMovement();
        String hostCardUsed = hostMove.getCardUsedId();
        String hostTargetCard = hostMove.getTargetCardId();
        String hostMoveType = hostMove.getMoveType();
        String guestCardUsed = guestMove.getCardUsedId();
        String guestTargetCard = guestMove.getTargetCardId();
        String guestMoveType = guestMove.getMoveType();

        if (hostTargetCard == guestCardUsed && hostCardUsed == guestTargetCard) { // LAS CARTAS SE CRUZAN
            if (hostMoveType == guestMoveType) {
                WsMessageModel messageToSend = new WsMessageModel();
                messageToSend.type = WsMessageModel.Type.SHIFT;
                messageToSend.data = null;
                messageToSend.message = "The Moves Were Canceled";
                sendMessage(roomId, messageToSend);
                return;
            }else{
                if (hostMoveType == "HIT" && guestMoveType == "SPELL") {
                    // HOST HIT GANA
                    return;
                }
                if (hostMoveType == "SPELL" && guestMoveType == "HIT") {
                    // HOST SPELL GANA
                    return;
                }
                if (guestMoveType == "HIT" && hostMoveType == "SPELL") {
                    // GUEST HIT GANA
                    return;
                }
                if (guestMoveType == "SPELL" && hostMoveType == "HIT") {
                    // GUEST SPELL GANA
                    return;
                }
            }

        }
        if (hostTargetCard == guestTargetCard) {                       // LOGICA DE DEFENSA - UNO ATACA Y EL OTRO DEFIENDE
            if (hostMoveType == "DEFEND" && guestMoveType == "HIT"){
                // HOST DEFEND GANA
                return;
            }
            if (hostMoveType == "DEFEND" && guestMoveType == "SPELL"){
                // HOST DEFEND PIERDE
                return;
            }
            if (hostMoveType == "HIT" && guestMoveType == "DEFEND"){
                // HOST HIT PIERDE
                return;
            }
            if (hostMoveType == "SPELL" && guestMoveType == "DEFEND"){
                // HOST SPELL GANA
                return;
            }
        } else {
            if (hostMoveType == "HIT"){
                // HOST HIT
            }
            if (hostMoveType == "SPELL"){
                // HOST SPELL
            }
            if (guestMoveType == "HIT"){
                // GUEST HIT
            }
            if (guestMoveType == "SPELL"){
                // GUEST SPELL
            }
            return;
        }


        WsMessageModel messageToSend = new WsMessageModel();
        messageToSend.type = WsMessageModel.Type.SHIFT;
        messageToSend.data = null;
        messageToSend.message = "Nothing Happend";
        sendMessage(roomId, messageToSend);

        //movements.setHostMovement(null);
        //movements.setGuestMovement(null);
    }
    @MessageMapping("/room/{roomId}/interact")
    public void interactWithRoom (@DestinationVariable Integer roomId, @Payload ModelPlayerMovement serializedMessage) throws Exception {

        WsMessageModel messageToSend = new WsMessageModel();
        try {
            messageToSend.type = WsMessageModel.Type.MOVE;
            messageToSend.data = serializedMessage;
            messageToSend.message = createLog(serializedMessage);
            sendMessage(roomId, messageToSend);
            saveMovements(serializedMessage);
        } catch (Exception e) {
            messageToSend.type = WsMessageModel.Type.ERROR;
            messageToSend.data = null;
            messageToSend.message = "Error Parsing the Message!";
            sendMessage(roomId, messageToSend);
            e.printStackTrace();
        }
        processingTurn(roomId);
        log.info("Alguien interactuó");
    }
}