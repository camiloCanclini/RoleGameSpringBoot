package com.canclini.rolegame.gameplay.cards;

import com.canclini.rolegame.gameplay.Card;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class HumanCard extends Card{

    public static final int humanAgeMaxValue = 120;

    public HumanCard(String name, String nickname, String birthdate, byte age, byte health, byte speed, byte dexterity, byte strength, byte magic, byte armor, String imageSrc) {
        super(name, nickname, birthdate, age, health, speed, dexterity, strength, magic, armor, imageSrc);
    }

    @Override
    public void setAge(int age) {
        // The Humans Cannot Live more than 120 years
        if (age > humanAgeMaxValue){
            this.age = humanAgeMaxValue;
        }
        if (age < 0 ) {
            throw new Error("Error Trying to Set the Age Atrribute");
        }
        this.age = age;
    }

    @Override
    public int hit(Card targetCard) {
        int result = (int) (((getHitValue() - targetCard.getDefensePower())/500.0)*100);
        return result < 0 ? 0 : result;
    }

    @Override
    public int castSpell(Card targetCard) {
        int result = (int) (((getSpellValue() - targetCard.getDefensePower())/500.0)*100);
        return result < 0 ? 0 : result;
    }
    @Override
    public int defend(Byte damageValueAttacker, MoveType moveType) { // Get The Damage Received (HIT or SPELL)
        if (moveType == MoveType.HIT) {
            return (int) ((this.getDefensePower()* 1.8) - damageValueAttacker); // Si el resultado es negativo se devuelve 0
        }else{
            return (int) ((this.getDefensePower()* 0.35) - damageValueAttacker); // SPELL MOVE TYPE
        }
    }

    @Override
    public void levelUp(Byte level) {
        setLevel(level);
    }
}
