package com.example.wizard309.helpers;

import android.graphics.RectF;

import com.example.wizard309.R;
import com.example.wizard309.entities.Building;
import com.example.wizard309.environments.Doorway;
import com.example.wizard309.environments.GameMap;

/**
 * helper methods for doorways and other items
 */
public class HelpMethods {

    /**
     * adds the doorway to the gamemap
     * @param gameMapLocatedIn
     * @param gameMapTarget
     * @param buildingIndex
     */
    public static void AddDoorwayToGameMap(GameMap gameMapLocatedIn, GameMap gameMapTarget, int buildingIndex) {
        float houseX = gameMapLocatedIn.getBuildingArrayList().get(buildingIndex).getPos().x;
        float houseY = gameMapLocatedIn.getBuildingArrayList().get(buildingIndex).getPos().y;

        RectF hitbox = gameMapLocatedIn.getBuildingArrayList().get(buildingIndex).getBuildingType().getHitboxDoorway();
        Doorway doorway = new Doorway(new RectF(hitbox.left + houseX, hitbox.top + houseY, hitbox.right + houseX, hitbox.bottom + houseY), gameMapTarget);

        gameMapLocatedIn.addDoorway(doorway);

    }

    /**
     * creates a hitbox for a doorway object
     * @param gameMapLocatedIn
     * @param buildingIndex
     * @return
     */
    public static RectF CreateHitboxForDoorway(GameMap gameMapLocatedIn, int buildingIndex) {
        Building building = gameMapLocatedIn.getBuildingArrayList().get(buildingIndex);

        float x = building.getPos().x;
        float y = building.getPos().y;
        RectF hitbox = gameMapLocatedIn.getBuildingArrayList().get(buildingIndex).getBuildingType().getHitboxDoorway();

        return new RectF(hitbox.left + x, hitbox.top + y, hitbox.right + x, hitbox.bottom + y);

    }

    /**
     * creates a hitbox for a doorway
     * @param xTile
     * @param yTile
     * @return
     */
    public static RectF CreateHitboxForDoorway(int xTile, int yTile) {
        float x = xTile * GameConstants.sprites.SIZE;
        float y = yTile * GameConstants.sprites.SIZE;

        return new RectF(x, y, x + GameConstants.sprites.SIZE, y + GameConstants.sprites.SIZE);

    }

    /**
     * connects two doorways together
     * @param gameMapOne
     * @param hitboxOne
     * @param gameMapTwo
     * @param hitboxTwo
     */
    public static void ConnectTwoDoorways(GameMap gameMapOne, RectF hitboxOne, GameMap gameMapTwo, RectF hitboxTwo) {

        Doorway doorwayOne = new Doorway(hitboxOne, gameMapOne);
        Doorway doorwayTwo = new Doorway(hitboxTwo, gameMapTwo);

        doorwayOne.connectDoorway(doorwayTwo);
        doorwayTwo.connectDoorway(doorwayOne);
    }

}
