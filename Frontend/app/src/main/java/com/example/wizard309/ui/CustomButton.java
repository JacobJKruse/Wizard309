package com.example.wizard309.ui;

import android.graphics.RectF;

public class CustomButton {

    private final RectF hitbox;
    private boolean clicked;

    /**
     * Creates click box for the custom button
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public CustomButton(float x , float y , float width, float height){
        hitbox = new RectF(x,y,(width)+x,y+(height));

    }

    /**
     * returns hitbox to get the bounds of the button
     * @return
     */
    public RectF getHitbox() {
        return hitbox;
    }

    /**
     *
     * @return if clicked
     */
    public boolean isClicked() {
        return clicked;
    }

    /**
     * sets clicked
     * @param clicked
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}
