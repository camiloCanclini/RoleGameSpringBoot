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
    public static class Movements{
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
    public static class WsResultModel {
        public enum Type{
            ERROR,
            ROOMREADY,
            MOVE,
            SHIFT
        }
        public Type type;
        public String message;
        public MovementsResult data;
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
    @AllArgsConstructor
    public static class MovementsResult{
        @Setter
        @Getter
        @AllArgsConstructor
        private static class PlayerResult{
            private String targetCardId;
            private String cardUsedId;
            private String moveType;
            private Integer damageReceived;
            private Integer damageDone;
        }
        private PlayerResult hostResult;
        private PlayerResult guestResult;
        private Integer whoWins; // 1 -> host | 2 -> guest | 3 -> nothing
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
    public static void sendMessage(Integer roomId, WsResultModel result) throws MessageConversionException{
        try {
            String jsonResponse = objectMapper.writeValueAsString(result);
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

        if (Objects.equals(serializedMessage.moveType, "HIT") || Objects.equals(serializedMessage.moveType, "DEFEND") || Objects.equals(serializedMessage.moveType, "SPELL")){
            moveType = serializedMessage.moveType;
        }else{
            throw new RuntimeException();
        }

        PlayerMovement movement = new PlayerMovement(playerId, serializedMessage.targetCard, targetCard, serializedMessage.cardUsed,cardUsed, moveType);

        if (!(roomMovements.containsKey(serializedMessage.roomId))) {
            roomMovements.put(serializedMessage.roomId, new Movements());
        }
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

    public static Card searchCardById(Integer roomId, String cardId){

        if (!RoomController.roomList.containsKey(roomId)){
            return null;
        }
        Room room = RoomController.roomList.get(roomId);
        String cardPrefix = cardId.split("-")[0];
        String cardNumberString = cardId.split("-")[1];
        int cardNumber;
        try {
            cardNumber = Integer.parseInt(cardNumberString);
        }catch (NumberFormatException e){
            return null;
        }

        if (Objects.equals(cardPrefix, "HC")) {
            ArrayList<Card> hostCards = room.getHostPlayer().getCards();
            if (cardNumber < hostCards.size()) {
                return hostCards.get(cardNumber);
            }
        } else if (Objects.equals(cardPrefix, "GC")) {
            ArrayList<Card> guestCards = room.getGuestPlayer().getCards();
            if (cardNumber < guestCards.size()) {
                return guestCards.get(cardNumber);
            }
        } else{
            return null;
        }
        return null;
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

        WsResultModel messageToSend = new WsResultModel();
        Card hostCard = searchCardById(roomId, hostCardUsed);
        Card guestCard = searchCardById(roomId, guestCardUsed);
        int guestDamage;
        int hostDamage;
        int result;

        if (hostTargetCard.equals(guestCardUsed) && hostCardUsed.equals(guestTargetCard)) { // LAS CARTAS SE CRUZAN
            if (hostMoveType.equals(guestMoveType)) {
                messageToSend.type = WsResultModel.Type.SHIFT;
                messageToSend.data = new MovementsResult(null, null, 3); // Empate
                messageToSend.message = "The Moves Were Canceled";
                sendMessage(roomId, messageToSend);
                return;
            }else{

                if (hostMoveType.equals("HIT") && guestMoveType.equals("SPELL")) { // HOST HIT WINS

                    messageToSend.type = WsResultModel.Type.SHIFT;

                    guestDamage = guestCard.castSpell(hostCard);
                    hostDamage = hostCard.hit(guestCard);
                    result = (int) (hostDamage - (guestDamage * 0.3)); // HIT WINS

                    if (result < 0){
                        result = 5; // Daño minimo
                    }
                    guestCard.getDamage((byte) result);

                    MovementsResult.PlayerResult hostResult = new MovementsResult.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, result, 0);
                    MovementsResult.PlayerResult guestResult = new MovementsResult.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, result);

                    messageToSend.data = new MovementsResult(hostResult, guestResult, 1);
                    messageToSend.message = "The HIT MOVE Wins";
                    sendMessage(roomId, messageToSend);
                    return;
                }
                if (hostMoveType.equals("SPELL") && Objects.equals(guestMoveType, "HIT")) { // GUEST HIT GANA

                    messageToSend.type = WsResultModel.Type.SHIFT;

                    hostDamage = hostCard.castSpell(guestCard);
                    guestDamage = guestCard.hit(hostCard);

                    result = (int) (guestDamage - (hostDamage * 0.3)); // HIT WINS

                    if (result < 0){
                        result = 5; // Daño minimo
                    }
                    hostCard.getDamage((byte) result);

                    MovementsResult.PlayerResult hostResult = new MovementsResult.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, result, 0);
                    MovementsResult.PlayerResult guestResult = new MovementsResult.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, result);

                    messageToSend.data = new MovementsResult(hostResult, guestResult, 2);
                    messageToSend.message = "The HIT MOVE Wins";
                    sendMessage(roomId, messageToSend);
                    return;
                }
            }

        }
        if (hostTargetCard.equals(guestTargetCard)) {
            // LOGICA DE DEFENSA - UNO ATACA Y EL OTRO DEFIENDE
            int defendValue;
            if (Objects.equals(hostMoveType, "DEFEND") && Objects.equals(guestMoveType, "HIT")){
                // HOST DEFEND GANA

                messageToSend.type = WsResultModel.Type.SHIFT;

                guestDamage = guestCard.hit(hostCard);
                defendValue = hostCard.defend((byte) guestDamage, Card.MoveType.HIT);

                if (defendValue > 0){
                    messageToSend.message = "The Defend Wins (The Card Heals)";
                    MovementsResult.PlayerResult hostResult = new MovementsResult.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, defendValue, 0);
                    MovementsResult.PlayerResult guestResult = new MovementsResult.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, guestDamage);
                    messageToSend.data = new MovementsResult(hostResult, guestResult, 1);
                    hostCard.healCard(defendValue);
                } else {
                    messageToSend.message = "The Defend Lost";
                    MovementsResult.PlayerResult hostResult = new MovementsResult.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, defendValue, 0);
                    MovementsResult.PlayerResult guestResult = new MovementsResult.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, defendValue);
                    messageToSend.data = new MovementsResult(hostResult, guestResult, 2);
                    hostCard.getDamage((byte) ~(defendValue - 1));
                }

                sendMessage(roomId, messageToSend);
                return;
            }
            if (Objects.equals(hostMoveType, "DEFEND") && Objects.equals(guestMoveType, "SPELL")){
                // HOST DEFEND PIERDE

                messageToSend.type = WsResultModel.Type.SHIFT;

                guestDamage = guestCard.castSpell(hostCard);
                defendValue = hostCard.defend((byte) guestDamage, Card.MoveType.SPELL);

                if (defendValue > 0){
                    messageToSend.message = "The Defend Wins (The Card Heals)";
                    MovementsResult.PlayerResult hostResult = new MovementsResult.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, defendValue, 0);
                    MovementsResult.PlayerResult guestResult = new MovementsResult.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0 , defendValue);
                    messageToSend.data = new MovementsResult(hostResult, guestResult, 1);
                    hostCard.healCard(defendValue);
                } else {
                    messageToSend.message = "The Defend Lost";
                    MovementsResult.PlayerResult hostResult = new MovementsResult.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, defendValue, 0);
                    MovementsResult.PlayerResult guestResult = new MovementsResult.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, defendValue);
                    messageToSend.data = new MovementsResult(hostResult, guestResult, 2);
                    hostCard.getDamage((byte) ~(defendValue - 1));
                }

                sendMessage(roomId, messageToSend);
                return;
            }
            if (Objects.equals(hostMoveType, "HIT") && Objects.equals(guestMoveType, "DEFEND")){
                // GUEST DEFEND GANA

                messageToSend.type = WsResultModel.Type.SHIFT;

                hostDamage = hostCard.hit(guestCard);
                defendValue = guestCard.defend((byte) hostDamage, Card.MoveType.HIT);

                if (defendValue > 0){
                    messageToSend.message = "The Defend Wins (The Card Heals)";
                    MovementsResult.PlayerResult hostResult = new MovementsResult.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, 0, 0);
                    MovementsResult.PlayerResult guestResult = new MovementsResult.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, 0);
                    messageToSend.data = new MovementsResult(hostResult, guestResult, 2);
                    guestCard.healCard(defendValue);
                } else {
                    messageToSend.message = "The Defend Lost";
                    MovementsResult.PlayerResult hostResult = new MovementsResult.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, 0, defendValue);
                    MovementsResult.PlayerResult guestResult = new MovementsResult.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, defendValue, 0);
                    messageToSend.data = new MovementsResult(hostResult, guestResult, 1);
                    guestCard.getDamage((byte) ~(defendValue - 1));
                }

                sendMessage(roomId, messageToSend);
                return;
            }
            if (Objects.equals(hostMoveType, "SPELL") && Objects.equals(guestMoveType, "DEFEND")){
                // GUEST DEFEND PIERDE

                messageToSend.type = WsResultModel.Type.SHIFT;

                hostDamage = hostCard.hit(guestCard);
                defendValue = guestCard.defend((byte) hostDamage, Card.MoveType.SPELL);

                if (defendValue > 0){
                    messageToSend.message = "The Defend Wins (The Card Heals)";
                    MovementsResult.PlayerResult hostResult = new MovementsResult.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, 0, 0);
                    MovementsResult.PlayerResult guestResult = new MovementsResult.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, 0);
                    messageToSend.data = new MovementsResult(hostResult, guestResult, 2);
                    guestCard.healCard(defendValue);
                } else {
                    messageToSend.message = "The Defend Lost";
                    MovementsResult.PlayerResult hostResult = new MovementsResult.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, 0, defendValue);
                    MovementsResult.PlayerResult guestResult = new MovementsResult.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, defendValue, 0);
                    messageToSend.data = new MovementsResult(hostResult, guestResult, 1);
                    guestCard.getDamage((byte) ~(defendValue - 1));
                }

                sendMessage(roomId, messageToSend);
                return;
            }
        } else {
            if (Objects.equals(hostMoveType, "HIT")){
                // HOST HIT
            }
            if (Objects.equals(hostMoveType, "SPELL")){
                // HOST SPELL
            }
            if (Objects.equals(guestMoveType, "HIT")){
                // GUEST HIT
            }
            if (Objects.equals(guestMoveType, "SPELL")){
                // GUEST SPELL
            }
            return;
        }


        messageToSend.type = WsResultModel.Type.SHIFT;
        messageToSend.data = null;
        messageToSend.message = "Nothing Happend";
        sendMessage(roomId, messageToSend);
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
            if (roomMovements.get(roomId).getHostMovement() != null && roomMovements.get(roomId).getGuestMovement() != null) {
                log.info("Los 2 jugadores eligieron");
                processingTurn(roomId);
                roomMovements.get(roomId).setHostMovement(null);
                roomMovements.get(roomId).setGuestMovement(null);
            }
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