package com.canclini.rolegame.gameplay;

public interface CardCombatSystem {
    public int hit(Card targetCard);
    public int castSpell(Card targetCard);
    public int defend(Card targetCard);
    public void levelUp(int level);

}
