package com.example.wizard309.entities;

import android.graphics.PointF;

import com.example.wizard309.helpers.GameConstants;

import java.util.Random;

/**
 * Objects used to represent the characters in the battle screen
 */
public class BattleCharacter extends Character{

    private final int id;
    private final int maxHP;
    private final int currentHP;
    private final int currentMana;
    private final int maxMana;
    private final int level;


    private final int type;

    private long lastDirectionChange = System.currentTimeMillis();

    private Random rand = new Random();
    private float prevX,prevY;

    private String displayName,element = "";
    /**
     * position of the character in form of pointf
     */
    public PointF position;

    private String character;

    /**
     * init the battleChar this can be an enemy or a player
     * @param pos
     * @param gameCharType
     * @param size
     * @param id
     * @param maxHP
     * @param currentHP
     * @param maxMana
     * @param currentMana
     * @param level
     * @param element
     */
    public BattleCharacter(PointF pos, GameCharacters gameCharType, int size,int id,int maxHP, int currentHP,int maxMana, int currentMana, int level,String element, int type, String displayName, String ext) {
        super(pos, gameCharType, size);
        this.character = ext;
        this.position = pos;
        this.displayName = displayName;
        this.id = id;
        this.maxHP = maxHP;
        this.currentHP = currentHP;
        this.maxMana = maxMana;
        this.currentMana = currentMana;
        this.level = level;
        this.element = element;
        this.type = type;



    }

    /**
     * updates pos and animation of wizard
     * @param delta
     * @param x
     * @param y
     */
    public void update(double delta, float x , float y){
        updateAnimation(3);
        //updateMove(delta,x,y);
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
     * returns the battle character type
     * @return
     */
    public int getType() {
        return type;
    }


    /**
     * returns the current mana point
     * @return
     */
    public int getCurrentMana() {
        return currentMana;
    }

    /**
     * returns the max mana of a character
     * @return
     */
    public int getMaxMana(){
        return maxMana;
    }

    /**
     * returns the character
     * @return
     */
    public String getCharacter() {
        return character;
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
     * @param touchX
     * @param touchY
     * @return true or false
     */
    public boolean isTouched(float touchX, float touchY) {
        boolean xInside =  touchX > this.position.x && touchX < this.position.x+300;
        boolean yInside =  touchY > this.position.y && touchY < this.position.y+500;
        return xInside && yInside;
    }

    /**
     *
     * @return wizard name
     */
    public String getName(){
        return displayName;
    }

    /**
     * returns the id value of a character
     * @return id
     */
    public int getId(){return id;}

    /**
     *
     * @return wizard element
     */
    public String getElement(){
        return element;
    }

    /**
     * returns the current hp
     * @return current hp
     */
    public int getCurrentHP() {
        return currentHP;
    }

    /**
     * returns the max hp
     * @return maxhp
     */
    public int getMaxHP() {
        return maxHP;
    }
}
