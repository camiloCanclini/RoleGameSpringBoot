package com.canclini.rolegame.gameplay;

public interface CardCombatSystem {
    public int attack(Card targetCard);
    public int defend(Card enemyCard);
    public void levelUp(int level);

}
