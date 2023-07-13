package com.canclini.rolegame.gameplay;

public interface CardCombatSystem {
    public int hit(Card targetCard);
    public int castSpell(Card targetCard);
    public int defend(Byte damageValueAttacker, Card.MoveType moveType);
    public void levelUp(Byte level);

}
