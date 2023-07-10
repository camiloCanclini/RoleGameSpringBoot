package com.canclini.rolegame.gameplay.cards;

import com.canclini.rolegame.gameplay.Card;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrcCard extends Card {

    public OrcCard(String name, String nickname, String birthdate, byte age, byte health, byte speed, byte dexterity, byte strength, byte magic, byte armor, String imageSrc) {
        super(name, nickname, birthdate, age, health, speed, dexterity, strength, magic, armor, imageSrc);
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
