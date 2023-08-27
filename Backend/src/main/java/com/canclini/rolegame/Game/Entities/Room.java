package com.canclini.rolegame.Game.Entities;

import com.canclini.rolegame.Game.Entities.Cards.Card;
import com.canclini.rolegame.Game.Entities.Cards.ElfCard;
import com.canclini.rolegame.Game.Entities.Cards.HumanCard;
import com.canclini.rolegame.Game.Entities.Cards.OrcCard;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

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
        ArrayList<Card> hostCards = generateRandomCards(3);
        ArrayList<Card> guestCards = generateRandomCards(3);
        this.hostPlayer.setCards(hostCards);
        this.guestPlayer.setCards(guestCards);
    }

    public static ArrayList<Card> generateRandomCards(int cardQuantity){
        if (cardQuantity <= 0) {
            throw new RuntimeException("The Quantity of Generated Cards Cannot be Less than or Equal to 0");
        }
        String[] medievalNames = {"Guillermo", "Isabel", "Fernando", "Leonor", "Ricardo", "Constanza", "Arturo", "Ginebra", "Lancelot", "Alicia", "Eduardo", "Matilde", "Gonzalo", "Beatriz", "Rodrigo"};
        String[] medievalNicknames = {"El Valiente", "El Justiciero", "El Intrépido", "El Audaz", "El Leal", "El Temerario", "El Sabio", "El Noble", "El Fiero", "El Despiadado", "El Astuto", "El Invencible", "El Bravo", "El Sagaz", "El León de Hierro"};
        String[] birthdates = {"05/12/1990", "10/07/1985", "22/09/1998", "16/03/1979", "08/11/2001", "14/06/1995", "29/12/1982", "03/04/1991", "19/08/1977", "27/01/1993", "12/10/1988", "07/02/2000", "25/11/1996", "18/07/1984", "31/05/1999"};
        Card[] cardConstructors = {new HumanCard(), new ElfCard(), new OrcCard()};


        ArrayList<Card> generatedCards = new ArrayList<>();
        for (int i = 0; i < cardQuantity; i++) {
            Random rand = new Random();
            int cardRaceNumber = rand.nextInt(cardConstructors.length);
            Card card = cardConstructors[cardRaceNumber];
            card = card.clone(); // Crea una nueva instancia de Card
            Field[] cardClassProperties = Card.class.getDeclaredFields();
            for (Field property : cardClassProperties) {
                while (true){
                    try {
                        switch (property.getName()) {
                            case "typeCard" -> card.setTypeCard(cardRaceNumber);
                            case "name" -> card.setName(medievalNames[rand.nextInt(medievalNames.length)]);
                            case "nickname" ->
                                    card.setNickname(medievalNicknames[rand.nextInt(medievalNicknames.length)]);
                            case "birthdate" -> card.setBirthdate(birthdates[rand.nextInt(birthdates.length)]);
                            case "age" -> card.setAge(rand.nextInt(Card.ageMaxValue) + 1); // Genera valores entre 1 y 9
                            case "health" -> card.setHealth(Card.healthMaxValue);
                            case "speed" ->
                                    card.setSpeed((byte) (rand.nextInt(Card.speedMaxValue) + 1)); // Genera valores entre 1 y 9
                            case "dexterity" ->
                                    card.setDexterity((byte) (rand.nextInt(Card.dexterityMaxValue) + 1)); // Genera valores entre 1 y 9
                            case "strength" ->
                                    card.setStrength((byte) (rand.nextInt(Card.strengthMaxValue) + 1)); // Genera valores entre 1 y 9
                            case "magic" ->
                                    card.setMagic((byte) (rand.nextInt(Card.magicMaxValue) + 1)); // Genera valores entre 1 y 9
                            case "level" ->
                                    card.setLevel((byte) (rand.nextInt(Card.levelMaxValue) + 1)); // Genera valores entre 1 y 9
                            case "armor" ->
                                    card.setArmor((byte) (rand.nextInt(Card.armorMaxValue) + 1)); // Genera valores entre 1 y 9
                        }
                    } catch (Error e) {
                        e.printStackTrace();
                        continue;
                    }
                    break;
                }
            }
            card.setCombatStats();
            generatedCards.add(card); // Se agrega una carta a lista
        }
        return generatedCards;
    }
}
