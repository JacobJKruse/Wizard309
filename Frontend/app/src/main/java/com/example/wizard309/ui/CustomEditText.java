package com.example.wizard309.ui;

import android.graphics.RectF;
import android.view.View;
import android.widget.EditText;

import com.example.wizard309.main.MainActivity;

public class CustomEditText {
    private final RectF hitbox;
    private boolean clicked;
    private String input;
    private EditText editText;

    /**
     * creates box for text edit
     * @param x
     * @param y
     * @param width
     * @param height
     * @param defStr
     */
    public CustomEditText(float x , float y , float width, float height, String defStr){
        hitbox = new RectF(x,y,width+x,height+y);
        editText = new EditText(MainActivity.getGameContext());


    }

    /**
     * gets current input
     * @return
     */
    public String getInput(){
        return input;
    }

    /**
     * sets input
     * @param input
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * gets bounds for edit text
     * @return
     */
    public RectF getHitbox() {
        return hitbox;
    }

    /**
     *
     * @return if cli
     */
    public boolean isClicked() {
        return clicked;
    }

    /**
     * gets current text
     * @return
     */
    public String getText(){
        return editText.getText().toString();
    }

    /**
     * sets edit text text
     * @param str
     */
    public void setEditText(String str){
        editText.setText(str);
    }

    /**
     * set if clicked or not
     * @param clicked
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}
