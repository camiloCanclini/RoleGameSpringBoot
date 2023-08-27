package com.canclini.rolegame.Game.Entities.Cards;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ElfCard extends Card {

    public ElfCard(String name, String nickname, String birthdate, byte age, byte health, byte speed, byte dexterity, byte strength, byte level, byte armor, String imageSrc) {
        super(name, nickname, birthdate, age, health, speed, dexterity, strength, level, armor, imageSrc);
    }

    @Override
    public int hit(Card targetCard) {
        int result = (int) ((((getHitValue() - targetCard.getDefensePower())/500.0)*100))*3;
        return Math.max(result, 0);
    }

    @Override
    public int castSpell(Card targetCard) {
        int result = (int) ((((getSpellValue() - targetCard.getDefensePower())/500.0)*100)*1.4)*3;
        return Math.max(result, 0);
    }
    @Override
    public int defend(Byte damageValueAttacker, MoveType moveType) { // Get The Damage Received (HIT or SPELL)
        if (moveType == MoveType.HIT) {
            return (int) ((this.getDefensePower()* 1.3) - damageValueAttacker)*2;
        }else{
            return (int) ((this.getDefensePower()* 0.2) - damageValueAttacker)*2; // SPELL MOVE TYPE
        }
    }

    @Override
    public void levelUp(Byte level) {
        setLevel(level);
    }
}
