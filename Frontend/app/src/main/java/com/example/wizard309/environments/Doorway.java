package com.example.wizard309.environments;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * doorway object that connects the two gamemaps
 */
public class Doorway {

    private RectF hitbox;
    private boolean active = true;
    private final GameMap gameMapLocatedIn;
    private Doorway doorwayConnectedTo;

    /**
     * doorway constructor that connects the doorway to the gamemap
     * @param doorwayHitbox
     * @param gameMapLocatedIn
     */
    public Doorway(RectF doorwayHitbox, GameMap gameMapLocatedIn) {
        this.hitbox = doorwayHitbox;
        this.gameMapLocatedIn = gameMapLocatedIn;
        gameMapLocatedIn.addDoorway(this);
    }

    /**
     * connects the doorway
     * @param destinationDoorway
     */
    public void connectDoorway(Doorway destinationDoorway) {
        this.doorwayConnectedTo = destinationDoorway;
    }

    /**
     * returns the connected doorway
     * @return
     */
    public Doorway getDoorwayConnectedTo() {
        if (doorwayConnectedTo != null)
            return doorwayConnectedTo;
        return null;
    }

    /**
     * returns the hitbox of the doorway
     * @return
     */
    public RectF getHitbox() {
        return hitbox;
    }

    /**
     * checks to see if the player hithox intersects the doorway hitbox
     * @param playerHitbox
     * @param cameraX
     * @param cameraY
     * @return
     */
    public boolean isPlayerInsideDoorway(RectF playerHitbox, float cameraX, float cameraY) {


        return playerHitbox.intersects(hitbox.left + cameraX, hitbox.top + cameraY, hitbox.right + cameraX, hitbox.bottom + cameraY);
    }

    /**
     * gets the width of the doorway
     * @return
     */
    public float getWidth() {
        return hitbox.width();
    }

    /**
     * gets the height of the doorway
     * @return
     */
    public float getHeight() {
        return hitbox.height();
    }

    /**
     * checks to see if the doorway is unlocked
     * @return
     */
    public boolean isDoorwayActive() {
        return active;
    }

    /**
     * sets the doorway to be active
     * @param active
     */
    public void setDoorwayActive(boolean active) {
        this.active = active;
    }

    /**
     * gets the position of the doorway
     * @return
     */
    public PointF getPosOfDoorway() {
        return new PointF(hitbox.left, hitbox.top);
    }

    /**
     * get the game map the doorway is located in
     * @return
     */
    public GameMap getGameMapLocatedIn() {
        return gameMapLocatedIn;
    }
}