package com.example.demo;

public class NPC {

    private int hp;
    private int strength;
    private int dexterity;
    private int intelligence;



    public NPC() {
        int base = Dice.roll(6);
        this.hp = base;
        this.strength = base * 2;
        this.dexterity = base * 2;
        this.intelligence = base * 2;
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

    public void setHp(int hp) {
        if (hp < 0) hp = 0;
        this.hp = hp;
    }


}