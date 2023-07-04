package com.canclini.rolegame.gameplay.cards;

import com.canclini.rolegame.gameplay.Card;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrcCard extends Card {

    public OrcCard(String name, String nickname, String birthdate, byte age, byte health, byte speed, byte dexterity, byte strength, byte level, byte armor, String imageSrc) {
        super(name, nickname, birthdate, age, health, speed, dexterity, strength, level, armor, imageSrc);
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
