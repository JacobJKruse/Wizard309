package com.example.wizard309.entities;

import android.graphics.PointF;

import com.example.wizard309.helpers.GameConstants;

import java.util.Random;

/**
 * extends the Character class, used to generate enemies on the overworld
 */
public class Enemy extends Character{
    private float prevX,prevY;
    private int rooomID;
    /**
     * position of enemy
     */
    public PointF position;
    /**
     * element string
     */
    public String element;

    /**
     * int of the id of a enemy
     */
    public int id;

    /**
     * inits enemy with character supers
     * @param pos
     * @param gameCharType
     * @param size
     * @param id
     */
    public Enemy(PointF pos, GameCharacters gameCharType, int size,int id,int roomID) {
        super(pos, gameCharType, size);
        prevX = pos.x;
        prevY = pos.y;
        position = pos;
        this.id = id;
        this.rooomID = roomID;
    }

    /**
     * updates annimation and movement of enemy
     * @param delta
     * @param x
     * @param y
     */
    public void update(double delta, float x , float y){
        updateAnimation(3);
        updateMove(delta,x,y);
    }

    /**
     * returns the room id of the enemy, it can be in the overworld, shop, or cave
     * @return
     */
    public int getRooomID() {
        return rooomID;
    }

    /**
     * handles face direction of enemy for animation purposes
     * @param delta
     * @param x
     * @param y
     */
    private void updateMove(double delta,float x, float y) {


        float deltaX = prevX-x;
        float deltaY = prevY-y;

        hitbox.left -=  deltaX;
        hitbox.top -=  deltaY;
        hitbox.right +=  -deltaX;
        hitbox.bottom +=  -deltaY;

        if(deltaX>0){

            faceDir = GameConstants.Face_Dir.LEFT;
        }
        else if (deltaX<0){

            faceDir = GameConstants.Face_Dir.RIGHT;
        }

        prevX = x;
        prevY = y;

    }

    /**
     * sets pos to param position
     * @param position
     */
    public void setPosition(PointF position) {

        this.position = position;
    }
}
