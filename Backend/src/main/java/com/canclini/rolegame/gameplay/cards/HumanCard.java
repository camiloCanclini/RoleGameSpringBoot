package com.canclini.rolegame.gameplay.cards;

import com.canclini.rolegame.gameplay.Card;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HumanCard extends Card{

    public static final int humanAgeMaxValue = 120;

    public HumanCard(String name, String nickname, String birthdate, byte age, byte health, byte speed, byte dexterity, byte strength, byte magic, byte armor, String imageSrc) {
        super(name, nickname, birthdate, age, health, speed, dexterity, strength, magic, armor, imageSrc);
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
    public int hit(Card targetCard) {
        return ((getHitValue() - targetCard.getDefensePower())/500)*100;
    }

    @Override
    public int castSpell(Card targetCard) {
        return ((getSpellValue() - targetCard.getDefensePower())/500)*100;
    }
    @Override
    public int defend(Byte damageValueAttacker, MoveType moveType) { // Get The Damage Received (HIT or SPELL)
        if (moveType == MoveType.HIT) {
            return (int) (damageValueAttacker - (this.getDefensePower()* 1.8));
        }else{
            return (int) (damageValueAttacker - (this.getDefensePower()* 1.15)); // SPELL MOVE TYPE
        }
    }

    @Override
    public void levelUp(Byte level) {
        setLevel(level);
    }
}
