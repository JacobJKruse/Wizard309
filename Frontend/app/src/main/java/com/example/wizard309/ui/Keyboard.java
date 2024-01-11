package com.example.wizard309.ui;


import android.content.Context;
import android.content.Context;
import android.graphics.Canvas;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsetsController;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.wizard309.R;
import com.example.wizard309.main.MainActivity;

import java.util.List;

public class Keyboard  {

    /**
     * inits the keyboard
     * @param keyboard
     * @return
     */
    public List<CustomButton> initKeyboard(List<CustomButton> keyboard) {
        for(int i = 0; i<= 26; i++) {
            keyboard.add(new CustomButton(MainActivity.screenWidth/7 + 64 * i, MainActivity.screenHeight - 240 + (88 * (i % 3)), KeyboardImages.KeyA.getWidth(), KeyboardImages.KeyA.getHeight()));
        }
        return keyboard;
    }

    /**
     * takes a canvas from the current state and renders the keyboard
     * @param c
     * @param keyboard
     */
    public void drawKeyboard(Canvas c, List<CustomButton> keyboard){
        c.drawBitmap(
                KeyboardImages.KeyA.getButtonImage(keyboard.get(0).isClicked()),
                keyboard.get(0).getHitbox().left,
                keyboard.get(0).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyB.getButtonImage(keyboard.get(1).isClicked()),
                keyboard.get(1).getHitbox().left,
                keyboard.get(1).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyC.getButtonImage(keyboard.get(2).isClicked()),
                keyboard.get(2).getHitbox().left,
                keyboard.get(2).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyD.getButtonImage(keyboard.get(3).isClicked()),
                keyboard.get(3).getHitbox().left,
                keyboard.get(3).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyE.getButtonImage(keyboard.get(4).isClicked()),
                keyboard.get(4).getHitbox().left,
                keyboard.get(4).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyF.getButtonImage(keyboard.get(5).isClicked()),
                keyboard.get(5).getHitbox().left,
                keyboard.get(5).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyG.getButtonImage(keyboard.get(6).isClicked()),
                keyboard.get(6).getHitbox().left,
                keyboard.get(6).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyH.getButtonImage(keyboard.get(7).isClicked()),
                keyboard.get(7).getHitbox().left,
                keyboard.get(7).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyI.getButtonImage(keyboard.get(8).isClicked()),
                keyboard.get(8).getHitbox().left,
                keyboard.get(8).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyJ.getButtonImage(keyboard.get(9).isClicked()),
                keyboard.get(9).getHitbox().left,
                keyboard.get(9).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyK.getButtonImage(keyboard.get(10).isClicked()),
                keyboard.get(10).getHitbox().left,
                keyboard.get(10).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyL.getButtonImage(keyboard.get(11).isClicked()),
                keyboard.get(11).getHitbox().left,
                keyboard.get(11).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyM.getButtonImage(keyboard.get(12).isClicked()),
                keyboard.get(12).getHitbox().left,
                keyboard.get(12).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyN.getButtonImage(keyboard.get(13).isClicked()),
                keyboard.get(13).getHitbox().left,
                keyboard.get(13).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyO.getButtonImage(keyboard.get(14).isClicked()),
                keyboard.get(14).getHitbox().left,
                keyboard.get(14).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyP.getButtonImage(keyboard.get(15).isClicked()),
                keyboard.get(15).getHitbox().left,
                keyboard.get(15).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyQ.getButtonImage(keyboard.get(16).isClicked()),
                keyboard.get(16).getHitbox().left,
                keyboard.get(16).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyR.getButtonImage(keyboard.get(17).isClicked()),
                keyboard.get(17).getHitbox().left,
                keyboard.get(17).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyS.getButtonImage(keyboard.get(18).isClicked()),
                keyboard.get(18).getHitbox().left,
                keyboard.get(18).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyT.getButtonImage(keyboard.get(19).isClicked()),
                keyboard.get(19).getHitbox().left,
                keyboard.get(19).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyU.getButtonImage(keyboard.get(20).isClicked()),
                keyboard.get(20).getHitbox().left,
                keyboard.get(20).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyV.getButtonImage(keyboard.get(21).isClicked()),
                keyboard.get(21).getHitbox().left,
                keyboard.get(21).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyW.getButtonImage(keyboard.get(22).isClicked()),
                keyboard.get(22).getHitbox().left,
                keyboard.get(22).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyX.getButtonImage(keyboard.get(23).isClicked()),
                keyboard.get(23).getHitbox().left,
                keyboard.get(23).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyY.getButtonImage(keyboard.get(24).isClicked()),
                keyboard.get(24).getHitbox().left,
                keyboard.get(24).getHitbox().top,
                null
        );
        c.drawBitmap(
                KeyboardImages.KeyZ.getButtonImage(keyboard.get(25).isClicked()),
                keyboard.get(25).getHitbox().left,
                keyboard.get(25).getHitbox().top,
                null
        );


    }

}
