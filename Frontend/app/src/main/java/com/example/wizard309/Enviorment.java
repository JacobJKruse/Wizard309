package com.example.wizard309;

import android.graphics.PointF;

import com.example.wizard309.entities.Enviorments;

/**
 * environment object that represents the environmental object
 */
public class Enviorment {

    private PointF pos;
    private Enviorments enviorments;

    /**
     * environment object constructor that takes in the position and the environments enum
     * @param pos
     * @param enviorments
     */
    public Enviorment(PointF pos, Enviorments enviorments){
        this.pos = pos;
        this.enviorments = enviorments;


    }

    /**
     * gets the top height int
     * @return
     */
    public int getTopH(){
        return enviorments.getTopH();
    }

    /**
     * gets the bottom height int
     * @return
     */
    public int getBotH(){
        return enviorments.getBotH();
    }

    /**
     * returns the enviorments enum
     * @return
     */
    public Enviorments getEnviormentsType() {
        return enviorments;
    }

    /**
     * returns the position of the environments object as a pointF
     * @return
     */
    public PointF getPos() {
        return pos;
    }
}