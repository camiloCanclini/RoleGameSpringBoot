package com.canclini.rolegame.gameplay.cards;

import com.canclini.rolegame.gameplay.Card;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HumanCard extends Card{

    public static final int humanAgeMaxValue = 120;
    public HumanCard(String name, String nickname, String birthdate, byte age, byte health, byte speed, byte dexterity, byte strength, byte level, byte armor, String imageSrc) {
        super(name, nickname, birthdate, age, health, speed, dexterity, strength, level, armor, imageSrc);
    }

    @Override
    public void setAge(int age) {
        // The Humans Cannot Live more than 120 years
        if (age < 0 || age > humanAgeMaxValue) {
            throw new Error("Error Trying to Set the Age Atrribute");
        }
        this.age = age;
    }

    @Override
    public int attack(Card targetCard) {
        return 0;
    }

    @Override
    public int defend(Card enemyCard) {
        return 0;
    }

    @Override
    public void levelUp(int level) {

    }
}
