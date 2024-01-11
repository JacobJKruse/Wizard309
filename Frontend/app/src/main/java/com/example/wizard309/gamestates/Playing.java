package com.example.wizard309.gamestates;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.wizard309.WebSockets.WebSocketListener;
import com.example.wizard309.WebSockets.WebSocketManager;
import com.example.wizard309.entities.Card;
import com.example.wizard309.entities.CardIcons;
import com.example.wizard309.entities.Enemy;
import com.example.wizard309.entities.GameCharacters;
import com.example.wizard309.R;
import com.example.wizard309.entities.Wizard;
import com.example.wizard309.entities.enemies.Wisp;
import com.example.wizard309.environments.Doorway;
import com.example.wizard309.environments.GameMap;
import com.example.wizard309.helpers.BackgroundAudioPlayer;
import com.example.wizard309.main.Game;
import com.example.wizard309.main.MainActivity;
import com.example.wizard309.entities.Character;
import com.example.wizard309.entities.Icons;
import com.example.wizard309.entities.Player;
import com.example.wizard309.entities.enemies.BadGuy;
import com.example.wizard309.environments.MapManager;
import com.example.wizard309.helpers.GameConstants;
import com.example.wizard309.helpers.interfaces.GameStatesInterface;
import com.example.wizard309.ui.ButtonImages;
import com.example.wizard309.ui.CustomButton;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.example.wizard309.volley.net_utils.Const;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Playing class
 * The playing class is the core of the overworld game state, this communicates with websockets and gamestateinterface
 */
public class Playing extends BaseState  implements GameStatesInterface, WebSocketListener {
    private static boolean needsUpdate = false;
    private MapManager mapManager;

   // private BuildingManager buildingManager;
    RequestQueue queue;
    private Player player;
    private ArrayList<Character> enemies;

    private boolean doorwayJustPassed;
    private Map<String , Character> players = new HashMap<>();
    private static float collisionTimer = .3f;
    private float playerSpeed = 450;
    private float playerX = MainActivity.screenWidth/2, playerY = MainActivity.screenHeight /2;
    private float cameraX;
    private float cameraY;
    private int wallet;
    private boolean movePlayer;
    private Paint redPaint,namePaint,uiPaint,mPaintShader,gray;
    private PointF lastTouchDiff;
    private float xCenter ,yCenter,radius = 250;
    private float wizardHp,maxHp,wizardMP,maxMP,wizardXp,wizardNextLevel;
    private Paint circlePaint,yellowPaint,hpFill,mpFill,xpFill;
    private float xTouch,yTouch,hpRight,mpRight;
    private float innerJoyStickX,innerJoyStickY;
    private Bitmap playerSprite;
    private int rad;
    private boolean playerEnemeyCollide = false,increaseDamageToggle = true;

    private boolean isAdmin = false, isMod = false,touchDown, showingMenu = false,showingSettings = false,showingDeck = false, showingCardInv = false,showingMod = false, showingAdmin = false,inRangeOfShop = false,showingShop =false;
    private boolean menuUp = false, settingsUp = false;
    private String name = "Null";
    private JSONObject wiz;

    private CustomButton menu,settings,chat,closeMenu,deckInv;
    private CustomButton deckleft,deckright,card1,card2,card3,card4,card5,card6,card7,card8;
    private CustomButton mod,modleft,modright,modSlot1,modSlot2,modSlot3;
    private CustomButton admin,adminHpUp,adminHpDown,adminLevelUp,adminLevelDown,adminCard1,adminCard2,adminCard3,adminLeft,adminRight,toggleDamage;
    private CustomButton shopButton;
    private boolean[] slots = {false,false,false,false,false,false,false,false};
    private int cardPageIndex = 1,modPageIndex = 1,adminPageIndex = 1,shopPageIndex = 1;
    private List<Card> allCards = new ArrayList<>();
    private String BASE_URL = "ws://coms-309-032.class.las.iastate.edu:8080/player/";
    //private String BASE_URL = "ws://10.0.2.2:8080/player/";
    private static String serverUrl = null;

    private float deckCheck = 1f;
    private boolean inside;
    private BackgroundAudioPlayer mediaPlayer;


    /**
     * Playing constructor
     * @param game insert the Game class here
     */
    public Playing(Game game){
        super(game);

        queue = Volley.newRequestQueue(MainActivity.getGameContext());
        namePaint = new Paint();namePaint.setColor(Color.BLACK);namePaint.setStyle(Paint.Style.FILL);namePaint.setTextSize(30);namePaint.setTextAlign(Paint.Align.CENTER);
        uiPaint = new Paint();uiPaint.setColor(Color.BLACK);uiPaint.setStyle(Paint.Style.FILL);uiPaint.setTextSize(60);uiPaint.setTextAlign(Paint.Align.LEFT);uiPaint.setTypeface(loadFont(MainActivity.getGameContext(),R.font.pressstart2pregular));
        redPaint = new Paint();redPaint.setColor(Color.RED);redPaint.setStrokeWidth(2);redPaint.setStyle(Paint.Style.STROKE);
        xCenter = MainActivity.screenWidth / 9;           // <--- these two values work rn as a good scale for screen sizes
        yCenter = MainActivity.screenHeight  - MainActivity.screenHeight  / 4;
        hpFill = new Paint();hpFill.setColor(Color.rgb(156, 48, 40));hpFill.setStyle(Paint.Style.FILL);
        mpFill = new Paint();mpFill.setColor(Color.rgb(69, 57, 173));mpFill.setStyle(Paint.Style.FILL);
        xpFill = new Paint();xpFill.setColor(Color.rgb(0, 204, 102));xpFill.setStyle(Paint.Style.FILL);
        circlePaint = new Paint();circlePaint.setStyle(Paint.Style.STROKE);circlePaint.setStrokeWidth(10);circlePaint.setColor(Color.WHITE);circlePaint.setStyle(Paint.Style.STROKE);
        gray = new Paint();
        gray.setStyle(Paint.Style.FILL);
        gray.setColor(Color.DKGRAY);
        gray.setAlpha(175);
        mapManager = new MapManager("Grassy",this);
       // buildingManager = new BuildingManager();
        innerJoyStickX = MainActivity.screenWidth /9 + 10 - Icons.JOYSTICKOUTTER.getSpriteLogo().getWidth()/2 +Icons.JOYSTICKINNER.getSpriteLogo().getWidth()/2;
        innerJoyStickY =MainActivity.screenHeight  - MainActivity.screenHeight  / 4 + 5 - Icons.JOYSTICKOUTTER.getSpriteLogo().getHeight()/2 + Icons.JOYSTICKINNER.getSpriteLogo().getHeight()/2;
        mPaintShader = new Paint();
        wiz = MainActivity.getWizard();
        System.out.println("wiz in playing" + wiz);
        try{
            wallet = wiz.getInt("wallet");
            name = wiz.getString("displayName");
            switch (wiz.getString("extension")) {
                case "fire_icon":
                    player = new Player(GameCharacters.PLAYERFIRE);
                    playerSprite = GameCharacters.PLAYERFIRE.getSprite(0,0);
                    break;
                case "ice_icon":
                    player = new Player(GameCharacters.PLAYERICE);
                    playerSprite = GameCharacters.PLAYERICE.getSprite(0,0);
                    break;
                case "storm_icon":
                    player = new Player(GameCharacters.PLAYERSTORM);
                    playerSprite = GameCharacters.PLAYERSTORM.getSprite(0,0);
                    break;
                case "life_icon":
                    player = new Player(GameCharacters.PLAYERLIFE);
                    playerSprite = GameCharacters.PLAYERLIFE.getSprite(0,0);
                    break;
                case "death_icon":
                    player = new Player(GameCharacters.PLAYERDEATH);
                    playerSprite = GameCharacters.PLAYERDEATH.getSprite(0,0);
                    break;
                case "earth_icon":
                    player = new Player(GameCharacters.PLAYEREARTH);
                    playerSprite = GameCharacters.PLAYEREARTH.getSprite(0,0);
                    break;
            }
            wizardHp = wiz.getInt("currhp");
            wizardMP = wiz.getInt("curr_mana");
            maxHp = wiz.getInt("maxHP");
            maxMP = wiz.getInt("max_mana");
            wizardXp = wiz.getInt("currXp");
            wizardNextLevel = wiz.getInt("nextLvlXp");
            System.out.println("WIZARD HP: " + wizardHp + "/" + maxHp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<Integer> xpath = new ArrayList<>();
        xpath.add(100);xpath.add(1100);xpath.add(1100);xpath.add(100);xpath.add(100);
        List<Integer> ypath = new ArrayList<>();
        ypath.add(100);ypath.add(100);ypath.add(700);ypath.add(700);ypath.add(100);

        enemies = new ArrayList<>();
        for(int i = 0; i < 0; i++){
          enemies.add(new Wisp(new PointF(50,50),xpath,ypath));
          //enemies.add(new BadGuy(new PointF(100,100)));
       }

        try {
            System.out.println("WIZARD");
            System.out.println(wiz.toString());
            serverUrl = BASE_URL + wiz.getString("displayName")+"/"+playerX+"/"+playerY+"/"+wiz.getInt("id");
            System.out.println(serverUrl);
        } catch (JSONException e) {
            System.out.println("ERROR");
            throw new RuntimeException(e);
        }
        try {
            MainActivity.deck = getDeck();
            MainActivity.cardInv = getCardInv();
            allCards = getAllCards();

        } catch (Exception e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
        //getInventory();
        setCollisionTimer(10);
        checkUserLevel();
        // Establish WebSocket connection and set listener
        // Establish WebSocket connection and set listener
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(serverUrl, Playing.this);
        menu = new CustomButton(MainActivity.screenWidth - ButtonImages.MENUBUTTON.getWidth() - 75,
                MainActivity.screenHeight - ButtonImages.MENUBUTTON.getHeight() - 75,
                ButtonImages.MENUBUTTON.getWidth(), ButtonImages.MENUBUTTON.getHeight());
        settings = new CustomButton(MainActivity.screenWidth - ButtonImages.SETTINGSBUTTON.getWidth() -75,
                75,
                ButtonImages.SETTINGSBUTTON.getWidth(),
                ButtonImages.SETTINGSBUTTON.getHeight());

        closeMenu = new CustomButton(MainActivity.screenWidth/2 + Icons.MENUBACK.getSpriteLogo().getWidth() /2 - ButtonImages.CLOSEMENUBUTTON.getWidth()/2,
                50 - ButtonImages.CLOSEMENUBUTTON.getHeight()/4,
                ButtonImages.CLOSEMENUBUTTON.getWidth(),
                ButtonImages.CLOSEMENUBUTTON.getHeight());
//        cardInv = new CustomButton(MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth()/2 + ButtonImages.MENUDECKBUTTON.getWidth()/2 ,
//                105 + Icons.MENUWIZARDFRAME.getSpriteLogo().getWidth()/4 + Icons.MENUNAMEBACK.getSpriteLogo().getHeight()*2 ,
//                ButtonImages.MENUDECKBUTTON.getWidth(),
//                ButtonImages.MENUDECKBUTTON.getHeight());
        deckInv =  new CustomButton(MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth()/2 + ButtonImages.MENUDECKBUTTON.getWidth()/2 ,
                105 + Icons.MENUWIZARDFRAME.getSpriteLogo().getWidth()/4 + Icons.MENUNAMEBACK.getSpriteLogo().getHeight()*2 ,
                ButtonImages.MENUDECKBUTTON.getWidth(),
                ButtonImages.MENUDECKBUTTON.getHeight());

        deckleft = new CustomButton( 40 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2,
                50 + Icons.MENUBACK.getSpriteLogo().getHeight() - 40 - ButtonImages.DECKLEFTARROW.getWidth() ,
                ButtonImages.DECKLEFTARROW.getWidth(), ButtonImages.DECKLEFTARROW.getHeight());
        deckright = new CustomButton(MainActivity.screenWidth/2 + Icons.MENUBACK.getSpriteLogo().getWidth()/2 - 40 - ButtonImages.DECKLEFTARROW.getWidth(),
                50 + Icons.MENUBACK.getSpriteLogo().getHeight() - 40 - ButtonImages.DECKLEFTARROW.getWidth() ,
                ButtonImages.DECKRIGHTARROW.getWidth(), ButtonImages.DECKRIGHTARROW.getHeight());

        float cardWidth = CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f));
        float cardHeight = CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f));
        card1 = new CustomButton((60 + cardWidth/2 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 + ((0 * cardWidth) + (0 * cardWidth/8))) - 150 * MainActivity.screenWidth * (1.0f/3120.0f),
                (100 + cardHeight/2) - 250 * MainActivity.screenWidth * (1.0f/3120.0f),
                CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f)),
                CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f)));
        card2 = new CustomButton((60 + cardWidth/2 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 + ((1 * cardWidth) + (1 * cardWidth/8))) - 150 * MainActivity.screenWidth * (1.0f/3120.0f),
                (100 + cardHeight/2) - 250 * MainActivity.screenWidth * (1.0f/3120.0f),
                CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f)),
                CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f)));
        card3 = new CustomButton((60 + cardWidth/2 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 + ((2 * cardWidth) + (2 * cardWidth/8))) - 150 * MainActivity.screenWidth * (1.0f/3120.0f),
                (100 + cardHeight/2) - 250 * MainActivity.screenWidth * (1.0f/3120.0f),
                CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f)),
                CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f)));
        card4 = new CustomButton((60 + cardWidth/2 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 + ((3 * cardWidth) + (3 * cardWidth/8))) - 150 * MainActivity.screenWidth * (1.0f/3120.0f),
                (100 + cardHeight/2) - 250 * MainActivity.screenWidth * (1.0f/3120.0f),
                CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f)),
                CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f)));
        card5 = new CustomButton((60 + cardWidth/2 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 + ((4 * cardWidth) + (4 * cardWidth/8))) - 150 * MainActivity.screenWidth * (1.0f/3120.0f),
                (100 + cardHeight/2) - 250 * MainActivity.screenWidth * (1.0f/3120.0f),
                CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f)),
                CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f)));
        card6 = new CustomButton((60 + cardWidth/2 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 + ((1 * cardWidth) + (1 * cardWidth/8))) - 150 * MainActivity.screenWidth * (1.0f/3120.0f),
                (cardHeight + 200 + cardHeight/2) - 250 * MainActivity.screenWidth * (1.0f/3120.0f),
                CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f)),
                CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f)));
        card7 = new CustomButton((60 + cardWidth/2 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 + ((2 * cardWidth) + (2 * cardWidth/8))) - 150 * MainActivity.screenWidth * (1.0f/3120.0f),
                (cardHeight + 200 + cardHeight/2) - 250 * MainActivity.screenWidth * (1.0f/3120.0f),
                CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f)),
                CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f)));
        card8 = new CustomButton((60 + cardWidth/2 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 + ((3 * cardWidth) + (3 * cardWidth/8))) - 150 * MainActivity.screenWidth * (1.0f/3120.0f),
                (cardHeight + 200 + cardHeight/2) - 250 * MainActivity.screenWidth * (1.0f/3120.0f),
                CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f)),
                CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f)));
        mod = new CustomButton(MainActivity.screenWidth/2 - 175 - ButtonImages.MODBUTTON.getWidth()/2,
                Icons.MENUBACK.getSpriteLogo().getHeight() - ButtonImages.MODBUTTON.getHeight() - 50,
                ButtonImages.MODBUTTON.getWidth(), ButtonImages.MODBUTTON.getHeight());
        modleft = new CustomButton( 40 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2,
                50 + Icons.MENUBACK.getSpriteLogo().getHeight() - 40 - ButtonImages.DECKLEFTARROW.getWidth() ,
                ButtonImages.DECKLEFTARROW.getWidth(), ButtonImages.DECKLEFTARROW.getHeight());
        modright = new CustomButton(MainActivity.screenWidth/2 + Icons.MENUBACK.getSpriteLogo().getWidth()/2 - 40 - ButtonImages.DECKLEFTARROW.getWidth(),
                50 + Icons.MENUBACK.getSpriteLogo().getHeight() - 40 - ButtonImages.DECKLEFTARROW.getWidth() ,
                ButtonImages.DECKRIGHTARROW.getWidth(), ButtonImages.DECKRIGHTARROW.getHeight());
        modSlot1 = new CustomButton(50 +  GameCharacters.PLAYERFIRE.getSprite(0,0).getWidth() + MainActivity.screenWidth / 2 - Icons.MENUBACK.getSpriteLogo().getWidth() / 2,
                100 + (((0 - ((modPageIndex - 1) * 3)) * GameCharacters.PLAYERFIRE.getSprite(0,0).getHeight()) + 50),
                GameCharacters.PLAYERFIRE.getSprite(0,0).getWidth(), GameCharacters.PLAYERFIRE.getSprite(0,0).getHeight());
        modSlot2 = new CustomButton(50 +  GameCharacters.PLAYERFIRE.getSprite(0,0).getWidth() + MainActivity.screenWidth / 2 - Icons.MENUBACK.getSpriteLogo().getWidth() / 2,
                100 + (((1 - ((modPageIndex - 1) * 3)) * GameCharacters.PLAYERFIRE.getSprite(0,0).getHeight()) + 50),
                GameCharacters.PLAYERFIRE.getSprite(0,0).getWidth(), GameCharacters.PLAYERFIRE.getSprite(0,0).getHeight());
        modSlot3 = new CustomButton(50 +  GameCharacters.PLAYERFIRE.getSprite(0,0).getWidth() + MainActivity.screenWidth / 2 - Icons.MENUBACK.getSpriteLogo().getWidth() / 2,
                100 + (((2 - ((modPageIndex - 1) * 3)) * GameCharacters.PLAYERFIRE.getSprite(0,0).getHeight()) + 50),
                GameCharacters.PLAYERFIRE.getSprite(0,0).getWidth(), GameCharacters.PLAYERFIRE.getSprite(0,0).getHeight());

        admin = new CustomButton(MainActivity.screenWidth/2 + 175 - ButtonImages.MODBUTTON.getWidth()/2,
                Icons.MENUBACK.getSpriteLogo().getHeight() - ButtonImages.MODBUTTON.getHeight() - 50,
                ButtonImages.MODBUTTON.getWidth(), ButtonImages.MODBUTTON.getHeight());
        adminHpUp = new CustomButton(MainActivity.screenWidth/2 + Icons.MENUBACK.getSpriteLogo().getWidth()/2 - 200 - ButtonImages.DECKRIGHTARROW.getWidth(),
                200 - ButtonImages.DECKRIGHTARROW.getHeight()/2,
                ButtonImages.DECKRIGHTARROW.getWidth(),ButtonImages.DECKRIGHTARROW.getHeight());
        adminHpDown = new CustomButton(MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth()/2 + 200,
                200 - ButtonImages.DECKRIGHTARROW.getHeight()/2,
                ButtonImages.DECKLEFTARROW.getWidth(),ButtonImages.DECKLEFTARROW.getHeight());
        adminRight = new CustomButton(MainActivity.screenWidth/2 + Icons.MENUBACK.getSpriteLogo().getWidth()/2 - 40 - ButtonImages.DECKLEFTARROW.getWidth(),
                50 + Icons.MENUBACK.getSpriteLogo().getHeight() - 40 - ButtonImages.DECKLEFTARROW.getWidth() ,
                ButtonImages.DECKRIGHTARROW.getWidth(),ButtonImages.DECKRIGHTARROW.getHeight());
        adminLeft = new CustomButton(40 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2,
                50 + Icons.MENUBACK.getSpriteLogo().getHeight() - 40 - ButtonImages.DECKLEFTARROW.getWidth() ,
                ButtonImages.DECKLEFTARROW.getWidth(),ButtonImages.DECKLEFTARROW.getHeight());
        adminLevelUp = new CustomButton(MainActivity.screenWidth/2 + Icons.MENUBACK.getSpriteLogo().getWidth()/2 - 200 - ButtonImages.DECKRIGHTARROW.getWidth(),
                450 - ButtonImages.DECKRIGHTARROW.getHeight()/2,
                ButtonImages.DECKRIGHTARROW.getWidth(),ButtonImages.DECKRIGHTARROW.getHeight());
        adminLevelDown = new CustomButton(MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth()/2 + 200,
                450- ButtonImages.DECKRIGHTARROW.getHeight()/2,
                ButtonImages.DECKLEFTARROW.getWidth(),ButtonImages.DECKLEFTARROW.getHeight());
        adminCard1 = new CustomButton((60 + cardWidth/2 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 + ((1 * cardWidth) + (1 * cardWidth/8))) - 150 * MainActivity.screenWidth * (1.0f/3120.0f),
                (cardHeight + 200 + cardHeight/2) - 250 * MainActivity.screenWidth * (1.0f/3120.0f),
                CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f)),
                CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f)));
        adminCard2 = new CustomButton((60 + cardWidth/2 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 + ((2 * cardWidth) + (2 * cardWidth/8))) - 150 * MainActivity.screenWidth * (1.0f/3120.0f),
                (cardHeight + 200 + cardHeight/2) - 250 * MainActivity.screenWidth * (1.0f/3120.0f),
                CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f)),
                CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f)));
        adminCard3 = new CustomButton((60 + cardWidth/2 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 + ((3 * cardWidth) + (3 * cardWidth/8))) - 150 * MainActivity.screenWidth * (1.0f/3120.0f),
                (cardHeight + 200 + cardHeight/2) - 250 * MainActivity.screenWidth * (1.0f/3120.0f),
                CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f)),
                CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f)));
        toggleDamage = new CustomButton( MainActivity.screenWidth/2 - Icons.MENUBUTTONTEXT.getSpriteLogo().getWidth()/2 - 20,
                50 + Icons.MENUBACK.getSpriteLogo().getHeight() - Icons.MENUBUTTONTEXT.getSpriteLogo().getHeight(),
                Icons.MENUBUTTONTEXT.getSpriteLogo().getWidth(),
                Icons.MENUBUTTONTEXT.getSpriteLogo().getHeight());
        shopButton = new CustomButton(MainActivity.screenWidth - ButtonImages.MENUBUTTON.getWidth() - 100 - ButtonImages.SHOPBUTTON.getWidth(),
                MainActivity.screenHeight - ButtonImages.MENUBUTTON.getHeight() - 75,
                ButtonImages.SHOPBUTTON.getWidth(), ButtonImages.SHOPBUTTON.getHeight());
    }

    private void checkUserLevel(){
        int id = MainActivity.getUser();
        String urlStr = "http://coms-309-032.class.las.iastate.edu:8080/users/" + id;

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, urlStr, null, response -> {
            try {

                if(response.getString("userLevel").equals("Mod")){
                    isMod = true;
                }
                if(response.getString("userLevel").equals("Admin")){
                    isAdmin = true;
                    isMod = true;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> System.out.println(error));

        queue.add(jsonArrayRequest);
    }
    /**
     * getDeck returns an ArrayList of Cards from the players deck this is an asyc request done on the join
     * @return
     */
    public ArrayList<Card> getDeck() {
        String urlStr = "http://coms-309-032.class.las.iastate.edu:8080/Deck/";
        try{
            urlStr += wiz.getInt("id");
        }catch (JSONException e){e.printStackTrace();}
        ArrayList<Card> cardList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlStr, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject responseObj = response.getJSONObject(i);
                    int id = responseObj.getInt("id");
                    String extension = responseObj.getString("extension");
                    String spellName = responseObj.getString("spell_name");
                    String element = responseObj.getString("element");
                    int attackPower = responseObj.getInt("attack_power");
                    int manaCost = responseObj.getInt("mana_cost");
                    String attackType = responseObj.getString("attack_type");

                    Card card = new Card(id, extension, spellName, element, attackPower, manaCost, attackType);
                    cardList.add(card);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, error -> System.out.println(error));

        queue.add(jsonArrayRequest);
        return cardList;
    }

    /**
     * gets the card inventory of the player
     * @return
     */
    public ArrayList<Card> getCardInv(){
        String urlStr = "";
        try {
            //urlStr = "http://10.0.2.2:8080/CardInventory/" + wiz.getInt("id");
            urlStr = "http://coms-309-032.class.las.iastate.edu:8080/CardInventory/" + wiz.getInt("id");
        }catch(JSONException e){
            e.printStackTrace();
        }
        ArrayList<Card> cardList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlStr, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject responseObj = response.getJSONObject(i);
                    int id = responseObj.getInt("id");
                    String extension = responseObj.getString("extension");
                    String spellName = responseObj.getString("spell_name");
                    String element = responseObj.getString("element");
                    int attackPower = responseObj.getInt("attack_power");
                    int manaCost = responseObj.getInt("mana_cost");
                    String attackType = responseObj.getString("attack_type");

                    Card card = new Card(id, extension, spellName, element, attackPower, manaCost, attackType);
                    cardList.add(card);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, error -> System.out.println(error));

        queue.add(jsonArrayRequest);
        return cardList;
    }

    private ArrayList<Card> getAllCards(){
        String urlStr = "";
            //urlStr = "http://10.0.2.2:8080/CardInventory/" + wiz.getInt("id");
            urlStr = "http://coms-309-032.class.las.iastate.edu:8080/cards";

        ArrayList<Card> cardList = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlStr, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject responseObj = response.getJSONObject(i);
                    int id = responseObj.getInt("id");
                    String extension = responseObj.getString("extension");
                    String spellName = responseObj.getString("spell_name");
                    String element = responseObj.getString("element");
                    int attackPower = responseObj.getInt("attack_power");
                    int manaCost = responseObj.getInt("mana_cost");
                    String attackType = responseObj.getString("attack_type");
                    Card card = new Card(id, extension, spellName, element, attackPower, manaCost, attackType);
                    cardList.add(card);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, error -> System.out.println(error));

        queue.add(jsonArrayRequest);
        return cardList;
    }

    /**
     * calls update wiz when returning from battles
     */
    public static void callUpdateWiz(){
        needsUpdate = true;
    }
    private void updateWiz(){

        String newUrl = Const.URL_WIZARDS_JSON_ARRAY +"/";
        try{
            newUrl +=  wiz.getInt("id");
        }catch (JSONException e){e.printStackTrace();};
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, newUrl, null, response -> {
            try {
                var wizard = response;
                System.out.println(wizard);
                wiz.put("id",wizard.getInt("id"));
                wiz.put("displayName",wizard.get("displayName"));
                wiz.put("maxHP",wizard.getInt("maxHP"));
                wiz.put("lvl",wizard.getInt("lvl"));
                wiz.put("nextLvlXp",wizard.getInt("nextLvlXp"));
                wiz.put("currXp",wizard.getInt("currXp"));
                wizardXp = wizard.getInt("currXp");
                wiz.put("element", wizard.get("element"));
                wiz.put("extension", wizard.get("extension"));
                wiz.put("deckSize",wizard.getInt("deckSize"));
                wiz.put("currhp",wizard.getInt("currhp"));
                wizardHp = wizard.getInt("currhp");
                wiz.put("max_mana",wizard.getInt("max_mana"));
                wiz.put("curr_mana",wizard.getInt("curr_mana"));
                wizardMP = wizard.getInt("curr_mana");
                wiz.put("wallet",wizard.getInt("wallet"));
                wallet =  wizard.getInt("wallet");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> System.out.println(error));

        MainActivity.wiz = wiz;
        queue.add(jsonArrayRequest);
    }


    private void healWiz(){
        try {
            wizardHp = maxHp;
            wizardMP = maxMP;
            wiz.put("currhp",wizardHp);
            wiz.put("curr_mana",wizardMP);
            updateDatabaseWiz();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }
    private void updateDatabaseWiz() {
        String newUrl = Const.URL_WIZARDS_JSON_ARRAY + "/";
        try {
            newUrl += wiz.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, newUrl, wiz,
                response -> {
                    try {
                        System.out.println("Update Successful: " + response);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> System.out.println(error)
        );

        MainActivity.wiz = wiz;
        queue.add(jsonObjectRequest);
    }


    private void setWizWallet(){

        String newUrl = Const.URL_WIZARDS_JSON_ARRAY +"/";
        try{
            newUrl +=  wiz.getInt("id");
        }catch (JSONException e){e.printStackTrace();};
        try {
            wiz.put("wallet",wallet);
            System.out.println(wiz.getInt("wallet"));

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        System.out.println(newUrl);


        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.PUT, newUrl, wiz, response -> {
            System.out.println("UPDATING WALLET");

        }, error -> System.out.println(error));

        queue.add(jsonArrayRequest);
    }

    /**
     * update runs on each frame of the game, this takes in
     * the delta time value to determine the time between frames
     * @param delta deltatime is a value calculated to determine the time between the frames
     */
    @Override
    public void update(double delta) {
        if(needsUpdate){
            updateWiz();
            startWebsocket();
            needsUpdate = false;
            MainActivity.swtichAudio(R.raw.overworld);
        }

        updatePlayerMove(delta);

        player.update(delta,movePlayer);

        //THIS IS TO MAKE IT SO IT DOESNT CHECK EVERY FRAME!!!
        collisionTimer -= delta;
        if(collisionTimer < 0){
            checkCollide();
            collisionTimer = .1f;//10 times a second should be good for walls even
        }


        //remove the synchros after the game states are added
        synchronized (enemies) {
            for (Character s : enemies) {
                if(s instanceof Wisp){
                    ((Wisp) s).update(delta, mapManager.getMaxWidth(), mapManager.getMaxHeight());
                }
                if(s instanceof BadGuy && !mapManager.inside){
                    ((BadGuy) s).update(delta, mapManager.getMaxWidth(), mapManager.getMaxHeight());
                }

            }
        }
        synchronized (players) {
            for (Map.Entry<String, Character> entry : players.entrySet()) {
                if(entry.getValue() instanceof Wizard) {
                    Wizard w = (Wizard) entry.getValue();
                    if(w.getRoomID() == mapManager.getRoomId() && !mapManager.inside) {
                        w.update(delta, w.position.x, w.position.y);
                    }
                }else if (entry.getValue() instanceof Enemy ){
                    Enemy e = (Enemy) entry.getValue();
                    if(e.getRooomID() == mapManager.getRoomId() && !mapManager.inside){
                        e.update(delta, e.position.x, e.position.y);
                    }

                }
            }
        }

        mapManager.setCameraPos(cameraX,cameraY);
        checkForDoorway();

        try {
            if(wizardHp != wiz.getInt("maxHP") && checkForHealStatue()){
                healWiz();
                System.out.println("heal wizard");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        // buildingManager.setCameraPos(cameraX,cameraY);
    }

    private void handleShop(Canvas c){
        if(mapManager.getRoomId() != 1)
            return;

        if(Math.abs((playerX - 200) - (500 + cameraX)) < 350 &&Math.abs(playerY - (500 + cameraY)) < 350 ){
            inRangeOfShop = true;
        }else{inRangeOfShop = false;}

        if(inRangeOfShop && !showingShop && !showingMenu && !showingDeck && !showingSettings && !showingMod && !showingAdmin){
            //render shop button
            c.drawBitmap(ButtonImages.SHOPBUTTON.getButtonImage(shopButton.isClicked()),
                    shopButton.getHitbox().left,shopButton.getHitbox().top,null);
        }

        if(showingShop){

            c.drawBitmap(Icons.MENUBACK.getSpriteLogo(),
                    MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 ,
                    50 ,null);
            c.drawBitmap(ButtonImages.CLOSEMENUBUTTON.getButtonImage(closeMenu.isClicked()),
                    closeMenu.getHitbox().left,
                    closeMenu.getHitbox().top, null);
            //c.drawText("deckinv",500,500,namePaint);
            //render cards here, seems not the best idea to do it with a for loop but should be find tbh
            float cardWidth = CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f));
            float cardHeight = CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f));
            for(int i = 0; i < allCards.size()- (8 * (shopPageIndex - 1)) ;i++) {
                boolean found = false;
                if (i > 7) {
                    break;
                }
                int index = i + ((shopPageIndex - 1) * 8);
                if (i > 4) {
                    allCards.get(index).draw(c, MainActivity.screenWidth * (1.0f / 3420.0f));
                    allCards.get(index).setPosition(
                            60 + cardWidth / 2 + MainActivity.screenWidth / 2 - Icons.MENUBACK.getSpriteLogo().getWidth() / 2 + (((i - 4) * cardWidth) + ((i - 4) * cardWidth / 8)),
                            200 + cardHeight / 2 + cardHeight,
                            MainActivity.screenWidth * (1.0f / 3120.0f));
                    uiPaint.setColor(Color.WHITE);
                    uiPaint.setTextAlign(Paint.Align.CENTER);
                    uiPaint.setTextSize(MainActivity.screenWidth/90);
                    for(int j = 0; j < MainActivity.cardInv.size();j++){
                        if(allCards.get(index).getID() == MainActivity.cardInv.get(j).getID()){
                            found = true;
                        }
                    }
                    if(found){
                        uiPaint.setTextSize(MainActivity.screenWidth/130);
                        c.drawText("ALREADY BOUGHT!",
                                60 + cardWidth / 2 + MainActivity.screenWidth / 2 - Icons.MENUBACK.getSpriteLogo().getWidth() / 2 + (((i - 4) * cardWidth) + ((i - 4) * cardWidth / 8)),
                                225 + 2*cardHeight,
                                uiPaint);
                    }else{
                        c.drawText("Price: " + (allCards.get(index).getDamage() * MainActivity.cardInv.size()),
                                60 + cardWidth / 2 + MainActivity.screenWidth / 2 - Icons.MENUBACK.getSpriteLogo().getWidth() / 2 + (((i - 4) * cardWidth) + ((i - 4) * cardWidth / 8)),
                                225 + 2*cardHeight,
                                uiPaint);
                    }
                    uiPaint.setTextAlign(Paint.Align.LEFT);


                } else {
                    allCards.get(index).draw(c, MainActivity.screenWidth * (1.0f / 3420.0f));
                    allCards.get(index).setPosition(
                            60 + cardWidth / 2 + MainActivity.screenWidth / 2 - Icons.MENUBACK.getSpriteLogo().getWidth() / 2 + ((i * cardWidth) + (i * cardWidth / 8)),
                            100 + cardHeight / 2,
                            MainActivity.screenWidth * (1.0f / 3120.0f));
                    uiPaint.setColor(Color.WHITE);

                    uiPaint.setTextAlign(Paint.Align.CENTER);
                    uiPaint.setTextSize(MainActivity.screenWidth/90);

                    for(int j = 0; j < MainActivity.cardInv.size();j++){
                        if(allCards.get(index).getID() == MainActivity.cardInv.get(j).getID()){
                          found = true;
                        }
                    }
                    if(found){
                        uiPaint.setTextSize(MainActivity.screenWidth/130);
                        c.drawText("ALREADY BOUGHT!",
                                60 + cardWidth / 2 + MainActivity.screenWidth / 2 - Icons.MENUBACK.getSpriteLogo().getWidth() / 2 + ((i * cardWidth) + (i * cardWidth / 8)),
                                125 + cardHeight,
                                uiPaint);
                    }else{
                        c.drawText("Price: " + (allCards.get(index).getDamage() * MainActivity.cardInv.size()),
                                60 + cardWidth / 2 + MainActivity.screenWidth / 2 - Icons.MENUBACK.getSpriteLogo().getWidth() / 2 + ((i * cardWidth) + (i * cardWidth / 8)),
                                125 + cardHeight,
                                uiPaint);
                    }
                    uiPaint.setTextAlign(Paint.Align.LEFT);


                }
            }
            c.drawBitmap(ButtonImages.DECKRIGHTARROW.getButtonImage(deckright.isClicked()),
                    deckright.getHitbox().left,deckright.getHitbox().top,null);
            c.drawBitmap(ButtonImages.DECKLEFTARROW.getButtonImage(deckleft.isClicked()),
                    deckleft.getHitbox().left,deckleft.getHitbox().top,null);
            float width = namePaint.measureText("Gold: " + wallet);

            c.drawText("Gold: " + wallet,
                    MainActivity.screenWidth/2 - width/2,
                    275 + 2*cardHeight,
                    uiPaint);
            uiPaint.setColor(Color.BLACK);

        }
    }
    private void checkForDoorway() {
        Doorway doorwayPlayerIsOn = mapManager.isPlayerOnDoorway(player.getHitbox());

        if (doorwayPlayerIsOn != null) {
            if (!doorwayJustPassed)
                mapManager.changeMap(doorwayPlayerIsOn.getDoorwayConnectedTo());

        } else
            doorwayJustPassed = false;


    }
    private boolean checkForHealStatue(){
        if (playerX >= (1018*12)+cameraX && playerX<= (1040*12)+cameraX && playerY >= (136*12)+cameraY && playerY<=(168*12)+cameraY) {
            return true;
        }

            return false;
    }

    /**
     * sets the doorway passed status to true or false
     * @param doorwayJustPassed
     */
    public void setDoorwayJustPassed(boolean doorwayJustPassed) {
        this.doorwayJustPassed = doorwayJustPassed;

    }

    /**
     * sets the position of the camera
     * @param cameraValues
     */
    public void setCameraPos(PointF cameraValues){
        this.cameraX = cameraValues.x;
        this.cameraY = cameraValues.y;
    }

    /**
     * Renders the game each frame and updated the bitmap canvas
     * @param c Bitmap canvas is placed to to be used to draw on
     * Also includes the implementation for moving to the battle state if a collision is true
     */
    @Override
    public void render(Canvas c) {
        if(mapManager.getRoomId() == 0){
            c.drawARGB(255, 51, 117, 4);
        }else {
            c.drawARGB(255, 16,24,24);
        }
        mapManager.draw(c);
        //buildingManager.draw(c);

        synchronized (enemies) {
            for (Character s : enemies) {
                drawCharacter(c, s);
            }
        }
        synchronized (players) {
            for (Map.Entry<String, Character> entry : players.entrySet()) {
                if(entry.getValue() instanceof Wizard) {
                    Wizard w = (Wizard) entry.getValue();

                        if(!mapManager.inside || w.getRoomID() == mapManager.getRoomId()){
                            drawCharacter(c, w);
                        }


                }else if (entry.getValue() instanceof Enemy ) {

                    Enemy e = (Enemy) entry.getValue();

                        if(e.getRooomID() == mapManager.getRoomId()){
                            drawCharacter(c,e);
                        }



                }
            }
        }
        drawPlayer(c);
        if(mapManager.getRoomId() == 1){
            c.drawText("IN SHOP",500 + cameraX,500 + cameraY,namePaint);
            c.drawBitmap(Icons.SHOPKEEPER.getSpriteLogo(),
                    500 + cameraX + Icons.SHOPKEEPER.getSpriteLogo().getWidth()/2,
                    500 + cameraY,null);
        }
        mapManager.inFrontEnviorments(c);
        //c.drawBitmap(Icons.LOGO.getSpriteLogo(),MainActivity.screenWidth - 950, MainActivity.screenHeight - 350,null);//example of getting a logo
        drawUI(c);
        // c.drawBitmap(Icons.LOGO.getSpriteLogo(),MainActivity.screenWidth - 950, MainActivity.screenHeight - 350,null);//example of getting a logo
        //draws joystick


        //If collide with enemy start transition to battle
        if(playerEnemeyCollide){

            Paint battleTransition = new Paint();
            battleTransition.setColor(Color.rgb(0,0,0));
            battleTransition.setStrokeWidth(rad);

            c.drawCircle(playerX,playerY,rad,battleTransition);
            rad += 50;
            if(rad >= 1500){
                playerEnemeyCollide = true;
                game.initBattleState();
                playerEnemeyCollide = false;
                setPlayerMoveFalse();
                touchDown = false;
                setPlayerMoveFalse();
                innerJoyStickX = MainActivity.screenWidth /9 + 10 - Icons.JOYSTICKOUTTER.getSpriteLogo().getWidth()/2 +Icons.JOYSTICKINNER.getSpriteLogo().getWidth()/2;
                innerJoyStickY =MainActivity.screenHeight  - MainActivity.screenHeight  / 4 + 5 - Icons.JOYSTICKOUTTER.getSpriteLogo().getHeight()/2 + Icons.JOYSTICKINNER.getSpriteLogo().getHeight()/2;
                game.setCurrentState(Game.GameState.BATTLE);
                rad = 0;

            }
        }


    }

    /**
     * not used but would help connect to a websocket based on the server url
     */
    public void startWebsocket(){
        // Establish WebSocket connection and set listener
        WebSocketManager.getInstance().setWebSocketListener(serverUrl,Playing.this);

    }

    private void drawUI(Canvas c) {


       // c.drawCircle(xCenter,yCenter,radius,circlePaint);
        c.drawBitmap(Icons.JOYSTICKOUTTER.getSpriteLogo(),
                MainActivity.screenWidth /9 + 10 - Icons.JOYSTICKOUTTER.getSpriteLogo().getWidth()/2,
                MainActivity.screenHeight  - MainActivity.screenHeight  / 4 + 5 - Icons.JOYSTICKOUTTER.getSpriteLogo().getHeight()/2,
                null);

        c.drawBitmap(Icons.JOYSTICKINNER.getSpriteLogo(),
                innerJoyStickX,
                innerJoyStickY,
                null);

        c.drawBitmap(Icons.HPTEXTICON.getSpriteLogo(),
                50,
                50,
                null);
        c.drawBitmap(Icons.MPTEXTICON.getSpriteLogo(),
                50,
                40 + Icons.HPTEXTICON.getSpriteLogo().getHeight(),
                null);
        //HP FILL

        Rect r = new Rect();
        r.left = 85 + Icons.HPTEXTICON.getSpriteLogo().getWidth();
        r.top = 65;
        hpRight = calcHpBar(wizardHp,r.left);
        r.right = Math.round(hpRight);
        r.bottom = 35 + Icons.HPBAR.getSpriteLogo().getHeight();
        c.drawRect(r, hpFill);
        //MP FILL
        Rect rm = new Rect();
        rm.left = 85 + Icons.MPTEXTICON.getSpriteLogo().getWidth();
        rm.top = 55 + Icons.MPTEXTICON.getSpriteLogo().getHeight();
        mpRight = calcMpBar(wizardMP,rm.left);
        rm.right = Math.round(mpRight);
        rm.bottom = 15 + Icons.MPBAR.getSpriteLogo().getHeight() +Icons.MPBAR.getSpriteLogo().getHeight();
        c.drawRect(rm, mpFill);
        c.drawBitmap(Icons.HPBAR.getSpriteLogo(),
                75 + Icons.HPTEXTICON.getSpriteLogo().getWidth(),
                50,
                null);
        c.drawBitmap(Icons.MPBAR.getSpriteLogo(),
                75 + Icons.HPTEXTICON.getSpriteLogo().getWidth(),
                40 + Icons.HPTEXTICON.getSpriteLogo().getHeight(),
                null);


        //FOR BUTTONS VVVV
        c.drawBitmap(
                ButtonImages.MENUBUTTON.getButtonImage(menu.isClicked()),
                menu.getHitbox().left,
                menu.getHitbox().top,
                null
        );
        c.drawBitmap(
                ButtonImages.SETTINGSBUTTON.getButtonImage(settings.isClicked()),
                settings.getHitbox().left,
                settings.getHitbox().top,
                null
        );

        //for player names
        namePaint.setTypeface(loadFont(MainActivity.getGameContext(),R.font.pressstart2pregular));
        try {
             c.drawText(wiz.getString("displayName"),
                    playerX ,
                    playerY + GameCharacters.PLAYERFIRE.getSprite(0, 0).getHeight() - 20,
                    namePaint);
            Icons icon = getIcon(wiz.getString("element"));
            float width = namePaint.measureText(wiz.getString("displayName"));
            if(icon != null)//here is the element logo
                c.drawBitmap(icon.getSpriteLogo(),
                        playerX - width/2 - Icons.FIREICON.getSpriteLogo().getWidth() -5,
                        playerY + GameCharacters.PLAYERFIRE.getSprite(0, 0).getHeight() - Icons.FIREICON.getSpriteLogo().getHeight()- 10,
                        null);
        }catch (JSONException e){
            e.printStackTrace();
        }

        handlePopUpUI(c);
        handleShop(c);


    }
    private Icons getIcon(String str){
        Icons icon = null;
        switch (str) {
            case "Fire":
                icon = Icons.FIREICON;
                break;
            case "Ice":
                icon = Icons.ICEICON;
                break;
            case "Storm":
                icon = Icons.STORMICON;
                break;
            case "Life":
                icon = Icons.LIFEICON;
                break;
            case "Death":
                icon = Icons.DEATHICON;
                break;
            case "Earth":
                icon = Icons.EARTHICON;
                break;
        }
        return icon;
    }

    /**
     * loads the pixel font
     * @param context
     * @param fontResId
     * @return
     */
    public static Typeface loadFont(Context context, int fontResId) {
        return context.getResources().getFont(fontResId);
    }
    private float calcHpBar(float hp, int left){
        float  ratio = hp/maxHp;
        float num = (left + Icons.HPBAR.getSpriteLogo().getWidth() * ratio) - 15 ;
        return num;
    }
    private float calcMpBar(float mp,int left){
        float  ratio = mp/maxMP;
        float num = (left + Icons.MPBAR.getSpriteLogo().getWidth() * ratio) - 15 ;
        return num;
    }
    private float calcXpBar(float xp,int left){
        float ratio = 1;
        if(xp < 6){
              ratio = 5/wizardNextLevel;

        }else{
              ratio = xp/wizardNextLevel;
        }
        float num = (left + Icons.MENUEXPBACK.getSpriteLogo().getWidth() * ratio) - 75 ;
        return num;
    }
    private void drawPlayer(Canvas c) {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.rgb(255,255,255));
        textPaint.setTextSize(36);

        textPaint.setTypeface(loadFont(MainActivity.getGameContext(),R.font.pressstart2pregular));
//        c.drawBitmap(Icons.SHADOW.getSpriteLogo(),
//                player.getHitbox().left-16,
//                player.getHitbox().top+64,
//                null);
        c.drawBitmap(
                player.getGameCharType().getSprite(player.getFaceDir(),player.getAniIndex()),
                player.getHitbox().left,
                player.getHitbox().top,
                null);


        //PLAYER HITBOX DEBUG
     //   c.drawRect(player.getHitbox(),redPaint);
    }
    private void drawCharacter(Canvas canvas, Character c){
        Paint textPaint = new Paint();
        textPaint.setColor(Color.rgb(0,0,0));
        textPaint.setTextSize(24);

        textPaint.setTypeface(loadFont(MainActivity.getGameContext(),R.font.pressstart2pregular));

        canvas.drawBitmap(
                c.getGameCharType().getSprite(c.getFaceDir(),c.getAniIndex()),
                c.getHitbox().left + cameraX,
                c.getHitbox().top +cameraY,
                null );


        if(c instanceof Wizard){
            //System.out.println(((Wizard) c).getName());
            canvas.drawText(((Wizard) c).getName(),
                    c.getHitbox().left + cameraX + GameCharacters.PLAYERFIRE.getSprite(0,0).getWidth()/2,
                    c.getHitbox().top + cameraY + GameCharacters.PLAYERFIRE.getSprite(0, 0).getHeight() + 40,
                    namePaint);

            Icons icon = getIcon(((Wizard) c).getElement());
            float width = namePaint.measureText(((Wizard) c).getName());
            if(icon != null)//here is the element logo
                canvas.drawBitmap(icon.getSpriteLogo(),
                        c.getHitbox().left + cameraX + GameCharacters.PLAYERFIRE.getSprite(0,0).getWidth()/2 - width/2 - Icons.FIREICON.getSpriteLogo().getWidth() -5,
                        c.getHitbox().top +cameraY + GameCharacters.PLAYERFIRE.getSprite(0, 0).getHeight() ,
                        null);
            //System.out.println(((Wizard) c).getElement());
        }


        //FOR OTHER ENTITY HITBOX
        //canvas.drawRect(c.getHitbox().left + cameraX,c.getHitbox().top +cameraY, c.getHitbox().right+cameraX,c.getHitbox().bottom + cameraY,redPaint);
//        String it = String.valueOf(c.getFaceDir());
//        canvas.drawText(
//               it,
//                c.getHitbox().left + cameraX,
//                c.getHitbox().top +cameraY,
//                textPaint
//        ); diretion facing test
    }

    /**
     * override of touch events interface
     * Checks for all in-game touch events that are implemented
     * Cases for:
     * Action Down
     * Action Up
     * Action Move
     * @param event
     */
    @Override
    public void touchEvents(MotionEvent event) {
//all for joystick logic
        switch (event.getAction()){ //doing the math for the direction
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                checkButton(x,y,menu);
                checkButton(x,y,settings);
                checkButton(x,y,closeMenu);
                checkButton(x,y,deckInv);
                checkButton(x,y,deckleft);
                checkButton(x,y,deckright);
                checkButton(x,y,mod);
                checkButton(x,y,admin);

                if(showingDeck || showingShop) {
                    checkButton(x, y, card1);
                    checkButton(x, y, card2);
                    checkButton(x, y, card3);
                    checkButton(x, y, card4);
                    checkButton(x, y, card5);
                    checkButton(x, y, card6);
                    checkButton(x, y, card7);
                    checkButton(x, y, card8);
                }
                if(showingMod){
                    checkButton(x,y,modleft);
                    checkButton(x,y,modright);
                    checkButton(x,y,modSlot1);
                    checkButton(x,y,modSlot2);
                    checkButton(x,y,modSlot3);
                }
                if(showingAdmin){
                    checkButton(x,y,adminHpUp);
                    checkButton(x,y,adminHpDown);
                    checkButton(x,y,adminRight);
                    checkButton(x,y,adminLeft);
                    checkButton(x,y,adminLevelUp);
                    checkButton(x,y,adminLevelDown);
                    checkButton(x,y,adminCard1);
                    checkButton(x,y,adminCard2);
                    checkButton(x,y,adminCard3);
                    checkButton(x,y,toggleDamage);
                }
                if(inRangeOfShop || showingShop){
                    checkButton(x,y,shopButton);
                }
               // checkButton(x,y,cardInv);

                //we using trig
                float a = Math.abs(x - xCenter);
                float b = Math.abs(y - yCenter);
                float c = (float)Math.hypot(a,b);


                if(c <= radius) {
                    touchDown = true;
                    xTouch = x;
                    yTouch = y;
                    innerJoyStickX = x - Icons.JOYSTICKINNER.getSpriteLogo().getWidth()/2;
                    innerJoyStickY = y  - Icons.JOYSTICKINNER.getSpriteLogo().getHeight()/2;
                }
                break;
            case MotionEvent.ACTION_MOVE: //actually moving char here
                if(touchDown){
                    xTouch = event.getX();
                    yTouch = event.getY();

                    //Math.hypot(x1 - x2, y1 - y2)
                    float circlex = xTouch - xCenter;
                    float circley = yTouch - yCenter;
                    if(Math.hypot(circlex,circley) <= radius){
                        innerJoyStickX = xTouch - Icons.JOYSTICKINNER.getSpriteLogo().getWidth()/2;
                        innerJoyStickY = yTouch - Icons.JOYSTICKINNER.getSpriteLogo().getHeight()/2;
                    }else{
                        circlex *= radius / Math.hypot(xCenter-xTouch,yCenter-yTouch);
                        circley *= radius / Math.hypot(xCenter-xTouch,yCenter-yTouch);
                        innerJoyStickX = xCenter + circlex - Icons.JOYSTICKINNER.getSpriteLogo().getWidth()/2;
                        innerJoyStickY = yCenter + circley - Icons.JOYSTICKINNER.getSpriteLogo().getHeight()/2;
                    }


                    float xDiff = xTouch - xCenter;
                    float yDiff = yTouch - yCenter;

                    if(!showingDeck) {
                        setPlayerMoveTrue(new PointF(xDiff, yDiff));
                    }
                }
                break;
            case MotionEvent.ACTION_UP: //stopping the movement
                touchDown = false;
                setPlayerMoveFalse();
                innerJoyStickX = MainActivity.screenWidth /9 + 10 - Icons.JOYSTICKOUTTER.getSpriteLogo().getWidth()/2 +Icons.JOYSTICKINNER.getSpriteLogo().getWidth()/2;
                innerJoyStickY =MainActivity.screenHeight  - MainActivity.screenHeight  / 4 + 5 - Icons.JOYSTICKOUTTER.getSpriteLogo().getHeight()/2 + Icons.JOYSTICKINNER.getSpriteLogo().getHeight()/2;

                menu.setClicked(false);
                settings.setClicked(false);
                closeMenu.setClicked(false);
                deckInv.setClicked(false);
                deckright.setClicked(false);
                deckleft.setClicked(false);
                mod.setClicked(false);
                admin.setClicked(false);
                modleft.setClicked(false);
                modright.setClicked(false);
                adminHpUp.setClicked(false);
                adminHpDown.setClicked(false);
                adminRight.setClicked(false);
                adminLeft.setClicked(false);
                adminLevelUp.setClicked(false);
                adminLevelDown.setClicked(false);
                shopButton.setClicked(false);
               // cardInv.setClicked(false);
                break;
        }
    }

    private void updatePlayerMove(double delta) {
        //System.out.println("DELTA: " + delta);
        if(!movePlayer)
            return;

        float baseSpeed = (float)(delta * playerSpeed);
        float ratio = Math.abs(lastTouchDiff.y) / Math.abs(lastTouchDiff.x);
        double angle = Math.atan(ratio);

        double xSpeed = Math.cos(angle);
        double ySpeed = Math.sin(angle);

        if(xSpeed > ySpeed){
            if(lastTouchDiff.x > 0)
                player.setFaceDir(GameConstants.Face_Dir.RIGHT);
            else
                player.setFaceDir(GameConstants.Face_Dir.LEFT);
        }/*else{                          <<--- this is for up and down when we have that eventually
            if(lastTouchDiff.y > 0)
                player.setFaceDir(GameConstants.Face_Dir.UP);
            else
                player.setFaceDir(GameConstants.Face_Dir.DOWN);
        }*/

        //actual movement here
        if(lastTouchDiff.x < 0){
            xSpeed *= -1;
        }
        if(lastTouchDiff.y < 0){
            ySpeed *= -1;
        }
        int pW = (int) player.getHitbox().width();
        int pH = (int) player.getHitbox().height();

        if(xSpeed <=0){
            pW= 0;
        }
        if(ySpeed <= 0){
            pH= 0;
        }

        double deltaX = xSpeed * baseSpeed * -1;
        double deltaY = ySpeed * baseSpeed * -1;
        //x and y are the actual x and y pos of the player
        if(mapManager.canMove(player.getHitbox().left+cameraX * -1+deltaX*-1+pW,player.getHitbox().top+cameraY*-1+deltaY*-1+pH)){
            cameraX += deltaX ;
            cameraY += deltaY ;
            player.setX(player.getHitbox().left+cameraX * -1+deltaX*-1);
            player.setY(player.getHitbox().top+cameraY * -1+deltaX*-1);
            MainActivity.updateXY(cameraX,cameraY);
           // sendMessage(String.format("%.2f %.2f",  player.getHitbox().left+cameraX * -1+deltaX*-1,  player.getHitbox().top+cameraY*-1+deltaY*-1));

            sendMessage(String.format("%.2f %.2f %d",
                    player.getHitbox().left+cameraX * -1+deltaX*-1,
                    player.getHitbox().top+cameraY*-1+deltaY*-1,mapManager.getRoomId()),
                    serverUrl);
        }

    }
    private void checkCollide() {
        RectF collideBoxWithoutCamer = new RectF(player.getHitbox());
        collideBoxWithoutCamer.left -= cameraX;
        collideBoxWithoutCamer.right -= cameraX;
        collideBoxWithoutCamer.top -= cameraY;
        collideBoxWithoutCamer.bottom -= cameraY;

        for(Character b : enemies){
            Enemy en = (Enemy) b;
            if(collideBoxWithoutCamer.intersects(b.getHitbox().left,b.getHitbox().top,b.getHitbox().right,b.getHitbox().bottom ) && en.getRooomID() == mapManager.getRoomId() ){
                playerEnemeyCollide = true;
            }
        }


        for (Map.Entry<String, Character> entry : players.entrySet()) {
              if (entry.getValue() instanceof Enemy){
                    Enemy b = (Enemy) entry.getValue();
                    if(collideBoxWithoutCamer.intersects(b.getHitbox().left,b.getHitbox().top,b.getHitbox().right,b.getHitbox().bottom) && b.getRooomID() == mapManager.getRoomId()){
                        playerEnemeyCollide = true;
                        MainActivity.setEnemyID(b.id);
                        System.out.println(b.id);
                    }
                }
        }


    }
    private void setPlayerMoveTrue(PointF lastTouchDiff){
        movePlayer = true;
        this.lastTouchDiff = lastTouchDiff;
    }
    private void setPlayerMoveFalse(){
        movePlayer = false;
        player.resetAnimation();
    }
    private void checkButton(float x,float y,CustomButton button){
        // bro this was the most useful debug message i have ever made
        // System.out.println("x: "+x+"y: "+y+ "bottom: "+button.getHitbox().bottom + "top: "+button.getHitbox().top +"height: " +ButtonImages.SETTINGSBUTTON.getHeight());
        if(x > button.getHitbox().left && x < button.getHitbox().right){
            if(y < button.getHitbox().bottom && y > button.getHitbox().top)
                toggleButton(button);
        }
    }
    private void toggleButton(CustomButton button){
        button.setClicked(true);
        if(button == menu && !showingAdmin && !showingMod){
            if(!showingMenu) {
                showingMenu = true;
                showingSettings = false;
                showingDeck = false;
                showingAdmin = false;
                showingMod = false;
                showingShop = false;
            }
            else
                showingMenu = false;
            return;
        }else if(button == closeMenu){
            showingSettings = false;
            showingMenu = false;
            showingDeck = false;
            showingAdmin = false;
            showingMod = false;
            showingShop = false;
            return;
        }else if(button == settings && !showingAdmin && !showingMod){
            if(!showingSettings) {
            //sendMessage("~kill philip",serverUrl);
                showingSettings = true;
                showingMenu = false;
                showingDeck = false;
                showingAdmin = false;
                showingMod = false;
                showingShop = false;

            }
            else
                showingSettings = false;
            return;
        }else if(button == deckInv && showingMenu){
            getCardInv();
            getDeck();
            if(!showingDeck) {
                showingSettings = false;
                showingMenu = false;
                showingDeck = true;
                showingAdmin = false;
                showingMod = false;
                showingShop = false;

                deckCheck = .2f;
                //SET SLOT FULL OR NOT HERE
                for(int i = 0; i < MainActivity.cardInv.size() - (8 * (cardPageIndex - 1)) ;i++){
                    for(int j = 0; j < MainActivity.deck.size();j++){
                        if(i > 7){
                            break;
                        }
                        int index = i + ((cardPageIndex-1) * 8);
                        if(MainActivity.deck.get(j).getID() == MainActivity.cardInv.get(index).getID()){
                            slots[i] = true;
                        }
                    }
                }

            }
            else
                showingDeck = false;
            return;
        } else if (button == deckleft &&  (showingDeck||showingShop)) {
            if(showingShop){
                if(shopPageIndex > 1){shopPageIndex--;}
                return;
            }
            if(cardPageIndex > 1){
                cardPageIndex--;
                for(int i = 0; i < slots.length;i++){slots[i] = false;}
                deckCheck = .2f;
                //SET SLOT FULL OR NOT HERE
                for(int i = 0; i < MainActivity.cardInv.size() - (8 * (cardPageIndex - 1)) ;i++){
                    for(int j = 0; j < MainActivity.deck.size();j++){
                        if(i > 7){
                            break;
                        }
                        int index = i + ((cardPageIndex-1) * 8);
                        if(MainActivity.deck.get(j).getID() == MainActivity.cardInv.get(index).getID()){
                            slots[i] = true;
                        }
                    }
                }
            }
            return;
        }else if (button == deckright && (showingDeck||showingShop)){
            if(showingShop){
                if(allCards.size() - (shopPageIndex * 8) > 0) {
                    shopPageIndex++;
                }
                return;
            }
            if(MainActivity.cardInv.size() - (cardPageIndex * 8) > 0){
                cardPageIndex++;
                for(int i = 0; i < slots.length;i++){slots[i] = false;}
                deckCheck = .2f;
                //SET SLOT FULL OR NOT HERE
                for(int i = 0; i < MainActivity.cardInv.size() - (8 * (cardPageIndex - 1)) ;i++){
                    for(int j = 0; j < MainActivity.deck.size();j++){
                        if(i > 7){
                            break;
                        }
                        int index = i + ((cardPageIndex-1) * 8);
                        if(MainActivity.deck.get(j).getID() == MainActivity.cardInv.get(index).getID()){
                            slots[i] = true;
                        }
                    }
                }
            }
            return;
        }else if(button == mod && showingSettings & isMod){
            if(!showingMod) {
                showingSettings = false;
                showingMenu = false;
                showingDeck = false;
                showingShop = false;
                showingAdmin = false;
                showingMod = true;

            }
            else
                showingMod = false;
        }else if(button == admin && showingSettings && isAdmin){
            if(!showingAdmin) {
                showingSettings = false;
                showingMenu = false;
                showingDeck = false;
                showingShop = false;
                showingAdmin = true;
                showingMod = false;

            }
            else
                showingAdmin = false;
        }else if(button == shopButton && !showingShop && !showingMenu && !showingDeck && !showingMod && !showingAdmin){
            if(!showingShop) {
                showingSettings = false;
                showingMenu = false;
                showingDeck = false;
                showingAdmin = false;
                showingMod = false;
                showingShop = true;
                System.out.println("SHOP BUTTON");

            }else{
                showingShop = false;
            }

        }

        if((showingDeck || showingShop) && !showingMenu){
            if (button == (card1)) {
                if(showingShop){
                    buyCard(allCards.get(0 + ((shopPageIndex - 1)*8)).getID());
                    return;
                }
                if(!slots[0]){
                    if(MainActivity.deck.size() >= 20){
                        return;
                    }
                    addCard(MainActivity.cardInv.get(0 + ((cardPageIndex - 1)*8)).getID());
                    slots[0] = true;
                }else{

                    removeCard(MainActivity.cardInv.get(0 + ((cardPageIndex- 1)*8)).getID());
                    slots[0] = false;
                }
            }else if (button.equals(card2)) {
                if(showingShop){
                    buyCard(allCards.get(1 + ((shopPageIndex - 1)*8)).getID());
                    return;
                }
                if(!slots[1]){
                    if(MainActivity.deck.size() >= 20){
                        return;
                    }
                    addCard(MainActivity.cardInv.get(1 + ((cardPageIndex - 1)*8)).getID());
                    slots[1] = true;
                }else{
                    removeCard(MainActivity.cardInv.get(1 + ((cardPageIndex- 1)*8)).getID());
                    slots[1] = false;
                }
                System.out.print("TAPPED CARD 2 AAAAAAAAAAAAA");
            }else if (button.equals(card3)) {
                if(showingShop){
                    buyCard(allCards.get(2 + ((shopPageIndex - 1)*8)).getID());
                    return;
                }
                if(!slots[2]){
                    if(MainActivity.deck.size() >= 20){
                        return;
                    }
                    addCard(MainActivity.cardInv.get(2 + ((cardPageIndex - 1)*8)).getID());
                    slots[2] = true;
                }else{
                    removeCard(MainActivity.cardInv.get(2 + ((cardPageIndex- 1)*8)).getID());
                    slots[2] = false;
                }
                System.out.print("TAPPED CARD 3 AAAAAAAAAAAAA");
            }else if (button.equals(card4)) {
                if(showingShop){
                    buyCard(allCards.get(3 + ((shopPageIndex - 1)*8)).getID());
                    return;
                }
                if(!slots[3]){
                    if(MainActivity.deck.size() >= 20){
                        return;
                    }
                    addCard(MainActivity.cardInv.get(3 + ((cardPageIndex - 1)*8)).getID());
                    slots[3] = true;
                }else{
                    removeCard(MainActivity.cardInv.get(3 + ((cardPageIndex- 1)*8)).getID());
                    slots[3] = false;
                }
                System.out.print("TAPPED CARD 4 AAAAAAAAAAAAA");
            }else if (button.equals(card5)) {
                if(showingShop){
                    buyCard(allCards.get(4 + ((shopPageIndex - 1)*8)).getID());
                    return;
                }
                if(!slots[4]){
                    if(MainActivity.deck.size() >= 20){
                        return;
                    }
                    addCard(MainActivity.cardInv.get(4 + ((cardPageIndex - 1)*8)).getID());
                    slots[4] = true;
                }else{
                    removeCard(MainActivity.cardInv.get(4 + ((cardPageIndex- 1)*8)).getID());
                    slots[4] = false;
                }
                System.out.print("TAPPED CARD 5 AAAAAAAAAAAAA");
            }else if (button.equals(card6)) {
                if(showingShop){
                    buyCard(allCards.get(5 + ((shopPageIndex - 1)*8)).getID());
                    return;
                }
                if(!slots[5]){
                    if(MainActivity.deck.size() >= 20){
                        return;
                    }
                    addCard(MainActivity.cardInv.get(5 + ((cardPageIndex - 1)*8)).getID());
                    slots[5] = true;
                }else{
                    removeCard(MainActivity.cardInv.get(5 + ((cardPageIndex- 1)*8)).getID());
                    slots[5] = false;
                }
                System.out.print("TAPPED CARD 6 AAAAAAAAAAAAA");
            }else if (button.equals(card7)) {
                if(showingShop){
                    buyCard(allCards.get(6 + ((shopPageIndex - 1)*8)).getID());
                    return;
                }
                if(!slots[6]){
                    if(MainActivity.deck.size() >= 20){
                        return;
                    }
                    addCard(MainActivity.cardInv.get(6 + ((cardPageIndex - 1)*8)).getID());
                    slots[6] = true;
                }else{
                    removeCard(MainActivity.cardInv.get(6 + ((cardPageIndex- 1)*8)).getID());
                    slots[6] = false;
                }
                System.out.print("TAPPED CARD 7 AAAAAAAAAAAAA");
            }else if (button.equals(card8)) {
                if(showingShop){
                    buyCard(allCards.get(7 + ((shopPageIndex - 1)*8)).getID());
                    return;
                }
                if(!slots[7]){
                    if(MainActivity.deck.size() >= 20){
                        return;
                    }
                    addCard(MainActivity.cardInv.get(7 + ((cardPageIndex - 1)*8)).getID());
                    slots[7] = true;
                }else{
                    removeCard(MainActivity.cardInv.get(7 + ((cardPageIndex- 1)*8)).getID());
                    slots[7] = false;
                }
                System.out.print("TAPPED CARD 8 AAAAAAAAAAAAA");
            }
        }
        if(showingMod){
            if (button == modleft) {
                if(modPageIndex > 1){
                    modPageIndex--;
                }
                return;
            }else if (button == modright ){
                int num = 0;
                for (Map.Entry<String, Character> entry : players.entrySet()) {
                    if (entry.getValue() instanceof Wizard) {
                        num++;
                    }
                }
                if(num- (modPageIndex * 8) > 0){
                    modPageIndex++;

                }
                return;
            }
            else if(button == modSlot1){
                System.out.println("CLICKED MOD 1");
                int index = -1;
                for (Map.Entry<String, Character> entry : players.entrySet()) {
                    if (entry.getValue() instanceof Wizard) {
                        Wizard w = (Wizard) entry.getValue();
                        index++;
                        if(index == 0 + ((modPageIndex - 1) * 3)){
                            kickPlayer(w.getName());
                        }
                    }

                }
            }else if(button == modSlot2){
                System.out.println("CLICKED MOD 2");
                int index = -1;
                for (Map.Entry<String, Character> entry : players.entrySet()) {
                    if (entry.getValue() instanceof Wizard) {
                        Wizard w = (Wizard) entry.getValue();
                        index++;
                        if(index == 1 + ((modPageIndex - 1) * 3)){
                            kickPlayer(w.getName());
                        }
                    }

                }

            }else if(button == modSlot3){
                System.out.println("CLICKED MOD 3");
                int index = -1;
                for (Map.Entry<String, Character> entry : players.entrySet()) {
                    if (entry.getValue() instanceof Wizard) {
                        Wizard w = (Wizard) entry.getValue();
                        index++;
                        if(index == 2 + ((modPageIndex - 1) * 3)){
                            kickPlayer(w.getName());
                        }
                    }

                }

            }
        }
        if(showingAdmin){
            if(button == adminHpUp)
                adminHp(true);
            else if (button == adminHpDown)
                adminHp(false);
            else if(button == adminLevelDown){
                adminLvl(false);
            }else if(button == adminLevelUp){
                adminLvl(true);
            }else if(button == adminLeft){
                if(adminPageIndex > 1) {
                    adminPageIndex--;
                }
            }else if(button == adminRight){
                if(adminPageIndex < players.size()/3) {
                    adminPageIndex++;
                }
            }else if(button == adminCard1){
                System.out.println("CLICKING CARD 1");
                int index = 0 + ((adminPageIndex-1) * 3);
                toggleCardDamage(MainActivity.cardInv.get(index).getID(),increaseDamageToggle);
            }else if(button == adminCard2){
                int index = 1 + ((adminPageIndex-1) * 3);
                toggleCardDamage(MainActivity.cardInv.get(index).getID(),increaseDamageToggle);
            }else if(button == adminCard3){
                int index = 2 + ((adminPageIndex-1) * 3);
                toggleCardDamage(MainActivity.cardInv.get(index).getID(),increaseDamageToggle);

            }else if(button == toggleDamage){
                increaseDamageToggle = !increaseDamageToggle;
            }

        }
    }
    private void toggleCardDamage(int id, boolean increase){
        String urlStr = "http://coms-309-032.class.las.iastate.edu:8080/cards/";
        urlStr += id;
        Card card = null;
        for(Card c: MainActivity.cardInv){
            if(c.getID() == id){
                if (increase) {
                    c.setDamage((int) Math.round(c.getDamage() + 10));
                } else {
                    c.setDamage((int) Math.round(c.getDamage() - 10));
                }
                card = c;
            }
        }
        for(Card c: MainActivity.deck){
            if(c.getID() == id){
                if (increase) {
                    c.setDamage((int) Math.round(c.getDamage() + 10));
                } else {
                    c.setDamage((int) Math.round(c.getDamage() - 10));
                }
                card = c;
            }
        }
        JSONObject cardJson = new JSONObject();
        try {
            cardJson.put("id", id);
            cardJson.put("extension", card.getExtension());
            cardJson.put("spell_name", card.getName());
            cardJson.put("element", card.getElement());
            cardJson.put("attack_power", card.getDamage());
            cardJson.put("mana_cost", card.getManaCost());
            cardJson.put("attack_type", card.getType());
            //System.out.println(cardJson.toString());
        }catch(JSONException e){e.printStackTrace();}

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.PUT, urlStr, cardJson, response -> {
        }, error -> System.out.println( "UPDATE DAMAGE ERROR " + error));
        queue.add(jsonArrayRequest);
        getCardInv();
        getDeck();
    }
    private void addCard(int cardId){

        String urlStr = "http://coms-309-032.class.las.iastate.edu:8080/CardInventory/Deck/";
        try {
            urlStr += wiz.getInt("id") + "/" +cardId;
        }catch (JSONException e){
            e.printStackTrace();
        }

        String finalUrlStr = urlStr;
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.PUT, urlStr, null, response -> {
            System.out.print("ADDING CARD TO DECK!!!!@!@#!@");
        }, error -> System.out.println(finalUrlStr + " ADD ERROR " + error));
        queue.add(jsonArrayRequest);
        for(int i = 0; i  < MainActivity.cardInv.size();i++){
            if( MainActivity.cardInv.get(i).getID() == cardId){
                MainActivity.deck.add(MainActivity.cardInv.get(i));
            }
        }
    }
    private void removeCard(int cardId){
        String urlStr = "http://coms-309-032.class.las.iastate.edu:8080/Deck/remove/";
        try {
            urlStr += wiz.getInt("id") + "/" +cardId;
        }catch (JSONException e){
            e.printStackTrace();
        }

        String finalUrlStr = urlStr;
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.PUT, urlStr, null, response -> {
            System.out.print("REMOVING CARD TO DECK!!!!@!@#!@");
        }, error -> System.out.println(finalUrlStr + " REMOVE ERROR " + error));
        queue.add(jsonArrayRequest);
        for(int i = 0;i < MainActivity.deck.size();i++){
            if(cardId == MainActivity.deck.get(i).getID()){
                MainActivity.deck.remove(i);
            }
        }
    }
    private void buyCard(int cardId){
        for(int j = 0; j < MainActivity.cardInv.size();j++){
            if(cardId == MainActivity.cardInv.get(j).getID()){
                return; //card already bought
            }
        }

        String urlStr = "http://coms-309-032.class.las.iastate.edu:8080/cards/" + cardId;

        String finalUrlStr = urlStr;
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, urlStr, null, response -> {
            System.out.print("BUYING CARD AND PUTTING INTO INV!!!!@!@#!@");
            int cost = 99999999;
            try {
                 cost = response.getInt("attack_power") * MainActivity.cardInv.size();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(wallet >= cost){
                System.out.println("WALLET BEFORE COST: " + wallet + "COST: " + cost);
                wallet -= cost;
                setWizWallet();
                addCardToInv(cardId);
                System.out.println("WALLET AFTER COST: " + wallet + "COST: " + cost);

            }
        }, error -> System.out.println(finalUrlStr + " BUY ERROR ERROR " + error));
        queue.add(jsonArrayRequest);
    }
    private void addCardToInv(int cardId){
        String urlStr = "http://coms-309-032.class.las.iastate.edu:8080/CardInventory/add/";
        try {
            urlStr += wiz.getInt("id") + "/" +cardId;
        }catch (JSONException e){
            e.printStackTrace();
        }
        String finalUrlStr = urlStr;
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, urlStr, null, response -> {
            System.out.print("actually adding to inv here");
            MainActivity.cardInv = getCardInv();
        }, error -> System.out.println(finalUrlStr + " ADD TO INV ERROR " + error));
        queue.add(jsonArrayRequest);
    }
    private void handlePopUpUI(Canvas c){
        Paint p = new Paint();
        p.setColor(Color.DKGRAY);
        if(showingMenu){

            c.drawBitmap(Icons.MENUBACK.getSpriteLogo(),
                    MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2,
                    50,null);
            c.drawBitmap(ButtonImages.CLOSEMENUBUTTON.getButtonImage(closeMenu.isClicked()),
                    closeMenu.getHitbox().left,
                    closeMenu.getHitbox().top, null);
            c.drawBitmap(Icons.MENUWIZARDFRAME.getSpriteLogo(),
                    MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 +  Icons.MENUWIZARDFRAME.getSpriteLogo().getHeight()/4,
                    50 + Icons.MENUWIZARDFRAME.getSpriteLogo().getWidth()/4,null);
            c.drawBitmap(playerSprite,
                    MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 +  Icons.MENUWIZARDFRAME.getSpriteLogo().getHeight()/4 + Icons.MENUWIZARDFRAME.getSpriteLogo().getHeight()/2 - GameCharacters.PLAYERFIRE.getSprite(0,0).getWidth()/2,
                    50 + Icons.MENUWIZARDFRAME.getSpriteLogo().getWidth()/4 + Icons.MENUWIZARDFRAME.getSpriteLogo().getHeight()/2 - GameCharacters.PLAYERFIRE.getSprite(0,0).getWidth()/2,null);
            c.drawBitmap(Icons.MENUNAMEBACK.getSpriteLogo(),
                    MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 +  Icons.MENUWIZARDFRAME.getSpriteLogo().getHeight()/4 + Icons.MENUWIZARDFRAME.getSpriteLogo().getWidth()+  50,
                    50 + Icons.MENUWIZARDFRAME.getSpriteLogo().getWidth()/4,null);
            c.drawBitmap(Icons.MENUSTATBACK.getSpriteLogo(),
                    MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 +  Icons.MENUWIZARDFRAME.getSpriteLogo().getHeight()/4 + Icons.MENUWIZARDFRAME.getSpriteLogo().getWidth()+  50,
                    75 + Icons.MENUWIZARDFRAME.getSpriteLogo().getWidth()/4 + Icons.MENUNAMEBACK.getSpriteLogo().getHeight(),null);
            c.drawBitmap(ButtonImages.MENUDECKBUTTON.getButtonImage(deckInv.isClicked()),
                    deckInv.getHitbox().left,
                    deckInv.getHitbox().top,
                    null);
//            c.drawBitmap(ButtonImages.MENUDECKBUTTON.getButtonImage(cardInv.isClicked()),
//                    cardInv.getHitbox().left,
//                    cardInv.getHitbox().top,
//                    null);
            c.drawBitmap(Icons.MENUBUTTONTEXT.getSpriteLogo(),
                    deckInv.getHitbox().left - Math.round(Icons.MENUBUTTONTEXT.getSpriteLogo().getWidth()/33),
                    25 + deckInv.getHitbox().top + ButtonImages.MENUDECKBUTTON.getHeight() ,null);
//            c.drawBitmap(Icons.MENUBUTTONTEXT.getSpriteLogo(),
//                    cardInv.getHitbox().left - Math.round(Icons.MENUBUTTONTEXT.getSpriteLogo().getWidth()/33),
//                    25 + deckInv.getHitbox().top + ButtonImages.MENUDECKBUTTON.getHeight() ,null);
            c.drawBitmap(Icons.MENUEXPBACK.getSpriteLogo(),
                    MainActivity.screenWidth/2 - Icons.MENUEXPBACK.getSpriteLogo().getWidth() /2,
                    50 + Icons.MENUBACK.getSpriteLogo().getHeight() - Math.round(Icons.MENUEXPBACK.getSpriteLogo().getHeight() * 1.5),null);
            Rect r = new Rect();
            r.left = 45+ MainActivity.screenWidth/2 - Icons.MENUEXPBACK.getSpriteLogo().getWidth() /2;
            r.top = (int) (75 + Icons.MENUBACK.getSpriteLogo().getHeight() - Math.round(Icons.MENUEXPBACK.getSpriteLogo().getHeight() * 1.5));
            float xpRight = calcXpBar(wizardXp,r.left);
            r.right = Math.round(xpRight);
            r.bottom = r.top + Icons.MENUEXPBACK.getSpriteLogo().getHeight() - 60;
            c.drawRect(r, xpFill);
            //ALL TEXT DOWN HERE
            try {
                Rect bounds = new Rect();
                uiPaint.getTextBounds(wiz.getString("displayName"), 0, wiz.getString("displayName").length(), bounds);
                int height = bounds.height();
                uiPaint.setTextSize(MainActivity.screenWidth/52);
                c.drawText(wiz.getString("displayName"),
                        90 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 +  Icons.MENUWIZARDFRAME.getSpriteLogo().getHeight()/4 + Icons.MENUWIZARDFRAME.getSpriteLogo().getWidth(),
                        50 + Icons.MENUWIZARDFRAME.getSpriteLogo().getWidth()/4 + height/2 + Icons.MENUNAMEBACK.getSpriteLogo().getHeight()/2,
                        uiPaint);
                uiPaint.setTextSize(MainActivity.screenWidth/78);
                c.drawText(wiz.get("currhp") +"/" + wiz.get("maxHP") + "HP "+ wiz.get("curr_mana") + "/" + wiz.get("max_mana") +"MP",
                        90 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 +  Icons.MENUWIZARDFRAME.getSpriteLogo().getHeight()/4 + Icons.MENUWIZARDFRAME.getSpriteLogo().getWidth(),
                        75 + Icons.MENUWIZARDFRAME.getSpriteLogo().getWidth()/4 + Icons.MENUNAMEBACK.getSpriteLogo().getHeight() + height/2 + Icons.MENUNAMEBACK.getSpriteLogo().getHeight()/2,
                        uiPaint);
                uiPaint.setTextAlign(Paint.Align.CENTER);
                c.drawText("Build Deck",
                        deckInv.getHitbox().left + Icons.MENUBUTTONTEXT.getSpriteLogo().getWidth()/2,
                        25 + deckInv.getHitbox().top + ButtonImages.MENUDECKBUTTON.getHeight()+ Icons.MENUBUTTONTEXT.getSpriteLogo().getHeight()/2 + height/2,
                        uiPaint);
//                c.drawText("Card Inv",
//                        cardInv.getHitbox().left + Icons.MENUBUTTONTEXT.getSpriteLogo().getWidth()/2,
//                        25 + deckInv.getHitbox().top + ButtonImages.MENUDECKBUTTON.getHeight() + Icons.MENUBUTTONTEXT.getSpriteLogo().getHeight()/2 + height/2,
//                        uiPaint);
                //5tguiPaint.setColor(Color.WHITE);
                c.drawText(wizardXp + "/" + wizardNextLevel +" XP",
                        MainActivity.screenWidth/2,
                        50 + Icons.MENUBACK.getSpriteLogo().getHeight() - Math.round(Icons.MENUEXPBACK.getSpriteLogo().getHeight() * 1.5) + Icons.MENUEXPBACK.getSpriteLogo().getHeight()/2+height/2,
                        uiPaint);
                c.drawText("Level: " + wiz.getInt("lvl"),
                        MainActivity.screenWidth/2,
                        Icons.MENUBACK.getSpriteLogo().getHeight() - Math.round(Icons.MENUEXPBACK.getSpriteLogo().getHeight() * 1.5) + Icons.MENUEXPBACK.getSpriteLogo().getHeight()/2+height/2 -50,
                        uiPaint);
                uiPaint.setColor(Color.BLACK);
                uiPaint.setTextAlign(Paint.Align.LEFT);


            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        if(showingSettings){
            c.drawBitmap(Icons.MENUBACK.getSpriteLogo(),
                    MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 ,
                    50 ,null);
            c.drawBitmap(ButtonImages.CLOSEMENUBUTTON.getButtonImage(closeMenu.isClicked()),
                    closeMenu.getHitbox().left,
                    closeMenu.getHitbox().top, null);

            c.drawBitmap(ButtonImages.MODBUTTON.getButtonImage(mod.isClicked()),
                    mod.getHitbox().left,mod.getHitbox().top,null);
            c.drawBitmap(ButtonImages.ADMINBUTTON.getButtonImage(admin.isClicked()),
                    admin.getHitbox().left,admin.getHitbox().top, null);
            c.drawText("setting",500,500,namePaint);
        }
        if(showingDeck){
            c.drawBitmap(Icons.MENUBACK.getSpriteLogo(),
                    MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 ,
                    50 ,null);
            c.drawBitmap(ButtonImages.CLOSEMENUBUTTON.getButtonImage(closeMenu.isClicked()),
                    closeMenu.getHitbox().left,
                    closeMenu.getHitbox().top, null);
            //c.drawText("deckinv",500,500,namePaint);
            //render cards here, seems not the best idea to do it with a for loop but should be find tbh
            float cardWidth = CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f));
            float cardHeight = CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f));
            for(int i = 0; i < MainActivity.cardInv.size()- (8 * (cardPageIndex - 1)) ;i++){
                if(i > 7){
                    break;
                }
                int index = i + ((cardPageIndex-1) * 8);
                if(i >  4){
                    MainActivity.cardInv.get(index).draw(c, MainActivity.screenWidth * (1.0f/3420.0f) );
                    MainActivity.cardInv.get(index).setPosition(
                            60 + cardWidth/2 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 + (((i-4) * cardWidth) + ((i-4) * cardWidth/8)),
                            200 + cardHeight/2 + cardHeight,
                            MainActivity.screenWidth * (1.0f/3120.0f));
                }else{
                    MainActivity.cardInv.get(index).draw(c, MainActivity.screenWidth * (1.0f/3420.0f) );
                    MainActivity.cardInv.get(index).setPosition(
                            60 + cardWidth/2 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 + ((i * cardWidth) + (i * cardWidth/8)),
                            100 + cardHeight/2,
                            MainActivity.screenWidth * (1.0f/3120.0f));
                }


            }

            for(int i = 0; i < slots.length; i++){
                if(!slots[i]){
                    switch(i){
                        case 0:
                            if(MainActivity.cardInv.size() - (8 * (cardPageIndex - 1)) > 0) {
                                c.drawRect(card1.getHitbox().left,
                                        card1.getHitbox().top,
                                        card1.getHitbox().right,
                                        card1.getHitbox().bottom,
                                        gray);
                            }
                            break;
                        case 1:
                            if(MainActivity.cardInv.size() - (8 * (cardPageIndex - 1)) > 1)
                                c.drawRect(card2.getHitbox().left,
                                    card2.getHitbox().top,
                                    card2.getHitbox().right,
                                    card2.getHitbox().bottom,
                                    gray);
                            break;
                            case 2:
                                if(MainActivity.cardInv.size() - (8 * (cardPageIndex - 1)) > 2)
                                    c.drawRect(card3.getHitbox().left,
                                        card3.getHitbox().top,
                                        card3.getHitbox().right,
                                        card3.getHitbox().bottom,
                                        gray);
                            break;
                            case 3:
                                if(MainActivity.cardInv.size() - (8 * (cardPageIndex - 1)) > 3)
                                    c.drawRect(card4.getHitbox().left,
                                        card4.getHitbox().top,
                                        card4.getHitbox().right,
                                        card4.getHitbox().bottom,
                                        gray);
                            break;
                            case 4:
                                if(MainActivity.cardInv.size() - (8 * (cardPageIndex - 1)) > 4)
                                    c.drawRect(card5.getHitbox().left,
                                        card5.getHitbox().top,
                                        card5.getHitbox().right,
                                        card5.getHitbox().bottom,
                                        gray);
                            break;
                            case 5:
                                if(MainActivity.cardInv.size() - (8 * (cardPageIndex - 1)) > 5)
                                    c.drawRect(card6.getHitbox().left,
                                        card6.getHitbox().top,
                                        card6.getHitbox().right,
                                        card6.getHitbox().bottom,
                                        gray);
                            break;
                            case 6:
                                if(MainActivity.cardInv.size() - (8 * (cardPageIndex - 1)) > 6)

                                    c.drawRect(card7.getHitbox().left,
                                        card7.getHitbox().top,
                                        card7.getHitbox().right,
                                        card7.getHitbox().bottom,
                                        gray);
                            break;
                            case 7:
                                if(MainActivity.cardInv.size() - (8 * (cardPageIndex - 1)) > 7)

                                    c.drawRect(card8.getHitbox().left,
                                        card8.getHitbox().top,
                                        card8.getHitbox().right,
                                        card8.getHitbox().bottom,
                                        gray);
                            break;
                    }
                }
            }

            c.drawBitmap(ButtonImages.DECKRIGHTARROW.getButtonImage(deckright.isClicked()),
                    deckright.getHitbox().left,deckright.getHitbox().top,null);
            c.drawBitmap(ButtonImages.DECKLEFTARROW.getButtonImage(deckleft.isClicked()),
                    deckleft.getHitbox().left,deckleft.getHitbox().top,null);
        }

        if(showingMod){
            c.drawBitmap(Icons.MENUBACK.getSpriteLogo(),
                    MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 ,
                    50 ,null);
            c.drawBitmap(ButtonImages.CLOSEMENUBUTTON.getButtonImage(closeMenu.isClicked()),
                    closeMenu.getHitbox().left,
                    closeMenu.getHitbox().top, null);
            c.drawText("mod",500,500,namePaint);

            c.drawBitmap(ButtonImages.DECKRIGHTARROW.getButtonImage(modright.isClicked()),
                    modright.getHitbox().left,modright.getHitbox().top,null);
            c.drawBitmap(ButtonImages.DECKLEFTARROW.getButtonImage(modleft.isClicked()),
                    modleft.getHitbox().left,modleft.getHitbox().top,null);
            int index = 0;
//            c.drawRect(modSlot1.getHitbox().left,modSlot1.getHitbox().top,modSlot1.getHitbox().right,modSlot1.getHitbox().bottom,redPaint);
//            c.drawRect(modSlot1.getHitbox().left,modSlot2.getHitbox().top,modSlot1.getHitbox().right,modSlot2.getHitbox().bottom,redPaint);
//            c.drawRect(modSlot1.getHitbox().left,modSlot3.getHitbox().top,modSlot1.getHitbox().right,modSlot3.getHitbox().bottom,redPaint);
            for (Map.Entry<String, Character> entry : players.entrySet()) {
                if (entry.getValue() instanceof Wizard) {
                    if(index >= 3 + (modPageIndex - 1) * 3){
                            return;
                    }
                    if(index >= (modPageIndex - 1) * 3) {
                        Wizard w = (Wizard) entry.getValue();
                        c.drawBitmap(w.getCharacter().getSprite(0, 0),
                                50 + w.getCharacter().getSprite(0, 0).getWidth() + MainActivity.screenWidth / 2 - Icons.MENUBACK.getSpriteLogo().getWidth() / 2,
                                100 + (((index - ((modPageIndex - 1) * 3)) * w.getCharacter().getSprite(0, 0).getHeight()) + 50),
                                null);
                        c.drawBitmap(Icons.MENUNAMEBACK.getSpriteLogo(),
                                100 + (2* w.getCharacter().getSprite(0, 0).getWidth()) + MainActivity.screenWidth / 2 - Icons.MENUBACK.getSpriteLogo().getWidth() / 2,
                                100 + (((index - ((modPageIndex - 1) * 3)) * w.getCharacter().getSprite(0, 0).getHeight()) + 50),
                        null);
                        Rect bounds = new Rect();
                        uiPaint.getTextBounds(w.getName(), 0, w.getName().length(), bounds);
                        int height = bounds.height();
                        uiPaint.setTextSize(MainActivity.screenWidth/62);
                        c.drawText(w.getName(),
                                150 + (2* w.getCharacter().getSprite(0, 0).getWidth()) + MainActivity.screenWidth / 2 - Icons.MENUBACK.getSpriteLogo().getWidth() / 2,
                                100 + (((index - ((modPageIndex - 1) * 3)) * w.getCharacter().getSprite(0, 0).getHeight()) + 50) + height/2 + Icons.MENUNAMEBACK.getSpriteLogo().getHeight()/2,
                                uiPaint);

                    }
                    index++;
                }

            }
        }
        if(showingAdmin){
            c.drawBitmap(Icons.MENUBACK.getSpriteLogo(),
                    MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 ,
                    50 ,null);
            c.drawBitmap(ButtonImages.CLOSEMENUBUTTON.getButtonImage(closeMenu.isClicked()),
                    closeMenu.getHitbox().left,
                    closeMenu.getHitbox().top, null);
            c.drawBitmap(ButtonImages.DECKRIGHTARROW.getButtonImage(adminHpUp.isClicked()),
                    adminHpUp.getHitbox().left,
                    adminHpUp.getHitbox().top, null);
            c.drawBitmap(ButtonImages.DECKLEFTARROW.getButtonImage(adminHpDown.isClicked()),
                    adminHpDown.getHitbox().left,
                    adminHpDown.getHitbox().top, null);
            c.drawBitmap(ButtonImages.DECKRIGHTARROW.getButtonImage(adminLevelUp.isClicked()),
                    adminLevelUp.getHitbox().left,
                    adminLevelUp.getHitbox().top, null);
            c.drawBitmap(ButtonImages.DECKLEFTARROW.getButtonImage(adminLevelDown.isClicked()),
                    adminLevelDown.getHitbox().left,
                    adminLevelDown.getHitbox().top, null);
            c.drawBitmap(ButtonImages.DECKLEFTARROW.getButtonImage(adminLeft.isClicked()),
                    adminLeft.getHitbox().left,
                    adminLeft.getHitbox().top, null);
            c.drawBitmap(ButtonImages.DECKRIGHTARROW.getButtonImage(adminRight.isClicked()),
                    adminRight.getHitbox().left,
                    adminRight.getHitbox().top, null);
            c.drawBitmap(Icons.MENUBUTTONTEXT.getSpriteLogo(),
                    toggleDamage.getHitbox().left,
                    toggleDamage.getHitbox().top,null);
            float cardWidth = CardIcons.cardBoarders.getSprite(0,1).getWidth() * (MainActivity.screenWidth * (1.0f/3420.0f));
            float cardHeight = CardIcons.cardBoarders.getSprite(0,1).getHeight() * (MainActivity.screenWidth * (1.0f/3420.0f));
            for(int i = 0; i < MainActivity.cardInv.size()- (3 * (adminPageIndex - 1)) ;i++){
                if(i > 2){
                    break;
                }
                int index = i + ((adminPageIndex-1) * 3);
                    MainActivity.cardInv.get(index).draw(c, MainActivity.screenWidth * (1.0f/3420.0f) );
                    MainActivity.cardInv.get(index).setPosition(
                            60 + cardWidth/2 + MainActivity.screenWidth/2 - Icons.MENUBACK.getSpriteLogo().getWidth() /2 + (((i+1) * cardWidth) + ((i+1) * cardWidth/8)),
                            200 + cardHeight/2 + cardHeight,
                            MainActivity.screenWidth * (1.0f/3120.0f));
            }

            try{
                uiPaint.setTextSize(MainActivity.screenWidth/52);
                uiPaint.setTextAlign(Paint.Align.CENTER);
                c.drawText("Hp: " + wiz.getInt("currhp"),
                        MainActivity.screenWidth/2,
                        200,uiPaint);
                c.drawText("Level:" + wiz.getInt("lvl"),
                        MainActivity.screenWidth/2,
                        450,uiPaint);
                if(increaseDamageToggle) {
                    c.drawText("+damage",
                            toggleDamage.getHitbox().left + Icons.MENUBUTTONTEXT.getSpriteLogo().getWidth() / 2,
                            toggleDamage.getHitbox().top + Icons.MENUBUTTONTEXT.getSpriteLogo().getHeight() / 2, uiPaint);

                }else{
                    c.drawText("-damage",
                            toggleDamage.getHitbox().left + Icons.MENUBUTTONTEXT.getSpriteLogo().getWidth() / 2,
                            toggleDamage.getHitbox().top + Icons.MENUBUTTONTEXT.getSpriteLogo().getHeight() / 2, uiPaint);
                }
                uiPaint.setTextAlign(Paint.Align.LEFT);
            }catch(JSONException e){e.printStackTrace();}



        }
    }
    private void kickPlayer(String name){
        sendMessage("~kill " + name,serverUrl);
    }
    private void adminHp(boolean add) {
        String newUrl = Const.URL_WIZARDS_JSON_ARRAY;
        try {
            int hp = wiz.getInt("currhp");
            int max = wiz.getInt("maxHP");
            if(add) {
                wiz.put("currhp", hp + 25);
                if(hp + 25 > max) {
                    wiz.put("maxHP", hp + 25);
                }
            }
            else {
                if(hp - 25 < 0){
                    wiz.put("currhp", 0);
                    wiz.put("maxHP",100);
                }else {
                    wiz.put("currhp", hp - 25);
                }
            }
            wiz.put("wallet",wallet + 1000);
            wallet = wiz.getInt("wallet");

            newUrl += "/"+wiz.getString("id");
            wizardHp = wiz.getInt("currhp");
            maxHp = wiz.getInt("maxHP");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.PUT, newUrl, wiz, response -> {

        }, error -> System.out.println("UPDATE ERROR"));

        queue.add(jsonArrayRequest);
    }
    private void adminLvl(boolean add) {
        String newUrl = Const.URL_WIZARDS_JSON_ARRAY;
        try {
            newUrl += "/"+wiz.getString("id");

            int lvl = wiz.getInt("lvl");
            if(add){
                wiz.put("lvl", lvl + 1);
            }else{
                if(lvl > 1){
                    wiz.put("lvl",lvl -1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.PUT, newUrl, wiz, response -> {

        }, error -> System.out.println("UPDATE ERROR"));

        queue.add(jsonArrayRequest);
    }

    private void setPictureBitmap(Bitmap src) {
        Bitmap mPictureBitmap = src;
        BitmapShader mBitmapShader = new BitmapShader(mPictureBitmap,
                Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT);

        mPaintShader.setShader(mBitmapShader);
    }


    /**
     * Websocket close, this is called on the close of a websocket and provides the reason
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
System.out.println(reason);
    }

    /**
     * Websocket error , prints out the error if occurs
     * @param ex The exception that describes the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {
        ex.printStackTrace();
    }

    /**
     * set up to send a message from the websocket manager
     * @param message
     */
    public static void sendMessage(String message, String url){
        try {
            // send message
            WebSocketManager.getInstance().sendMessage(url,message);
        } catch (Exception e) {
            Log.d("ExceptionSendMessage:", e.getMessage().toString());
        }
    }
    //On personal open websocket not others

    /**
     * Runs on websocket open
     * @param handshakedata Information about the server handshake.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        System.out.println("Hello");

    }


    //receiver


    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Receiver for all /player websocket messages
     * @param message The received WebSocket message.
     * Handles all cases for websockets messages from the /player Websocket
     */
    @Override
    public void onWebSocketMessage(String message) {
        ObjectMapper objectMapper = new ObjectMapper();

        if (message.startsWith("[onClose]")) {
            String player = message.substring("[onClose]".length()).trim();
            System.out.println(player);
            players.remove(player);
            System.out.println("removed");
            System.out.println(players);
            return;
        }
        if (message.equals("!%SHUTDOWN0987654321%!")) {
            System.out.println(message);
            System.out.println("PLAYER KICKED");
            MainActivity.playerKicked();
        }

        if (message.startsWith("Joined")) {
            String json = message.substring("Joined".length());
            try {
                JsonNode jsonNode = objectMapper.readTree(json);
                System.out.println(json);

                float x = (float) jsonNode.get("x").asDouble();
                float y = (float) jsonNode.get("y").asDouble();
                int id = jsonNode.get("id").asInt();
                String username = jsonNode.get("username").asText();

                if (Objects.equals(username, wiz.getString("displayName"))) {
                    return;
                }

                String ext = jsonNode.get("extension").asText();
                GameCharacters playerType = getType(ext);
                PointF pos = new PointF(x, y);
                String element = jsonNode.get("element").asText();
                int roomID = jsonNode.get("roomID").asInt();

                players.put(username, new Wizard(pos, playerType, 2, username, element, roomID));

            } catch (JsonProcessingException | JSONException e) {
                throw new RuntimeException(e);
            }
        }  else {
        try {
            String json = message.substring(message.indexOf("{"));
            JsonNode jsonNode = objectMapper.readTree(json);

            if (jsonNode.isObject()) {
                // Create a set of usernames from the incoming JSON
                Set<String> incomingUsernames = new HashSet<>();
                Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> field = fields.next();
                    JsonNode node = field.getValue();
                    String username = node.get("username").asText();
                    incomingUsernames.add(username);
                }

                // Remove any players from the map that are not in the incoming JSON
                players.keySet().removeIf(username -> !incomingUsernames.contains(username));

                // Now proceed with the rest of your code
                fields = jsonNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> field = fields.next();
                    JsonNode node = field.getValue();

                    String username = node.get("username").asText();
                    int type = node.get("type").asInt();

                    if (!players.containsKey(username)) {
                        switch (type) {
                            case 0:
                                handleWizard(node, objectMapper);
                                break;
                            case 1:
                                handleEnemy(node, objectMapper);
                                break;
                        }
                    } else {
                        switch (type) {
                            case 0:
                                updateWizard(node);
                                break;
                            case 1:
                                updateEnemy(node);
                                break;
                        }
                    }
                }
            }
        } catch (JsonProcessingException e) {
            // Log the error message here
        }
    }
    }


    private void handleWizard(JsonNode node, ObjectMapper objectMapper) {
        float x = (float) node.get("x").asDouble();
        float y = (float) node.get("y").asDouble();
        int id = node.get("id").asInt();
        String username = node.get("username").asText();
        String ext = node.get("extension").asText();
        String element = node.get("element").asText();
        int roomID = node.get("roomID").asInt();
        try {
            if (Objects.equals(username, wiz.getString("displayName"))) {
                return;
            }
        } catch (JSONException e) {
            // Log the error message here
        }

        GameCharacters playerType = getType(ext);
        PointF pos = new PointF(x, y);

        players.put(username, new Wizard(pos, playerType, 2, username, element, roomID));
    }


    private void handleEnemy(JsonNode node, ObjectMapper objectMapper) {
        float x = (float) node.get("x").asDouble();
        float y = (float) node.get("y").asDouble();
        int id = node.get("id").asInt();
        String username = node.get("username").asText();
        String ext = node.get("extension").asText();
        String element = node.get("element").asText();

        GameCharacters eType = getEType(ext);
        PointF pos = new PointF(x, y);
        if(ext.compareTo("boss")==0){
            players.put(username, new Enemy(pos, eType, 3, id,2));

        }else{
            players.put(username, new Enemy(pos, eType, 2, id,0));

        }
    }


    private void updateEnemy(JsonNode node) {
        float x = (float) node.get("x").asDouble();
        float y = (float) node.get("y").asDouble();
        int id = node.get("id").asInt();
        String username = node.get("username").asText();
        PointF pos = new PointF(x, y);
        int roomID = node.get("roomID").asInt();
        Enemy updateEnemy = (Enemy) players.get(username);
        updateEnemy.setPosition(new PointF(x, y));

    }

    private void updateWizard(JsonNode node) {
        float x = (float) node.get("x").asDouble();
        float y = (float) node.get("y").asDouble();
        int id = node.get("id").asInt();
        String username = node.get("username").asText();
        int roomID = node.get("roomID").asInt();

        try {
            if (Objects.equals(username, wiz.getString("displayName"))) {
                return;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String ext = node.get("extension").asText();
        GameCharacters playerType = getType(ext);
        PointF pos = new PointF(x, y);

        Wizard updateWiz = (Wizard) players.get(username);
        updateWiz.setPosition(new PointF(x, y));
        updateWiz.setRoomID(roomID);
    }




    private GameCharacters getEType(String ext) {
        switch(ext){
            case "test_guy":
                return  GameCharacters.BADGUY;
            case "wisp":
                return GameCharacters.WISP;
            case "mimic":
                return GameCharacters.OVERWORLDMIMIC;
            case "witch":
                return GameCharacters.WITCH;
            case "boss":
                return GameCharacters.WARLOCK;

        }
        return  GameCharacters.BADGUY;
    }

    /**
     * changes the collision time to give a buffer in between collisions
     * @param f
     */
    public static void setCollisionTimer(float f){
        collisionTimer = f;
    }

    private GameCharacters getType(String ext){
        switch(ext){
            case "ice_icon":
                return  GameCharacters.PLAYERICE;
            case "fire_icon":
                return GameCharacters.PLAYERFIRE;
            case "storm_icon":
                return  GameCharacters.PLAYERSTORM;
            case "life_icon":
                return GameCharacters.PLAYERLIFE;
            case "death_icon":
                return GameCharacters.PLAYERDEATH;
            case "earth_icon":
                return GameCharacters.PLAYEREARTH;

        }
        return GameCharacters.PLAYERDEATH;
    }

}
