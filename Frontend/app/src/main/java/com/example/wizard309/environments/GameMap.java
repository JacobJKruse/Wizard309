package com.example.wizard309.environments;

import android.graphics.Canvas;

import com.example.wizard309.Enviorment;
import com.example.wizard309.entities.Building;
import com.example.wizard309.entities.Buildings;
import com.example.wizard309.entities.Enviorments;
import com.example.wizard309.helpers.GameConstants;

import java.util.ArrayList;

/**
 * Gamemap draws the map of based on the provided tileset
 */
public class GameMap {

    private int[][] spriteID;


    private Floor floor;
    private ArrayList<Building> buildingArrayList;
    private ArrayList<Enviorment> enviormentsArrayList;

    private ArrayList<Doorway> doorwayArrayList;
    private ArrayList<Enviorment> healStatueArrayList;


    /**
     * game map generates the array for the map based off the tileset
     * @param spriteID
     */
    public GameMap(int[][] spriteID, Floor floor, ArrayList<Building> buildingArrayList, ArrayList<Enviorment> enviormentArrayList,ArrayList<Enviorment> healStatueArrayList){
        this.spriteID = spriteID;
        this.floor = floor;
        this.buildingArrayList = buildingArrayList;
        this.doorwayArrayList = new ArrayList<>();
        this.enviormentsArrayList = enviormentArrayList;
        this.healStatueArrayList = healStatueArrayList;
    }

    /**
     * adds the doorway to the doorway array list
     * @param doorway
     */
    public void addDoorway(Doorway doorway){
        this.doorwayArrayList.add(doorway);
    }

    /**
     * gets the buildings arraylist
     * @return
     */
    public ArrayList<Building> getBuildingArrayList() {
        return buildingArrayList;
    }

    /**
     * gets the environment array list
     * @return
     */
    public ArrayList<Enviorment> getEnviormentsArrayList() {return enviormentsArrayList;}

    /**
     * returns the current floor
     * @return
     */
    public Floor getFloor() {
        return floor;
    }

    /**
     * gets the doorway array list
     * @return
     */
    public ArrayList<Doorway> getDoorwayArrayList(){
        return doorwayArrayList;
    }

    /**
     * returns the heal statues array list
     * @return
     */
    public ArrayList<Enviorment> getHealStatueArrayList(){return healStatueArrayList;}
    /**
     * gets height value
     * @return
     */
    public int getArrayHeight(){
        return spriteID.length;
    }

    /**
     * gets the width of the map
     * @return
     */
    public int getArrayWidth(){
        return spriteID[0].length;
    }

    /**
     * gets the tile id
     * @param x
     * @param y
     * @return
     */

    public int getTileID(int x, int y){
        return spriteID[y][x];
    }

    /**
     * draws the map onto a canvas - Out of date method new method created
     * @param c
     */
    public void draw(Canvas c){
        for(int i = 0; i < spriteID.length; i++){
            for(int j = 0; j < spriteID[i].length; j++){
                c.drawBitmap(Floor.GRASSY.getSprite(spriteID[i][j]),i* GameConstants.sprites.SIZE, j*GameConstants.sprites.SIZE,null);
            }
        }

    }
}
