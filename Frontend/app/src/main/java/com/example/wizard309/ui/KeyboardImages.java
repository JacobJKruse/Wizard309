package com.example.wizard309.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.wizard309.R;
import com.example.wizard309.helpers.interfaces.BitmapMethods;
import com.example.wizard309.main.MainActivity;

public enum KeyboardImages implements BitmapMethods {


    KeyA (R.drawable.a_key,96,96 ),
    KeyB (R.drawable.b_key,96,96 ),
    KeyC (R.drawable.c_key,96,96 ),
    KeyD (R.drawable.d_key,96,96 ),
    KeyE (R.drawable.e_key,96,96 ),
    KeyF (R.drawable.f_key,96,96 ),
    KeyG (R.drawable.g_key,96,96 ),
    KeyH (R.drawable.h_key,96,96 ),
    KeyI (R.drawable.i_key,96,96 ),
    KeyJ (R.drawable.j_key,96,96 ),
    KeyK (R.drawable.k_key,96,96 ),
    KeyL (R.drawable.l_key,96,96 ),
    KeyM (R.drawable.m_key,96,96 ),
    KeyN (R.drawable.n_key,96,96 ),
    KeyO (R.drawable.o_key,96,96 ),
    KeyP (R.drawable.p_key,96,96 ),
    KeyQ (R.drawable.q_key,96,96 ),
    KeyR (R.drawable.r_key,96,96 ),
    KeyS (R.drawable.s_key,96,96 ),
    KeyT (R.drawable.t_key,96,96 ),
    KeyU (R.drawable.u_key,96,96 ),
    KeyV (R.drawable.v_key,96,96 ),
    KeyW (R.drawable.w_key,96,96 ),
    KeyX (R.drawable.x_key,96,96 ),
    KeyY (R.drawable.y_key,96,96 ),
    KeyZ (R.drawable.z_key,96,96 );





    private Bitmap unclicked,clicked;
    private int width,height;
    KeyboardImages(int ID, int width, int height){
        options.inScaled = false;
        this.width = width;
        this.height = height;

        Bitmap bitmapIndex = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), ID,options);
        unclicked = Bitmap.createBitmap(bitmapIndex,0,0,width,height);
        clicked = Bitmap.createBitmap(bitmapIndex,width,0,width,height);


    }

    /**
     * returns key width
     * @return
     */
    public int getWidth(){
        return width;
    }

    /**
     * returns key height
     * @return
     */
    public int getHeight(){
        return height;
    }

    /**
     * returns image of clicked or unclicked key depends on isClicked
     * @param isClicked
     * @return
     */
    public Bitmap getButtonImage (boolean isClicked){
        return isClicked ? clicked:unclicked;
    }
}
