package com.example.wizard309.entities;

import android.graphics.PointF;

import com.example.wizard309.main.MainActivity;
import com.example.wizard309.helpers.GameConstants;

/**
 * implements the character class, used for the main player
 */
public class Player extends Character{

    private double x,y;

    /**
     * inits player character using character super
     * @param character
     */
    public Player(GameCharacters character) { //some wack math in here to center the player lol but it works so dont touch it
        super(new PointF(MainActivity.screenWidth/2 - (16*GameConstants.sprites.DEFAULT_SIZE)/2,MainActivity.screenHeight/2-(16*GameConstants.sprites.DEFAULT_SIZE)/4), character,2);
    }

    /**
     * updates player aniation every frame
     * @param delta
     * @param movePlayer
     */
    public void update(double delta,boolean movePlayer){
        if(movePlayer){
            updateAnimation(4);
        }

    }

    /**
     * sets x to param x
     * @param x
     */
    public void setX(double x){
        this.x = x;
    }

    /**
     * sets y to param y
     * @param y
     */
    public void setY(double y){
        this.y = y;
    }
}
