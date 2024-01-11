package com.example.wizard309.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;



import java.util.Random;

/**
 * game panel that extends the surfaceview and suface holder
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

   private SurfaceHolder holder;

   private Random rand = new Random();
//   private GameLoop gameLoop;
  // private TouchEvents touchEvents;

   private int screenWidth, screenHeight;
   private Game game;


    /**
     * Creates pannel for game to be rendered on
     * @param context
     * @param height
     * @param width
     */
    public GamePanel(Context context, int height, int width) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        game = new Game(holder);

        screenHeight = height;
        screenWidth = width;

//        gameLoop = new GameLoop(this);


    }


    /**
     * handles touch event
     * @param event The motion event.
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //handle all of the many touch events in here now
        return game.touchEvent(event);
    }

    /**
     * starts game loop
     * @param surfaceHolder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        game.startGameLoop();

    }

    /**
     *
     * @param surfaceHolder The SurfaceHolder whose surface has changed.
     * @param i The new {@link android.graphics.PixelFormat} of the surface.
     * @param i1 The new width of the surface.
     * @param i2 The new height of the surface.
     */
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    /**
     *
     * @param surfaceHolder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }


}
