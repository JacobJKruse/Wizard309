package com.example.wizard309.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.example.wizard309.R;
import com.example.wizard309.helpers.interfaces.BitmapMethods;
import com.example.wizard309.main.MainActivity;

/**
 * enum for all button images
 */
public enum ButtonImages implements BitmapMethods {


    MAINMENU_START(R.drawable.sprite_startbutton,300,140,1),

    KEYA(R.drawable.a_key,96,96,1),
    TEXTBOX_START(R.drawable.sprite_textbox,300,200,1),
    MENUBUTTON(R.drawable.inv_button,31,32,10),
    SETTINGSBUTTON(R.drawable.settings_button,21,21,10),
    CLOSEMENUBUTTON(R.drawable.closemenubutton,21,21,MainActivity.screenWidth/300),
    MENUDECKBUTTON(R.drawable.menu_deck_button,31,15,MainActivity.screenWidth/150),
    MODBUTTON(R.drawable.mod_buttom,31,15,MainActivity.screenWidth/225),
    ADMINBUTTON(R.drawable.admin_button,31,15,MainActivity.screenWidth/225),
    SHOPBUTTON(R.drawable.shop_button,31,15,MainActivity.screenWidth/225),
    DECKRIGHTARROW(R.drawable.deck_right_arrow,17,17,MainActivity.screenWidth/225),
    DECKLEFTARROW(R.drawable.deck_left_arrow,17,17,MainActivity.screenWidth/225),
    UPARROW(R.drawable.up_arrow,17,17,MainActivity.screenWidth/225),
    RUNBUTTON(R.drawable.run_button,21,21,10),
    ARROWBUTTON(R.drawable.arrow,31,16,10),
    SKIPBUTTON(R.drawable.skip_button,31,15,MainActivity.screenWidth/225);




    private Bitmap unclicked,clicked,staticImage;
    private int width,height;

    /**
     * constructor for the button images
     * @param ID
     * @param width
     * @param height
     * @param scale
     */
    ButtonImages(int ID, int width, int height,int scale){
        options.inScaled = false;
        this.width = width * scale;
        this.height = height * scale;

        Bitmap bitmapIndex = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), ID,options);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bitmapIndex, this.width*2, this.height, false);
        unclicked = Bitmap.createBitmap(resizedBitmap,0,0,this.width,this.height);
        clicked = Bitmap.createBitmap(resizedBitmap,this.width,0,this.width,this.height);
        staticImage = Bitmap.createBitmap(resizedBitmap,0,0,this.width,this.height);

    }

    /**
     *
     * @return width of button
     */
    public int getWidth(){
        return width;
    }

    /**
     *
     * @return height of button
     */
    public int getHeight(){
        return height;
    }

    /**
     *
     * @return whole button image
     */
    public Bitmap getButtonImage (){
        return staticImage;
    }

    /**
     *
     * @param isClicked
     * @return clicked or unclicked button image depending on param
     */
    public Bitmap getButtonImage (boolean isClicked){
        return isClicked ? clicked:unclicked;
    }
}
