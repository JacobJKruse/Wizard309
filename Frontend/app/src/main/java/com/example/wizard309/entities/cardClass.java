package com.example.wizard309.entities;

/**
 * public enum for all card classes
 */
public enum cardClass {

    GRASS(3),
    FIRE(1),
    WATER(0),

    ROCK(2),

    ELECTRIC(4),
    DEATH(5),
    LIFE(6),
    ICE(7);
    private final int value;

    /**
     * constructor of the card type
     * @param value
     */
     cardClass(int value) {
        this.value = value;
    }

    /**
     * returns the value of the card enum
     * @return
     */
    public int getValue() {
        return value;
    }
}
