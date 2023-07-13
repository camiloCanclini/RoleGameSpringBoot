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
        return (int) ((((getHitValue() - targetCard.getDefensePower())/500)*100)*1.4);
    }

    @Override
    public int castSpell(Card targetCard) {
        return (int) (((getSpellValue() - targetCard.getDefensePower())/500)*100);
    }
    @Override
    public int defend(Byte damageValueAttacker, MoveType moveType) { // Get The Damage Received (HIT or SPELL)
        if (moveType == MoveType.HIT) {
            return (int) (damageValueAttacker - (this.getDefensePower()* 1.3));
        }else{
            return (int) (damageValueAttacker - (this.getDefensePower()* 1.2)); // SPELL MOVE TYPE
        }
    }

    @Override
    public void levelUp(Byte level) {
        setLevel(level);
    }
}
