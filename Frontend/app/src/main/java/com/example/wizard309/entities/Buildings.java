package com.example.wizard309.entities;

import static com.example.wizard309.helpers.GameConstants.sprites.DEFAULT_SCALE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.example.wizard309.R;
import com.example.wizard309.helpers.GameConstants;
import com.example.wizard309.helpers.interfaces.BitmapMethods;
import com.example.wizard309.main.MainActivity;

/**
 * buildings enum for each type of building
 */
public enum Buildings implements BitmapMethods {


    SHOP(0, 0, 64, 48,32,32,16,14),
    CAVE(109,48,48,42,17,25,15,14),
    FARM(416,0,48,48,17,32,16,15);

    Bitmap houseImg;
    RectF hitboxDoorway;

    /**
     * Buildings constructor for each type of building
     * sets the x,y based off the tile set image, and snips out the image and
     * constructs a bitmap image. This also sets up the doorway hitbox.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param doorwayX
     * @param doorwayY
     * @param doorwayWidth
     * @param doorwayHeight
     */
    Buildings(int x, int y, int width, int height,int doorwayX,int doorwayY,int doorwayWidth, int doorwayHeight) {
        options.inScaled = false;

        Bitmap atlas = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), R.drawable.buildings_atlas, options);
        houseImg = getScale(Bitmap.createBitmap(atlas,x,y,width,height));

        hitboxDoorway = new RectF(doorwayX * DEFAULT_SCALE,
                doorwayY * DEFAULT_SCALE, (doorwayWidth+doorwayX)*DEFAULT_SCALE,
                (doorwayY+doorwayHeight)*DEFAULT_SCALE);
    }

    /**
     * returns the hitbox of the doorway
     * @return
     */
    public RectF getHitboxDoorway(){
        return hitboxDoorway;
    }

    /**
     * returns the image of the house
     * @return
     */
    public Bitmap getHouseImg(){
        return houseImg;
    }
}
