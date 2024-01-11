package com.example.wizard309.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.wizard309.main.MainActivity;
import com.example.wizard309.R;
import com.example.wizard309.helpers.GameConstants;
import com.example.wizard309.helpers.interfaces.BitmapMethods;

/**
 * Bitmap images for the different player characters and enemies
 */
public enum GameCharacters implements BitmapMethods {

    //need to make new player for each character sprite so six total now
    PLAYERDEATH(R.drawable.adeptnecromancer,4,2),
    PLAYERLIFE(R.drawable.life_wizard,4,2),
    PLAYERSTORM(R.drawable.storm_wizard,4,2),
    PLAYERFIRE(R.drawable.fire_wizard,4,2),
    PLAYERICE(R.drawable.ice_wizard,4,2),
    PLAYEREARTH(R.drawable.earth_wizard,4,2),
    BADGUY(R.drawable.test_guy,3,4),
    WISP(R.drawable.wisp,4,4),
    OVERWORLDMIMIC(R.drawable.sprite_mimic,4,1),
    MIMIC(R.drawable.sprite_mimicbattle,4,1),
    WITCH(R.drawable.vilewitch,4,2),
    WARLOCK(R.drawable.grandmasterwizard,9,2,32);

    private Bitmap spriteSheet;
    private Bitmap[][] sprites;//7 rows 4 columns for a sprite sheet


    /**
     * bitmap constructor for the game characters
     * @param ID
     * @param sheetWidth
     * @param sheetHeight
     */
    GameCharacters(int ID, int sheetWidth,int sheetHeight){
        sprites = new Bitmap[sheetHeight][sheetWidth];
        options.inScaled = false;
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(),ID,options);
        for(int i = 0; i < sprites.length; i++){ //splits sprite sheet apart into an array of bitmaps
            for(int j = 0; j < sprites[i].length; j++){
                sprites[i][j] = getScale(Bitmap.createBitmap(spriteSheet, GameConstants.sprites.DEFAULT_SIZE *j, GameConstants.sprites.DEFAULT_SIZE * i, GameConstants.sprites.DEFAULT_SIZE, GameConstants.sprites.DEFAULT_SIZE));
            }
        }
    }

    /**
     * scaled version of a game characters
     * @param ID
     * @param sheetWidth
     * @param sheetHeight
     * @param size
     */
    GameCharacters(int ID, int sheetWidth,int sheetHeight,int size){
        sprites = new Bitmap[sheetHeight][sheetWidth];
        options.inScaled = false;
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(),ID,options);
        for(int i = 0; i < sprites.length; i++){ //splits sprite sheet apart into an array of bitmaps
            for(int j = 0; j < sprites[i].length; j++){
                sprites[i][j] = getScale(Bitmap.createBitmap(spriteSheet, size *j, size * i, size, size));
            }
        }
    }

    /**
     *
     * @return whole spritesheet
     */
    public Bitmap getSpriteSheet(){
        return spriteSheet;
    }

    /**
     *
     * @param yPos
     * @param xPos
     * @return sprite at xpos and ypos
     */
    public Bitmap getSprite(int yPos,int  xPos){
        return sprites[yPos][xPos];
    }


}
