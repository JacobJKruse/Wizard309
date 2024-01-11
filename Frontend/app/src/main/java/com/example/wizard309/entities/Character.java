package com.example.wizard309.entities;

import android.graphics.PointF;

import com.example.wizard309.helpers.GameConstants;

/**
 * abstract class used to create different types of characters from enemies to players, and other players
 */
public abstract class Character extends Entity{

    protected int aniTick, aniIndex;
    protected int faceDir = GameConstants.Face_Dir.RIGHT;
    protected final GameCharacters gameCharType;

    /**
     * inits character using entity supers
     * @param pos
     * @param gameCharType
     * @param size
     */
    public Character(PointF pos, GameCharacters gameCharType,int size) {
        super(pos,GameConstants.sprites.SIZE,GameConstants.sprites.SIZE,size);
        this.gameCharType = gameCharType;
    }

    protected void updateAnimation(int maxIndex){
        aniTick ++;
        if(aniTick >= GameConstants.animation.SPEED){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= maxIndex){
                aniIndex = 0;
            }
        }

    }

    /**
     * resets animation index
     */
    public void resetAnimation(){
        aniTick = 0;
        aniIndex = 0;
    }

    /**
     *
     * @return animation index
     */
    public int getAniIndex() {
        return aniIndex;
    }

    /**
     *
     * @return faceDir
     */
    public int getFaceDir() {
        return faceDir;
    }

    /**
     * sets faceDir to dir
     * @param dir
     */
    public void setFaceDir(int dir){
        this.faceDir = dir;
    }

    /**
     *
     * @return charType
     */
    public GameCharacters getGameCharType() {
        return gameCharType;
    }
}
