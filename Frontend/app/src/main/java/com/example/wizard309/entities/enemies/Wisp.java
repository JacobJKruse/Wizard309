package com.example.wizard309.entities.enemies;

import android.graphics.PointF;

import com.example.wizard309.entities.Character;
import com.example.wizard309.entities.GameCharacters;
import com.example.wizard309.helpers.GameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wisp extends Character {
    private long lastDirectionChange = System.currentTimeMillis();
    private Random rand = new Random();

    private List<Integer> xPath,yPath;
    Boolean goingForward = true;
    private int currentIndex = 0;

    /**
     * Wisp inset list of points for the paths and the starting pos
     * @param pos
     * @param xPath
     * @param yPath
     */
    public Wisp(PointF pos, List<Integer> xPath,List<Integer> yPath) {
        super(pos,GameCharacters.WISP,1);
        this.xPath = xPath;
        this.yPath = yPath;
    }

    /**
     * update method gets called every frame
     * @param delta
     * @param maxX
     * @param maxY
     */
    public void update(double delta, int maxX,int maxY){
        updateAnimation(3);
        updateMove(delta,maxX,maxY);
    }

    private void updateMove(double delta, int maxX,int maxY) {
        if(Math.abs(hitbox.left - xPath.get(currentIndex)) > 25 || Math.abs(hitbox.top - yPath.get(currentIndex)) > 25){
            //FOR X MOVEMENT
            if(Math.abs(hitbox.left - xPath.get(currentIndex)) > 25) {
                if (hitbox.left < xPath.get(currentIndex)) {
                    faceDir = GameConstants.Face_Dir.RIGHT;
                    hitbox.left += delta * 300;
                    hitbox.right += delta * 300;
                } else {
                    faceDir = GameConstants.Face_Dir.LEFT;
                    hitbox.left -= delta * 300;
                    hitbox.right -= delta * 300;
                }
            }

            //FOR Y MOVEMENT
            if(Math.abs(hitbox.top - yPath.get(currentIndex)) > 25) {
                if (hitbox.top < yPath.get(currentIndex)) {
                    faceDir = GameConstants.Face_Dir.DOWN;
                    hitbox.top += delta * 300;
                    hitbox.bottom += delta * 300;
                } else {
                    faceDir = GameConstants.Face_Dir.UP;
                    hitbox.top -= delta * 300;
                    hitbox.bottom -= delta * 300;
                }
            }
        }else{
            if(goingForward){
                if(currentIndex < xPath.size() - 1){
                    currentIndex++;
                }else{
                    goingForward = false;
                    currentIndex--;
                }
            }else{
                if(currentIndex > 0){
                    currentIndex--;
                }else{
                    goingForward = true;
                }
            }

          //  System.out.println("CURRENT INDEX:" + currentIndex);
        }

    }

}
