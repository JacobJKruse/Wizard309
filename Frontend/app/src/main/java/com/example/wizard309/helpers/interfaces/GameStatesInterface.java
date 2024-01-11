package com.example.wizard309.helpers.interfaces;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

/**
 * Game state interface for all different game states
 */
public interface GameStatesInterface  {

    /**
     * updates on each frame
     * @param delta
     */
    void update(double delta);

    /**
     * updates the canvas each frome
     * @param c
     */
    void render(Canvas c);

    /**
     * gets input for touch events
     * @param event
     */
    void touchEvents(MotionEvent event);

}
