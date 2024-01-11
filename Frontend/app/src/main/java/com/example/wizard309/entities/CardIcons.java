package com.example.wizard309.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.wizard309.R;
import com.example.wizard309.helpers.interfaces.BitmapMethods;
import com.example.wizard309.main.MainActivity;

/**
 * public enum for card asset images
 */
public enum CardIcons implements BitmapMethods {

    cardBackgrounds(R.drawable.cardsbackgrounds,8,1,103,128 ,4,4),
    cardBoarders(R.drawable.cardboarders,3,1,100,128,4,4),
    cardTitles(R.drawable.cardtitles,3,1,96,29,3,3),
    manaBackground(R.drawable.manabackground,1,1,21,18,5,5),
    attackBackground(R.drawable.attackbackground,1,1,21,18,5,5),
    cardIconBoarder(R.drawable.cardiconboarder,3,1,86,72,2.6,3.095);

    private Bitmap spriteSheet;
    private Bitmap[][] cards;//7 rows// 4 columns for a sprite sheet
    private int width,height;

    /**
     * constructor to get the card asset
     * @param ID
     * @param sheetWidth
     * @param sheetHeight
     * @param cardLen
     * @param cardHei
     * @param widthscale
     * @param heightscale
     */
    CardIcons(int ID, int sheetWidth, int sheetHeight, int cardLen, int cardHei , double widthscale, double heightscale){
        width = cardLen;
        height = cardHei;
        cards = new Bitmap[sheetHeight][sheetWidth];
        options.inScaled = false;
        spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(),ID,options);
        for(int i = 0; i < cards.length; i++){ //splits sprite sheet apart into an array of bitmaps
            for(int j = 0; j < cards[i].length; j++){

                cards[i][j] = getCardScale(Bitmap.createBitmap(spriteSheet, cardLen *j, cardHei * i,cardLen, cardHei),widthscale,heightscale);
            }
        }
    }

    /**
     * retuns the sprite sheet of the card asset
     * @return
     */
    public Bitmap getSpriteSheet(){
        return spriteSheet;
    }

    /**
     * retuns the width of the asset
     * @return
     */
    public int getWidth(){
        return width;
    }

    /**
     * returns the height of the card icon
     * @return
     */
    public int getHeight(){
        return height;
    }

    /**
     * returns the bitmap image of the asset
     * @param yPos
     * @param xPos
     * @return
     */
    public Bitmap getSprite(int yPos,int  xPos){
        return cards[yPos][xPos];
    }


}
