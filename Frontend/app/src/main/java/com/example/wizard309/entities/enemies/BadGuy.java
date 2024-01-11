package com.example.wizard309.entities.enemies;

import android.graphics.PointF;

import com.example.wizard309.entities.Character;
import com.example.wizard309.entities.GameCharacters;
import com.example.wizard309.helpers.GameConstants;

import java.util.Random;

/**
 * Front end bag guy POC
 */
public class BadGuy extends Character {
    private long lastDirectionChange = System.currentTimeMillis();
    private Random rand = new Random();

    /**
     * constructor for bad guy object inset the starting position
     * @param pos
     */
    public BadGuy(PointF pos) {
        super(pos, GameCharacters.BADGUY,1);
    }

    /**
     * update position of bad guy with the deltatime and the max x and y
     * @param delta
     * @param maxX
     * @param maxY
     */
    public void update(double delta, int maxX,int maxY){
        updateAnimation(3);
        updateMove(delta,maxX,maxY);
    }

    private void updateMove(double delta, int maxX,int maxY) {

        int time = rand.nextInt(3000);
        if(System.currentTimeMillis() - lastDirectionChange >= time){
            faceDir = rand.nextInt(4);
            lastDirectionChange = System.currentTimeMillis();
        }
        switch (faceDir){
            case GameConstants.Face_Dir.LEFT:
                hitbox.left -= delta * 300;
                hitbox.right -= delta * 300;

                if(hitbox.left <= 0){
                    faceDir = GameConstants.Face_Dir.RIGHT;
                }
                break;
            case GameConstants.Face_Dir.RIGHT:
                hitbox.left += delta * 300;
                hitbox.right += delta * 300;

                if(hitbox.left >= maxX){
                    faceDir = GameConstants.Face_Dir.LEFT;
                }
                break;
            case GameConstants.Face_Dir.UP:
                hitbox.top -= delta * 300;
                hitbox.bottom -= delta * 300;

                if(hitbox.top <= 0){
                    faceDir = GameConstants.Face_Dir.DOWN;
                }
                break;
            case GameConstants.Face_Dir.DOWN:
                hitbox.top += delta * 300;
                hitbox.bottom += delta * 300;
                if(hitbox.top >= maxY){
                    faceDir = GameConstants.Face_Dir.UP;
                }
                break;
        }

    }

}
