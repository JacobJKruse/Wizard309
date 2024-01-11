package com.example.wizard309.main;

/**
 * game loop class that will run constantly for the game object
 */
public class
GameLoop implements Runnable{

    private Thread gameThread;
    private Game game;

    /**
     * Takes game and creates the game thread
     * @param game
     */
    public GameLoop(Game game){
        gameThread = new Thread(this);
        this.game = game;
    }

    /**
     * Most important method, runs the game in the while true and calculates delta to pass on to other states.
     */
    @Override
    public void run() {
        long lastFPScheck = System.currentTimeMillis();
        int fps = 0;

        long lastDelta = System.nanoTime();
        long nanoSec = 1_000_000_000;//same as 1000000000 just makes easy to read

        while(true){

            long nowDelta = System.nanoTime();
            double timeSinceLastDelta = nowDelta - lastDelta;
            double delta = timeSinceLastDelta / nanoSec;

          game.update(delta);
          game.render();
            lastDelta = nowDelta;
            fps++;
            long now = System.currentTimeMillis();
            if(now - lastFPScheck >= 1000){
                System.out.println("FPS: "+fps);
                fps = 0;
                lastFPScheck += 1000;
            }
        }
    }

    /**
     * starts game loop
     */
    public void startGameLoop(){
        gameThread.start();
    }
}
