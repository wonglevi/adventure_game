package com.example.demo;

public class Player {


    private int hp;
    private int strength;
    private int dexterity;
    private int intelligence;
    private int gold;

    public Player() {
        this.hp = 20;
        this.strength = Dice.rollSum(3, 6);
        this.dexterity = Dice.rollSum(3, 6);
        this.intelligence = Dice.rollSum(3, 6);
        this.gold = 0;


    }


    public int getHp() {
        return hp;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getGold() {
        return gold;
    }


    public void setGold(int gold) {
        if (gold < 0) gold = 0;
        this.gold = gold;
    }

    public void heal () {
        this.hp = 20;
    }

    public void setHp(int hp) {
        if (hp < 0) hp = 0;
        if (hp > 20) hp = 20;
        this.hp = hp;
    }
}