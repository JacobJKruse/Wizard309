package com.example.wizard309.environments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.wizard309.main.MainActivity;
import com.example.wizard309.R;
import com.example.wizard309.helpers.interfaces.BitmapMethods;

/**
 * Floor tiles bitmap enum
 */
public enum Floor implements BitmapMethods {

    /**
     * Grassy over-world this is the main tileset
     */
    GRASSY(R.drawable.tileset_grassy,13,10),
    INTERIORFLOOR(R.drawable.floor_inside,22,22);


    private Bitmap[] sprites;

    /**
     * Floor generates a bitmap array of tiles from the tile set, this is not the map.
     * @param ID
     * @param tilesWidth
     * @param tilesHeight
     */
    Floor(int ID, int tilesWidth, int tilesHeight){
        options.inScaled = false;
        sprites = new Bitmap[tilesHeight*tilesWidth];
        Bitmap spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(),ID,options);
        for(int i = 0; i < tilesHeight; i++){ //splits sprite sheet apart into an array of bitmaps
            for(int j = 0; j < tilesWidth; j++){
                int index = i * tilesWidth + j;
                sprites[index] = getScale(Bitmap.createBitmap(spriteSheet, 16*j, 16 * i, 16, 16));

            }
        }

    }

    /**
     * returns the tile of the tileset
     * @param ID
     * @return
     */
    public Bitmap getSprite(int ID){
        return sprites[ID];
    }

}
