package com.example.wizard309.entities;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * abstract class of all Entities to represent hitboxes
 */
public abstract class Entity {

    protected RectF hitbox;

    //FOR SIZE USE
    // 1 FOR 8x8
    // 2 FOR 16x16
    // 3 for 32x32?????

    /**
     * creates hitbox for entity being created
     * @param pos
     * @param width
     * @param height
     * @param size
     */
    public Entity(PointF pos, float width, float height, int size ){
        switch (size){
            case 1:
                this.hitbox = new RectF(pos.x,pos.y,pos.x+width/2,pos.y + height/2);
                break;
            case 2:
                this.hitbox = new RectF(pos.x,pos.y,pos.x+width,pos.y + height);
                break;
            case 3:
                this.hitbox = new RectF(pos.x,pos.y,pos.x+width*2,pos.y + height*2);
            default:
                this.hitbox = new RectF(pos.x,pos.y,pos.x+width,pos.y + height);
                break;
        }
    }

    /**
     *
     * @return hitbox params
     */
    public RectF getHitbox() {
        return hitbox;
    }
}
