package com.example.demo;

public class Room {
    private int row, col, gold;
    private boolean blocked;
    private NPC npc;

    public Room(int row, int col, int gold) {
        this.row = row;
        this.col = col;
        this.gold = gold;
        this.blocked = false;
        this.npc = null;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {

        return col;
    }

    public int getGold() {
        return gold;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public NPC getNpc() {

        return npc;
    }

    public void setGold(int gold) {
        if (gold < 0) gold = 0;
        this.gold = gold;
    }
    public void setBlocked(boolean blocked) {

        this.blocked = blocked;
    }
    public void setNpc(NPC npc) {

        this.npc = npc;
    }
}