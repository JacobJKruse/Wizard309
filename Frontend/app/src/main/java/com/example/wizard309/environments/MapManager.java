package com.example.wizard309.environments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.example.wizard309.Enviorment;
import com.example.wizard309.entities.Building;
import com.example.wizard309.entities.Buildings;
import com.example.wizard309.entities.Enviorments;
import com.example.wizard309.gamestates.Playing;
import com.example.wizard309.helpers.GameConstants;
import com.example.wizard309.helpers.HelpMethods;
import com.example.wizard309.main.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * MapManager class is used to handle all maps for the player and retrieve from the backend
 */
public class MapManager
{
    private Playing playing;
    private GameMap currentMap,outsidemap,insidemap,insideCave;


    private String sate;
    private float cameraX, cameraY;
    public boolean inside;
    private int roomId;

    /**
     * constructor for the mapmanager
     * @param state gets the state so it know what map to fetch
     */
    public MapManager(String state, Playing playing){
        this.sate = state;
        inside = false;
        this.playing = playing;
        roomId = 0;
        initializeMap(() -> {
            // Start your game or do other setup that requires the map to be initialized
        });

    }

    /**
     * map manager constructor with a state system
     * @param state
     */
    public MapManager(String state){
        this.sate = state;
        initializeMap(() -> {
            // Start your game or do other setup that requires the map to be initialized
        });

    }

    /**
     * checks the bounds of the map for the player
     * @param x
     * @param y
     * @return
     */
    public boolean canMove(double x, double y){
        if (x < 0 || y < 0){
            return false;
        }
        if(x >= getMaxWidth() || y >= getMaxHeight()){
            return false;
        }
        return true;
    }

    /**
     * gets the max width of the map
     * @return mapwidth or 0
     */
    public int getMaxWidth(){
        if (currentMap != null) {
            return currentMap.getArrayWidth() * GameConstants.sprites.SIZE;
        } else {
            // Handle the case where currentMap is null, e.g. by returning a default value
            return 0;
        }
    }
    /**
     * gets the max height of the map
     * @return mapheight or 0
     */
    public int getMaxHeight(){
        if (currentMap != null) {return currentMap.getArrayHeight() * GameConstants.sprites.SIZE;} else {
            // Handle the case where currentMap is null, e.g. by returning a default value
            return 0;
        }
    }

    /**
     * draws the buildings for the current map
     * @param c
     */
    public void drawbuildings(Canvas c){
        if(currentMap.getBuildingArrayList() != null){
            for(Building b : currentMap.getBuildingArrayList()){
                c.drawBitmap(b.getBuildingType().getHouseImg(), b.getPos().x+cameraX,b.getPos().y+cameraY,null);

            }
        }

    }

    /**
     * draws the background environments behind the player
     * @param c
     */
    public void behindEnviorments(Canvas c){
        if(currentMap.getEnviormentsArrayList() != null){
            for(Enviorment e : currentMap.getEnviormentsArrayList()){
                if(e.getBotH()>0){
                    c.drawBitmap(e.getEnviormentsType().getBottomItem(), e.getPos().x+cameraX,e.getPos().y+cameraY+(e.getTopH()*GameConstants.sprites.DEFAULT_SCALE),null);

                }

            }
        }

    }

    /**
     * draws any foreground the environment items in front of the player
     * @param c
     */
    public void inFrontEnviorments(Canvas c){
        if(currentMap.getEnviormentsArrayList() != null){
            for(Enviorment e : currentMap.getEnviormentsArrayList()){
                if(e.getTopH()>0){

                    c.drawBitmap(e.getEnviormentsType().getTopItem(), e.getPos().x+cameraX,e.getPos().y+cameraY,null);

                }

            }
        }
    }

    /**
     * draws the sprite tiles of the ground
     * @param c
     */
    public void drawTiles(Canvas c){
        if(currentMap == null)
            return;
        for(int i = 0; i < currentMap.getArrayHeight(); i++){
            for(int j = 0; j < currentMap.getArrayWidth(); j++){
                c.drawBitmap(currentMap.getFloor().getSprite(currentMap.getTileID(j,i)),j* GameConstants.sprites.SIZE + cameraX, i*GameConstants.sprites.SIZE + cameraY,null);
            }
        }
    }

    /**
     * draws doorways for testing
     * @param c
     */
    public void drawDoorways(Canvas c){
        if(currentMap.getDoorwayArrayList() != null){
//            Paint paint = new Paint();
//            paint.setColor(Color.RED); // Set the color of the rectangle
//            paint.setStyle(Paint.Style.STROKE); // Set the style to stroke to create an outline
//            paint.setStrokeWidth(10); // Set the stroke width

            for(Doorway d : currentMap.getDoorwayArrayList()){
                RectF rect = new RectF(d.getPosOfDoorway().x + cameraX, d.getPosOfDoorway().y + cameraY,
                        d.getPosOfDoorway().x + d.getWidth() + cameraX, d.getPosOfDoorway().y + d.getHeight() + cameraY);
//                c.drawRect(rect, paint);
            }
        }
    }

    /**
     * draws the map on a specified canvas
     * @param c
     */
    public void draw(Canvas c){
       drawTiles(c);
       drawbuildings(c);
       drawDoorways(c);
       behindEnviorments(c);
    }

    /**
     * sets the position of the map based on a "camera"
     * @param cameraX
     * @param cameraY
     */
    public void setCameraPos(float cameraX, float cameraY){
        this.cameraX = cameraX;
        this.cameraY = cameraY;
    }

    private void initializeMap(Runnable callback) {

        String states = this.sate;
        switch (states){
            case "Battle":
                int[][] testArray = {{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 99, 100, 100, 100, 123, 100, 100, 123, 100, 101, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 99, 124, 113, 113, 113, 113, 113, 113, 113, 113, 122, 101, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 112, 113, 113, 113, 113, 113, 113, 113, 113, 113, 113, 114, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 111, 113, 113, 113, 113, 113, 113, 113, 113, 113, 113, 114, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 112, 113, 113, 113, 113, 113, 113, 113, 113, 113, 113, 109, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 111, 113, 113, 113, 113, 113, 113, 113, 113, 113, 113, 114, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 112, 113, 113, 113, 113, 113, 113, 113, 113, 113, 113, 114, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 112, 113, 113, 113, 113, 113, 113, 113, 113, 113, 113, 114, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 112, 113, 113, 113, 113, 113, 113, 113, 113, 113, 113, 109, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 125, 98, 113, 113, 113, 113, 113, 113, 113, 113, 96, 127, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 125, 126, 97, 126, 126, 97, 126, 97, 97, 127, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110},{110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110, 110,110, 110, 110, 110, 110, 110, 110, 110, 110}};
                currentMap = new GameMap(testArray,Floor.GRASSY,null,null,null);
                break;
            case "Grassy":
                Maps.GRASSYMAP.getLayers(() -> {
                    Map<String, int[][]> mapLayers = Maps.GRASSYMAP.getMapLayers();
                    System.out.println("init map");
                    System.out.println(mapLayers.toString());
                    int[][] testArrayWithIds = mapLayers.get("grassOverworld");
                    System.out.println(Arrays.deepToString(testArrayWithIds));
                    ArrayList<Building> buildingArrayList = new ArrayList<>();
                    ArrayList<Enviorment> enviormentArrayList = new ArrayList<>();
                    ArrayList<Enviorment> healStatueArrayList = new ArrayList<>();

                    ArrayList<Enviorment> shopenviormentArrayList = new ArrayList<>();
                    enviormentArrayList.add(new Enviorment(new PointF(40,40), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(540,948), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(1272,36), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(140*12,193*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(192*12,212*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(258*12,236*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(252*12,191*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(227*12,307*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(102*12,328*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(6*12,339*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(59*12,373*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(175*12,370*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(9*12,189*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(22*12,440*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(297*12,4*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(337*12,56*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(432*12,24*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(408*12,256*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(557*12,314*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(9*12,189*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(791*12,694*12), Enviorments.SUNFLOWER));
                    enviormentArrayList.add(new Enviorment(new PointF(791*12,715*12), Enviorments.SUNFLOWER));
                    enviormentArrayList.add(new Enviorment(new PointF(791*12,737*12), Enviorments.SUNFLOWER));
                    enviormentArrayList.add(new Enviorment(new PointF(824*12,695*12), Enviorments.SAPLING));
                    enviormentArrayList.add(new Enviorment(new PointF(824*12,720*12), Enviorments.SAPLING));
                    enviormentArrayList.add(new Enviorment(new PointF(824*12,741*12), Enviorments.SAPLING));
                    enviormentArrayList.add(new Enviorment(new PointF(855*12,695*12), Enviorments.CABBAGE));
                    enviormentArrayList.add(new Enviorment(new PointF(855*12,717*12), Enviorments.CABBAGE));
                    enviormentArrayList.add(new Enviorment(new PointF(855*12,735*12), Enviorments.CABBAGE));
                    enviormentArrayList.add(new Enviorment(new PointF(300*12,568*12), Enviorments.DEADTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(491*12,568*12), Enviorments.DEADTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(273*12,648*12), Enviorments.DEADTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(496*12,652*12), Enviorments.DEADTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(530*12,606*12), Enviorments.DEADTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(236*12,607*12), Enviorments.DEADTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(390*12,528*12), Enviorments.DEADTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(368*12,334*12), Enviorments.MEDROCK));
                    enviormentArrayList.add(new Enviorment(new PointF(134*12,581*12), Enviorments.MEDROCK));
                    enviormentArrayList.add(new Enviorment(new PointF(771*12,330*12), Enviorments.MEDROCK));
                    enviormentArrayList.add(new Enviorment(new PointF(582*12,551*12), Enviorments.MEDROCK));
                    enviormentArrayList.add(new Enviorment(new PointF(496*12,227*12), Enviorments.CHOPPEDTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(396*12,7*12), Enviorments.CHOPPEDTREE));

                    enviormentArrayList.add(new Enviorment(new PointF(759*12,403*12), Enviorments.CHOPPEDTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(550*12,520*12), Enviorments.CHOPPEDTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(903*12,3*12), Enviorments.PINKTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(833*12,52*12), Enviorments.PINKTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(707*12,10*12), Enviorments.PINKTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(718*12,80*12), Enviorments.PINKTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(630*12,64*12), Enviorments.PINKTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(782*12,14*12), Enviorments.PINKTREE));
                    enviormentArrayList.add(new Enviorment(new PointF(432*12,99*12), Enviorments.THEROCK));
                    enviormentArrayList.add(new Enviorment(new PointF(703*12,496*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(780*12,480*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(852*12,527*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(747*12,575*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(946*12,752*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(34*12,714*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(828*12,620*12), Enviorments.TREE));
                    enviormentArrayList.add(new Enviorment(new PointF(1018*12,136*12), Enviorments.HEALSTATUE));
                    healStatueArrayList.add(new Enviorment(new PointF(1018*12,136*12), Enviorments.HEALSTATUE));





                    buildingArrayList.add(new Building(new PointF(2200,450), Buildings.SHOP));
                    buildingArrayList.add(new Building(new PointF(4608,7200), Buildings.CAVE));
                    buildingArrayList.add(new Building(new PointF(11496,8388),Buildings.FARM));
                    int[][] cottageArray = {
                            {374, 377, 377, 377, 377, 377, 378},
                            {396, 0, 1, 1, 1, 2, 400},
                            {396, 22, 23, 23, 23, 24, 400},
                            {396, 22, 23, 23, 23, 24, 400},
                            {396, 22, 23, 23, 23, 24, 400},
                            {396, 44, 45, 45, 45, 46, 400},
                            {462, 465, 463, 394, 464, 465, 466}};
                    int[][] caveArray = {{275,276,	276,276	,276,	276,	276,	276	,276,277},
                            {297,	298	,298,	298,	298	,298,	298	,298	,298,	299},
                            { 297,	298	,298	,298,	298,	298,	298,	298,	298,	299},
                            {297,	298	,298	,298,	298,	298,	298,	298,	298,	299},
                            {319,	320,	320	,320,	320,	350,	320,	320,	320,	321},
                    };
                    shopenviormentArrayList.add(new Enviorment(new PointF(400,350),Enviorments.RUG));

                    shopenviormentArrayList.add(new Enviorment(new PointF(500,50),Enviorments.BOOKSHELF));
                    shopenviormentArrayList.add(new Enviorment(new PointF(400,615),Enviorments.TABLE));
                    outsidemap = new GameMap(testArrayWithIds,Floor.GRASSY,buildingArrayList,enviormentArrayList,healStatueArrayList);
                     insidemap = new GameMap(cottageArray,Floor.INTERIORFLOOR,null,shopenviormentArrayList,null);
                        insideCave = new GameMap(caveArray,Floor.INTERIORFLOOR,null,null,null);
                   // HelpMethods.AddDoorwayToGamemap(currentMap,cottageBuilding,0);
                    HelpMethods.ConnectTwoDoorways(
                            outsidemap,
                            HelpMethods.CreateHitboxForDoorway(outsidemap, 0),
                            insidemap,
                            HelpMethods.CreateHitboxForDoorway(3, 6));
                    HelpMethods.ConnectTwoDoorways(
                            outsidemap,
                            HelpMethods.CreateHitboxForDoorway(outsidemap, 1),
                            insideCave,
                            HelpMethods.CreateHitboxForDoorway(5, 4));
                    // Call the callback function
                    callback.run();
                    currentMap = outsidemap;
                });
                break;
        }

    }

    /**
     * checks to see if player is on the doorway
     * @param playerHitbox
     * @return
     */
    public Doorway isPlayerOnDoorway(RectF playerHitbox) {
        for (Doorway doorway : currentMap.getDoorwayArrayList()) {

            if (doorway.isPlayerInsideDoorway(playerHitbox, cameraX, cameraY)){
                return doorway;
            }
        }
        return null;
    }

    private boolean isPlayerOnHealStatue(RectF playerHitbox){

        System.out.println(playerHitbox.left);
        System.out.println(playerHitbox.top);
        System.out.println((1018*12)+cameraX);
        System.out.println((136*12)+cameraY);
        return playerHitbox.intersects((1018*12)+cameraX,(136*12)+cameraY,(1040*12)+cameraX,(148*12)+cameraY);
    }
    private long lastMapChangeTime = 0;
    private static final long MAP_CHANGE_COOLDOWN = 1000; // Cooldown period in milliseconds

    /**
     * returns the current map
     * @return
     */
    public GameMap getCurrentMap() {
        return currentMap;
    }

    /**
     * changes the map to the target of the doorway
     * @param doorwayTarget
     */
    public void changeMap(Doorway doorwayTarget) {

        this.currentMap = doorwayTarget.getGameMapLocatedIn();
        inside = !inside;
        float cX = MainActivity.screenWidth / 2 - doorwayTarget.getPosOfDoorway().x-GameConstants.sprites.SIZE/2;
        float cY = MainActivity.screenHeight / 2 - doorwayTarget.getPosOfDoorway().y;

        playing.setCameraPos(new PointF(cX, cY));
        cameraX = cX;
        cameraY = cY;

        playing.setDoorwayJustPassed(true);
    }

    /**
     * gets the room id
     * @return
     */
    public int getRoomId() {
        if (currentMap == outsidemap) {
            return 0;
        } else if (currentMap == insidemap) {
            return 1;
        } else if (currentMap == insideCave) {
            return 2;
        } else {
            return 0; // or throw an exception, depending on your needs
        }
    }
}
