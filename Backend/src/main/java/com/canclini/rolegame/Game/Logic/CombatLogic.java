package com.canclini.rolegame.Game.Logic;

import com.canclini.rolegame.Controllers.WebSockets.Dtos.MovementResultDto;
import com.canclini.rolegame.Controllers.WebSockets.Dtos.WsResultDto;
import com.canclini.rolegame.Controllers.WebSockets.WebSocketController;
import com.canclini.rolegame.Game.Entities.Cards.Card;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
@Slf4j
public class CombatLogic {

    public static void processingTurn(Integer roomId){

        WebSocketController.Movements movements = WebSocketController.roomMovements.get(roomId);

        PlayerMovement hostMove = movements.getHostMovement();
        PlayerMovement guestMove = movements.getGuestMovement();

        String hostCardUsed = hostMove.getCardUsedId();
        String hostTargetCard = hostMove.getTargetCardId();

        String guestCardUsed = guestMove.getCardUsedId();
        String guestTargetCard = guestMove.getTargetCardId();

        String hostMoveType = hostMove.getMoveType();
        String guestMoveType = guestMove.getMoveType();

        WsResultDto messageToSend = new WsResultDto();
        Card hostCard = WebSocketController.searchCardById(roomId, hostCardUsed);
        Card guestCard = WebSocketController.searchCardById(roomId, guestCardUsed);
        int guestDamage = 0;
        int hostDamage = 0;
        int result;

        if (hostTargetCard.equals(guestCardUsed) && hostCardUsed.equals(guestTargetCard)) { // LAS CARTAS SE CRUZAN
            log.info("La cartas se cruzan");
            if (hostMoveType.equals(guestMoveType)) {
                log.info("Mismo tipo de movimientos");
                messageToSend.type = WsResultDto.Type.SHIFT;
                MovementResultDto.PlayerResult hostResult = new MovementResultDto.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, 0,0);
                MovementResultDto.PlayerResult guestResult = new MovementResultDto.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, 0);
                messageToSend.data = new MovementResultDto(hostResult, guestResult, 3); // Empate
                messageToSend.message = "The Moves Were Canceled";
                WebSocketController.sendMessage(roomId, messageToSend);
                return;
            }else{
                log.info("Distinto tipo de movimientos");
                if (hostMoveType.equals("HIT") && guestMoveType.equals("SPELL")) { // HOST HIT WINS

                    messageToSend.type = WsResultDto.Type.SHIFT;

                    guestDamage = guestCard.castSpell(hostCard);
                    hostDamage = hostCard.hit(guestCard);
                    log.info("guestDamage: "+guestDamage);
                    log.info("hostDamage: "+hostDamage);
                    result = (int) (hostDamage - (guestDamage * 0.3)); // HIT WINS

                    if (result < 0){
                        result = 15; // Daño minimo
                    }
                    log.info("result: "+result);
                    guestCard.getDamage((byte) result);

                    MovementResultDto.PlayerResult hostResult = new MovementResultDto.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, result, 0);
                    MovementResultDto.PlayerResult guestResult = new MovementResultDto.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, result);

                    messageToSend.data = new MovementResultDto(hostResult, guestResult, 1);
                    messageToSend.message = "The HIT MOVE Wins";
                    WebSocketController.sendMessage(roomId, messageToSend);
                    return;
                }
                if (hostMoveType.equals("SPELL") && Objects.equals(guestMoveType, "HIT")) { // GUEST HIT GANA

                    messageToSend.type = WsResultDto.Type.SHIFT;

                    hostDamage = hostCard.castSpell(guestCard);
                    guestDamage = guestCard.hit(hostCard);

                    result = (int) (guestDamage - (hostDamage * 0.3)); // HIT WINS

                    if (result < 0){
                        result = 15; // Daño minimo
                    }

                    log.info("guestDamage: "+guestDamage);
                    log.info("hostDamage: "+hostDamage);
                    log.info("result: "+result);

                    hostCard.getDamage((byte) result);

                    MovementResultDto.PlayerResult hostResult = new MovementResultDto.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, result, 0);
                    MovementResultDto.PlayerResult guestResult = new MovementResultDto.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, result);

                    messageToSend.data = new MovementResultDto(hostResult, guestResult, 2);
                    messageToSend.message = "The HIT MOVE Wins";
                    WebSocketController.sendMessage(roomId, messageToSend);
                    return;
                }
            }

        }
        if (hostTargetCard.equals(guestTargetCard)) {
            log.info("Los objetivos Son Iguales");
            // LOGICA DE DEFENSA - UNO ATACA Y EL OTRO DEFIENDE
            int defendValue;
            if (Objects.equals(hostMoveType, "DEFEND") && Objects.equals(guestMoveType, "HIT")){
                // HOST DEFEND GANA

                messageToSend.type = WsResultDto.Type.SHIFT;

                guestDamage = guestCard.hit(hostCard);
                defendValue = hostCard.defend((byte) guestDamage, Card.MoveType.HIT);

                if (defendValue > 0){
                    messageToSend.message = "The Defend Wins (The Card Heals)";
                    MovementResultDto.PlayerResult hostResult = new MovementResultDto.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, defendValue, 0);
                    MovementResultDto.PlayerResult guestResult = new MovementResultDto.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, guestDamage);
                    messageToSend.data = new MovementResultDto(hostResult, guestResult, 1);
                    hostCard.healCard((int) (defendValue*0.15));
                } else {
                    messageToSend.message = "The Defend Lost";
                    MovementResultDto.PlayerResult hostResult = new MovementResultDto.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, defendValue, 0);
                    MovementResultDto.PlayerResult guestResult = new MovementResultDto.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, defendValue);
                    messageToSend.data = new MovementResultDto(hostResult, guestResult, 2);
                    hostCard.getDamage((byte) -defendValue);
                }
                log.info("defendValue: "+defendValue);
                log.info("guestDamage: "+guestDamage);
                log.info("hostDamage: "+hostDamage);
                WebSocketController.sendMessage(roomId, messageToSend);
                return;
            }
            if (Objects.equals(hostMoveType, "DEFEND") && Objects.equals(guestMoveType, "SPELL")){
                // HOST DEFEND PIERDE

                messageToSend.type = WsResultDto.Type.SHIFT;

                guestDamage = guestCard.castSpell(hostCard);
                defendValue = hostCard.defend((byte) guestDamage, Card.MoveType.SPELL);

                if (defendValue > 0){
                    messageToSend.message = "The Defend Wins (The Card Heals)";
                    MovementResultDto.PlayerResult hostResult = new MovementResultDto.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, defendValue, 0);
                    MovementResultDto.PlayerResult guestResult = new MovementResultDto.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0 , defendValue);
                    messageToSend.data = new MovementResultDto(hostResult, guestResult, 1);
                    hostCard.healCard((int) (defendValue*0.15));
                } else {
                    messageToSend.message = "The Defend Lost";
                    MovementResultDto.PlayerResult hostResult = new MovementResultDto.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, defendValue, 0);
                    MovementResultDto.PlayerResult guestResult = new MovementResultDto.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, defendValue);
                    messageToSend.data = new MovementResultDto(hostResult, guestResult, 2);
                    hostCard.getDamage((byte) -defendValue);
                }
                log.info("defendValue: "+defendValue);
                log.info("guestDamage: "+guestDamage);
                log.info("hostDamage: "+hostDamage);
                WebSocketController.sendMessage(roomId, messageToSend);
                return;
            }
            if (Objects.equals(hostMoveType, "HIT") && Objects.equals(guestMoveType, "DEFEND")){
                // GUEST DEFEND GANA

                messageToSend.type = WsResultDto.Type.SHIFT;

                hostDamage = hostCard.hit(guestCard);
                defendValue = guestCard.defend((byte) hostDamage, Card.MoveType.HIT);

                if (defendValue > 0){
                    messageToSend.message = "The Defend Wins (The Card Heals)";
                    MovementResultDto.PlayerResult hostResult = new MovementResultDto.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, 0, 0);
                    MovementResultDto.PlayerResult guestResult = new MovementResultDto.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, 0);
                    messageToSend.data = new MovementResultDto(hostResult, guestResult, 2);
                    guestCard.healCard((int) (defendValue*0.15));
                } else {
                    messageToSend.message = "The Defend Lost";
                    MovementResultDto.PlayerResult hostResult = new MovementResultDto.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, 0, defendValue);
                    MovementResultDto.PlayerResult guestResult = new MovementResultDto.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, defendValue, 0);
                    messageToSend.data = new MovementResultDto(hostResult, guestResult, 1);
                    guestCard.getDamage((byte) -defendValue);
                }
                log.info("defendValue: "+defendValue);
                log.info("guestDamage: "+guestDamage);
                log.info("hostDamage: "+hostDamage);
                WebSocketController.sendMessage(roomId, messageToSend);
                return;
            }
            if (Objects.equals(hostMoveType, "SPELL") && Objects.equals(guestMoveType, "DEFEND")){
                // GUEST DEFEND PIERDE

                messageToSend.type = WsResultDto.Type.SHIFT;

                hostDamage = hostCard.hit(guestCard);
                defendValue = guestCard.defend((byte) hostDamage, Card.MoveType.SPELL);

                if (defendValue > 0){
                    messageToSend.message = "The Defend Wins (The Card Heals)";
                    MovementResultDto.PlayerResult hostResult = new MovementResultDto.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, 0, 0);
                    MovementResultDto.PlayerResult guestResult = new MovementResultDto.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, 0, 0);
                    messageToSend.data = new MovementResultDto(hostResult, guestResult, 2);
                    guestCard.healCard((int) (defendValue*0.15));
                } else {
                    messageToSend.message = "The Defend Lost";
                    MovementResultDto.PlayerResult hostResult = new MovementResultDto.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, 0, defendValue);
                    MovementResultDto.PlayerResult guestResult = new MovementResultDto.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, defendValue, 0);
                    messageToSend.data = new MovementResultDto(hostResult, guestResult, 1);
                    guestCard.getDamage((byte) -defendValue);
                }
                log.info("defendValue: "+defendValue);
                log.info("guestDamage: "+guestDamage);
                log.info("hostDamage: "+hostDamage);
                WebSocketController.sendMessage(roomId, messageToSend);
                return;
            }
        } else {
            log.info("Los objetivos Son Distintos");
            if (Objects.equals(hostMoveType, "HIT")){
                // HOST HIT
                hostDamage = hostCard.hit(guestCard);
                if (hostDamage <= 0) {
                    log.info("check");
                    hostDamage = 10;
                }
                guestCard.getDamage((byte) hostDamage);
            }
            if (Objects.equals(hostMoveType, "SPELL")){
                // HOST SPELL
                hostDamage = hostCard.castSpell(guestCard);
                if (hostDamage <= 0) {
                    log.info("check");
                    hostDamage = 10;
                }
                guestCard.getDamage((byte) hostDamage);
            }
            if (Objects.equals(guestMoveType, "HIT")){
                // GUEST HIT
                guestDamage = guestCard.hit(hostCard);
                if (guestDamage <= 0) {
                    log.info("check");
                    guestDamage = 10;
                }
                hostCard.getDamage((byte) guestDamage);
            }


            if (Objects.equals(guestMoveType, "SPELL")){
                // GUEST SPELL
                guestDamage = guestCard.castSpell(hostCard);
                if (guestDamage <= 0) {
                    log.info("check");
                    guestDamage = 10;
                }
                hostCard.getDamage((byte) guestDamage);
            }
            log.info("guestDamage: "+guestDamage);
            log.info("hostDamage: "+hostDamage);
            MovementResultDto.PlayerResult hostResult = new MovementResultDto.PlayerResult(hostTargetCard, hostCardUsed, hostMoveType, guestDamage, hostDamage);
            MovementResultDto.PlayerResult guestResult = new MovementResultDto.PlayerResult(guestTargetCard, guestCardUsed, guestMoveType, hostDamage, guestDamage);
            messageToSend.data = new MovementResultDto(hostResult, guestResult, 3);
            messageToSend.type = WsResultDto.Type.SHIFT;
            messageToSend.message = "Both Movements Take Effect";
            WebSocketController.sendMessage(roomId, messageToSend);
        }


    }
}
