package com.example.wizard309.entities;

import android.graphics.Canvas;
import android.graphics.PointF;

import java.util.ArrayList;

/**
 * deprecated building manager class
 */
public class BuildingManager {

    private ArrayList<Building> buildingArrayList;
    private float cameraX, cameraY;

    /**
     * deprecated building manager
     */
    public BuildingManager(){

    }

    /**
     * deprecated
     * @param cameraX
     * @param cameraY
     */
    public void setCameraPos(float cameraX, float cameraY){
        this.cameraX = cameraX;
        this.cameraY = cameraY;
    }

    /**
     * deprecated
     * @param c
     */
    public void draw(Canvas c){
        for(Building b : buildingArrayList){
            c.drawBitmap(b.getBuildingType().houseImg, b.getPos().x+cameraX,b.getPos().y+cameraY,null);
        }
    }
}
