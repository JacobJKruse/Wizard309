package com.example.wizard309.entities;

import android.graphics.PointF;

/**
 * bitmap objects the represent the buildings
 */
public class Building {

    private PointF pos;
    private Buildings buildingType;

    /**
     * constructor of building, takes in the top left point and the building type enum
     * @param pos
     * @param buildingType
     */
    public Building(PointF pos, Buildings buildingType){
        this.pos = pos;
        this.buildingType = buildingType;

    }

    /**
     * returns the buildings enum
     * @return
     */
    public Buildings getBuildingType() {
        return buildingType;
    }

    /**
     * returns the pointf pos
     * @return PointF position of the building
     */
    public PointF getPos() {
        return pos;
    }
}
