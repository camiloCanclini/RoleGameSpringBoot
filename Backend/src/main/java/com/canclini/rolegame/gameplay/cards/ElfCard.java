package com.canclini.rolegame.gameplay.cards;

import com.canclini.rolegame.gameplay.Card;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ElfCard extends Card {

    public ElfCard(String name, String nickname, String birthdate, byte age, byte health, byte speed, byte dexterity, byte strength, byte level, byte armor, String imageSrc) {
        super(name, nickname, birthdate, age, health, speed, dexterity, strength, level, armor, imageSrc);
    }

    @Override
    public int hit(Card targetCard) {
        return 0;
    }

    @Override
    public int castSpell(Card targetCard) {
        return 0;
    }
    @Override
    public int defend(Card targetCard) {
        return 0;
    }

    @Override
    public void levelUp(int level) {

    }
}
