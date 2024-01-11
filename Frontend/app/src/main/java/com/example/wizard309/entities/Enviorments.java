package com.example.wizard309.entities;

import static com.example.wizard309.helpers.GameConstants.sprites.DEFAULT_SCALE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.example.wizard309.R;
import com.example.wizard309.helpers.interfaces.BitmapMethods;
import com.example.wizard309.main.MainActivity;

/**
 * Enviorments is used to repersent the bitmap images of the elements in the overworld, shop
 */
public enum Enviorments implements BitmapMethods {

    TREE(48,288,48,32,48,320,48,16,R.drawable.enviorments),
    DEADTREE(64,0,32,16,64,16,32,16,R.drawable.enviorments),
    SUNFLOWER(0,0,0,0,16,176,16,16,R.drawable.enviorments),
    SAPLING(0,0,0,0,80,144,16,16,R.drawable.enviorments),
    CABBAGE(0,0,0,0,64,176,16,16,R.drawable.enviorments),
    MEDROCK(240,224,32,16,240,240,31,16,R.drawable.enviorments),
    CHOPPEDTREE(192,208,16,8,192,216,16,40,R.drawable.enviorments),
    PINKTREE(0,288,48,32,0,320,48,16,R.drawable.enviorments),
    THEROCK(272,224,64,32,272,256,64,16,R.drawable.enviorments),
    BOOKSHELF(0,0,0,0,128,112,32,32,R.drawable.insideitems),
    TABLE(32,160,48,8,32,168,48,8,R.drawable.insideitems),
    RUG(0,0,0,0,208,64,48,48,R.drawable.insideitems),
    HEALSTATUE(48,304,32,16,48,320,32,16,R.drawable.outsidetiles);

    RectF hitbox;

    Bitmap topItem;
    Bitmap bottomItem;
    int topH;
    int botH;


    /**
     * used to construct the forground and background sections of the eveniorments,
     * for the top variable is used to be drawn above the player, the bot is used to be drawn behind the player
     * @param topX
     * @param topY
     * @param topW
     * @param topH
     * @param botX
     * @param botY
     * @param botW
     * @param botH
     * @param id
     */
    Enviorments(int topX, int topY,int topW, int topH, int botX,int botY,int botW,int botH,int id){
        options.inScaled = false;
        this.topH = topH;
        this.botH = botH;
        Bitmap atlas = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), id, options);
        if(topH>0){
            topItem = getScale(Bitmap.createBitmap(atlas,topX,topY,topW,topH));

        }
        if(botH >0){
            bottomItem = getScale(Bitmap.createBitmap(atlas,botX,botY,botW,botH));
        }
        hitbox = new RectF(topX * DEFAULT_SCALE,
                topY * DEFAULT_SCALE, (topW+topX)*DEFAULT_SCALE,
                (topH+botH)*DEFAULT_SCALE);

    }

    /**
     * returns the hitbox of the enviorment item
     * @return
     */
    public RectF getEnvHitbox(){
        return hitbox;
    }

    /**
     * returns the toplayer item
     * @return
     */
    public Bitmap getTopItem(){
        return topItem;
    }

    /**
     * returns the top height of the bitmap
     * @return
     */
    public int getTopH() {
        return topH;
    }

    /**
     * retuns the bottom height of the bitmap
     * @return
     */
    public int getBotH() {
        return botH;
    }

    /**
     * returns the bottom bitmap image
     * @return
     */
    public Bitmap getBottomItem() {
        return bottomItem;
    }
}
