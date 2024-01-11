package com.example.wizard309.helpers.interfaces;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.wizard309.helpers.GameConstants;

/**
 * interface for bitmap images
 */
public interface BitmapMethods {

    BitmapFactory.Options options = new BitmapFactory.Options();

    /**
     * returns the scaled image of a bitmap
     * @param bitmap
     * @return
     */
    default Bitmap getScale(Bitmap bitmap){
        return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * GameConstants.sprites.DEFAULT_SCALE, bitmap.getHeight() * GameConstants.sprites.DEFAULT_SCALE, false);
    }

    /**
     * retuns the scaled version of a card image bitmap image
     * @param bitmap
     * @param widthscale
     * @param heightscale
     * @return
     */
    default Bitmap getCardScale(Bitmap bitmap, double widthscale, double heightscale){
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*widthscale ), (int) (bitmap.getHeight()*heightscale), false);
    }

}
