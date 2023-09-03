package com.canclini.rolegame.Controllers.WebSockets;

import com.canclini.rolegame.Controllers.RoomController;
import com.canclini.rolegame.Controllers.WebSockets.Dtos.MovementResultDto;
import com.canclini.rolegame.Controllers.WebSockets.Dtos.PlayerMovementDto;
import com.canclini.rolegame.Controllers.WebSockets.Dtos.WsMessageDto;
import com.canclini.rolegame.Controllers.WebSockets.Dtos.WsResultDto;
import com.canclini.rolegame.Game.Entities.Cards.Card;
import com.canclini.rolegame.Game.Entities.Room;
import com.canclini.rolegame.Game.Logic.CombatLogic;
import com.canclini.rolegame.Game.Logic.PlayerMovement;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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

    public static void sendMessage(Integer roomId, WsMessageDto message) throws MessageConversionException{
        try {
            String jsonResponse = objectMapper.writeValueAsString(message);
            messagingTemplate.convertAndSend("/room/" + roomId, jsonResponse);
        } catch (Exception e) {
            // Manejar el error de serialización
            e.printStackTrace();
        }
    }
    public static void sendMessage(Integer roomId, WsResultDto result) throws MessageConversionException{
        try {
            String jsonResponse = objectMapper.writeValueAsString(result);
            messagingTemplate.convertAndSend("/room/" + roomId, jsonResponse);
        } catch (Exception e) {
            // Manejar el error de serialización
            e.printStackTrace();
        }
    }

    public static void saveMovements(PlayerMovementDto serializedMessage){
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

    public String createLog(PlayerMovementDto serializedMessage){
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

    @MessageMapping("/room/{roomId}/interact")
    public void interactWithRoom (@DestinationVariable Integer roomId, @Payload PlayerMovementDto serializedMessage) throws Exception {

        WsMessageDto messageToSend = new WsMessageDto();
        try {
            messageToSend.type = WsMessageDto.Type.MOVE;
            messageToSend.data = serializedMessage;
            messageToSend.message = createLog(serializedMessage);
            sendMessage(roomId, messageToSend);
            saveMovements(serializedMessage);
            if (roomMovements.get(roomId).getHostMovement() != null && roomMovements.get(roomId).getGuestMovement() != null) {
                log.info("Los 2 jugadores eligieron");
                WsResultDto result = CombatLogic.processingTurn(roomId);
                int finishGame = CombatLogic.GameFinishCheck(roomId);
                if (finishGame != 0) {
                    result.type = WsResultDto.Type.FINISH;
                    switch (finishGame){
                        case 1:
                            result.message = "Player: "+ RoomController.roomList.get(roomId).getHostPlayer().getNamePlayer() +" Wins!";
                            break;
                        case 2:
                            result.message = "Player: "+ RoomController.roomList.get(roomId).getGuestPlayer().getNamePlayer() +" Wins!";
                            break;
                        case 3:
                            result.message = "Draw! No One Wins";
                            break;
                    }

                }
                sendMessage(roomId, result);
                roomMovements.get(roomId).setHostMovement(null);
                roomMovements.get(roomId).setGuestMovement(null);
            }
        } catch (Exception e) {
            messageToSend.type = WsMessageDto.Type.ERROR;
            messageToSend.data = null;
            messageToSend.message = "Error Parsing the Message!";
            sendMessage(roomId, messageToSend);
            e.printStackTrace();
        }

        log.info("Alguien interactuó");
    }
}