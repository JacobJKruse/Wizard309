package com.example.wizard309.entities;

import android.graphics.PointF;
import android.view.animation.ScaleAnimation;

import com.example.wizard309.entities.Character;
import com.example.wizard309.entities.GameCharacters;
import com.example.wizard309.helpers.GameConstants;
import com.example.wizard309.main.Game;

import java.util.Random;

/**
 * Overworld wizards for all other player characters
 */
public class Wizard extends Character {

    private int roomID;
    private long lastDirectionChange = System.currentTimeMillis();

    private Random rand = new Random();
    private float prevX,prevY;

    private String displayName,element = "";
    public PointF position;
    private int mana;
    private GameCharacters character;

    /**
     * inits wizard character using character super
     * @param pos
     * @param gameCharacter
     * @param size
     * @param name
     * @param element
     * @param roomID
     */
    public Wizard(PointF pos , GameCharacters gameCharacter , int size,String name,String element, int roomID) {
        super(pos, gameCharacter, size);
        prevX = pos.x;
        prevY = pos.y;
        position = pos;
        displayName = name;
        character = gameCharacter;
        this.element = element;
        this.roomID = roomID;
    }

    /**
     * updates pos and animation of wizard
     * @param delta
     * @param x
     * @param y
     */
    public void update(double delta, float x , float y){
        updateAnimation(3);
        updateMove(delta,x,y);
    }
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


//        hitbox.left = position.x;
//        hitbox.top = position.y;
        prevX = x;
        prevY = y;

    }

    /**
     * sets wizard postion
     * @param position
     */
    public void setPosition(PointF position) {

        this.position = position;
    }

    /**
     *
     * @return wizard name
     */
    public String getName(){
        return displayName;
    }

    /**
     * retuns the room id of the wizard
     * @return
     */
    public int getRoomID(){
        return roomID;
    }

    /**
     * sets the room id of the wizard
     * @param roomnum
     */
    public void setRoomID(int roomnum){
        this.roomID = roomnum;
    }

    /**
     *
     * @return wizard element
     */
    public String getElement(){
        return element;
    }

    /**
     * returns the game character asset
     * @return
     */
    public GameCharacters getCharacter(){
        return character;
    }
}


