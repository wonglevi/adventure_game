package com.example.demo;

import java.util.Random;

public class Dice {
    private static final Random rand = new Random();

    public static int roll(int sides) {
        return rand.nextInt(sides) + 1;
    }

    public static int rollSum(int times, int sides){
        int sum = 0;
        for(int i = 0; i < times; i++){
            sum += roll(sides);
        }
        return sum;
        }
    }

