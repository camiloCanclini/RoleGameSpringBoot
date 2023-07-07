package com.canclini.rolegame.gameplay;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import com.canclini.rolegame.gameplay.cards.ElfCard;
import com.canclini.rolegame.gameplay.cards.HumanCard;
import com.canclini.rolegame.gameplay.cards.OrcCard;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class Room {
    private Player hostPlayer;
    private Player guestPlayer;
    private Stage stage;
    private int movementsCounter = 0;
    private final int maxMoves = 7;
    private boolean turn = true; // true -> playerOne, false -> playerTwo
    private boolean fullRoom = false;

    @Override
    public String toString() {
        return "Room{" +
                "hostPlayer=" + hostPlayer +
                ", guestPlayer=" + guestPlayer +
                ", stage=" + stage +
                ", movementsCounter=" + movementsCounter +
                ", maxMoves=" + maxMoves +
                ", turn=" + turn +
                ", fullRoom=" + fullRoom +
                '}';
    }

    public Room(Stage stage, boolean turn) {
        this.stage = stage;
        this.turn = turn;
    }

    public void setFullRoom(boolean state){
        this.fullRoom = state;

        this.hostPlayer.setCards(generateRandomCards(3));
        this.guestPlayer.setCards(generateRandomCards(3));
    }

    public static ArrayList<Card> generateRandomCards(int cardQuantity){
        if (cardQuantity <= 0) {
            throw new RuntimeException("The Quantity of Generated Cards Cannot be Less than or Equal to 0");
        }
        String[] medievalNames = {"Guillermo", "Isabel", "Fernando", "Leonor", "Ricardo", "Constanza", "Arturo", "Ginebra", "Lancelot", "Alicia", "Eduardo", "Matilde", "Gonzalo", "Beatriz", "Rodrigo"};
        String[] medievalNicknames = {"El Valiente", "El Justiciero", "El Intrépido", "El Audaz", "El Leal", "El Temerario", "El Sabio", "El Noble", "El Fiero", "El Despiadado", "El Astuto", "El Invencible", "El Bravo", "El Sagaz", "El León de Hierro"};
        String[] birthdates = {"05/12/1990", "10/07/1985", "22/09/1998", "16/03/1979", "08/11/2001", "14/06/1995", "29/12/1982", "03/04/1991", "19/08/1977", "27/01/1993", "12/10/1988", "07/02/2000", "25/11/1996", "18/07/1984", "31/05/1999"};
        Card[] cardConstructors = {new HumanCard(), new ElfCard(), new OrcCard()};

        Random rand = new Random();
        ArrayList<Card> generatedCards = new ArrayList<>();
        for (int i = 0; i < cardQuantity; i++) {
            int cardRaceNumber = rand.nextInt(cardConstructors.length);
            Card card = cardConstructors[cardRaceNumber];
            Field[] cardClassProperties = Card.class.getDeclaredFields();
            for (Field property : cardClassProperties) {
                while (true){
                    try {

                        if (property.getName() == "typeCard"){
                            card.setTypeCard(cardRaceNumber); // Se usa en la aplicacion cliente
                        }
                        if (property.getName() == "name"){
                            card.setName(medievalNames[rand.nextInt(medievalNames.length)]);
                        }
                        if (property.getName() == "nickname")
                            card.setNickname(medievalNicknames[rand.nextInt(medievalNicknames.length)]);
                        if (property.getName() == "birthdate")
                            card.setBirthdate(birthdates[rand.nextInt(birthdates.length)]);
                        if (property.getName() == "age"){
                            //log.info(String.valueOf(rand.nextInt(Card.ageMaxValue)));
                            card.setAge(rand.nextInt(Card.ageMaxValue));
                        }
                        if (property.getName() == "health"){
                            //log.info(String.valueOf(rand.nextInt(Card.ageMaxValue)));
                            card.setHealth(Card.healthMaxValue);
                        }
                        if (property.getName() == "speed")
                            card.setSpeed((byte) rand.nextInt(Card.speedMaxValue));
                        if (property.getName() == "dexterity")
                            card.setDexterity((byte) rand.nextInt(Card.dexterityMaxValue));
                        if (property.getName() == "strength")
                            card.setStrength((byte) rand.nextInt(Card.strengthMaxValue));
                        if (property.getName() == "level")
                            card.setLevel((byte) rand.nextInt(Card.levelMaxValue));
                        if (property.getName() == "armor")
                            card.setArmor((byte) rand.nextInt(Card.armorMaxValue));
                    } catch (Error e) {
                        //log.info("ERROR");
                        continue;
                    }
                    break;
                }
            }
            generatedCards.add(card); // Se agrega una carta a lista
        }
        return generatedCards;
    }
}
