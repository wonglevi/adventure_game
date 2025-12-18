package com.example.demo;

import java.util.Random;

public class Map {
    private int size = 10;
    private Room[][] map;


    public Map(int size) {
        this.size = size;
        map = new Room[size][size];
        generatemap();
    }

    private void generatemap() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {

                int gold = Dice.roll(10);

                Room room = new Room(row, col, gold);


                if (Dice.roll(10) == 1) {
                    room.setBlocked(true);
                }

                map[row][col] = room;
            }
        }


        map[0][0].setBlocked(false);
        if (size > 1) {
            if (map[0][1].isBlocked() && map[1][0].isBlocked()) {
                map[0][1].setBlocked(false);
            }
        }
    }


    private boolean isEdge(int row, int col) {
        return (row == 0 || col == 0 || row == size - 1 || col == size - 1);
    }

    public Room getRoom(int row, int col) {
        if (row >= 0 && col >= 0 && row < size && col < size) {
            return map[row][col];
        }
        return null;
    }

    public int getSize() {
        return size;
    }
}




                /// use this  int roll = Dice.roll(10);