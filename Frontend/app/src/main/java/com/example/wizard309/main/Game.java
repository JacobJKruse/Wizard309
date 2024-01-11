package com.example.wizard309.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.example.wizard309.gamestates.Battle;
import com.example.wizard309.gamestates.MainMenu;
import com.example.wizard309.gamestates.Playing;

import org.json.JSONException;

/**
 * Game object , this controls the game loop and the gamestate
 */
public class Game {

    private SurfaceHolder holder;
    private MainMenu mainMenu;
    private GameState currentState = GameState.PLAYING;
    private Playing playing;

    private Battle battle;
    private GameLoop gameLoop;

    /**
     * Creates the game object that handles things such as the game loop
     * @param surfaceHolder
     */
    public Game(SurfaceHolder surfaceHolder) {
        this.holder = surfaceHolder;
        initGameStates();
        gameLoop = new GameLoop(this);
    }

    /**
     * Takes delta (time between frames) and passes it to the current state.
     * @param delta
     */
    public void update(double delta){
        switch(currentState){
            case MAINMENU:
                mainMenu.update(delta);
                break;
            case PLAYING:
                playing.update(delta);
                break;
            case BATTLE:
                battle.update(delta);
                break;
        }
    }

    /**
     * Gets the canvas to draw all the states on and passes it
     * to the current state.
     */
    public void render(){

        Canvas c = holder.lockCanvas();
        c.drawColor(Color.DKGRAY);

        switch(currentState){
            case MAINMENU:
                mainMenu.render(c);
                break;
            case PLAYING:
                playing.render(c);
                break;
            case BATTLE:
                battle.render(c);
                break;
        }
        //draw game

        holder.unlockCanvasAndPost(c);

    }


    private void initGameStates() {
        mainMenu = new MainMenu(this);
        playing = new Playing(this);

    }

    /**
     * creates new battle
     */
    public void initBattleState(){
        battle = new Battle(this);
    }

    /**
     * closes the current battle
     */
    public void closeBattleState(){
        battle = null;
    }

    /**
     * creates the playing state
     */
    public void initPlayingState(){
        playing = new Playing(this);
        try {
            Thread.sleep(1000); // Sleep for 1000 milliseconds = 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * closes the playing state
     */
    public void closePlayingState(){
        playing = null;
    }

    /**
     * Handles touches and passes the touch to the current state
     * @param event
     * @return
     */
    public boolean touchEvent(MotionEvent event) {
        switch (currentState){
            case MAINMENU:
                mainMenu.touchEvents(event);
                break;
            case PLAYING:
                playing.touchEvents(event);
                break;
            case BATTLE:
                battle.touchEvents(event);
                break;
        }
        return true;
    }

    /**
     * starts the game loop which is the engine of the game.
     */
    public void startGameLoop() {
        gameLoop.startGameLoop();
    }

    /**
     * state enum
     */
    public enum GameState {
        MAINMENU,PLAYING,BATTLE;

    }

    /**
     *
     * @return current state
     */
    public GameState getCurrentState(){
        return currentState;

    }

    /**
     * takes newState and sets it to the current state
     * @param newState
     */
    public void setCurrentState (GameState newState){
        this.currentState = newState;
    }
}
