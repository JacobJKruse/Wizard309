package com.example.wizard309.gamestates;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.health.SystemHealthManager;
import android.util.Log;
import android.view.MotionEvent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wizard309.R;
import com.example.wizard309.WebSockets.WebSocketListener;
import com.example.wizard309.WebSockets.WebSocketManager;
import com.example.wizard309.entities.BattleCharacter;
import com.example.wizard309.entities.Card;
import com.example.wizard309.entities.CardIcons;
import com.example.wizard309.entities.Character;
import com.example.wizard309.entities.Enemy;
import com.example.wizard309.entities.GameCharacters;
import com.example.wizard309.entities.Icons;
import com.example.wizard309.entities.Player;
import com.example.wizard309.entities.Wizard;
import com.example.wizard309.entities.cardClass;
import com.example.wizard309.environments.MapManager;
import com.example.wizard309.helpers.interfaces.GameStatesInterface;
import com.example.wizard309.main.Game;
import com.example.wizard309.main.MainActivity;
import com.example.wizard309.ui.ButtonImages;
import com.example.wizard309.ui.CustomButton;
import com.example.wizard309.volley.net_utils.Const;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Battle class for the game , you are sent to this class on collision
 */
public class Battle extends BaseState implements GameStatesInterface, WebSocketListener {

    RequestQueue queue;

    private Card[] cardHand;
    private CustomButton run,arrow,closeDialog,skipTurn;
    private int arrowX,arrowY;
    private boolean cardsVisable,cardMoving = false;
    private MapManager mapManager;
    private Paint namePaint;
    private List<Card> deck = new ArrayList<>(MainActivity.deck);

    private String BASE_URL = "ws://coms-309-032.class.las.iastate.edu:8080/BattleRoom/";
    //private String BASE_URL = "ws://10.0.2.2:8080/BattleRoom/";
    private JSONObject wiz = new JSONObject();
    private static String serverUrl = null;
    private ConcurrentHashMap<Integer , BattleCharacter> characterMap = new ConcurrentHashMap<>();
    private String turn;

    private boolean dialogVisable = false;
    private String dialog;
    private String scrollingDialog;
    private boolean dead = false;


    /**
     * constructor for the battle takes in the game and retrives the deck
     * @param game
     */

    public Battle(Game game) {

     
        super(game);
        queue = Volley.newRequestQueue(MainActivity.getGameContext());
        MainActivity.swtichAudio(R.raw.battletheme);
        namePaint = new Paint();
        namePaint.setColor(Color.BLACK);
        namePaint.setStyle(Paint.Style.FILL);
        namePaint.setTextSize(30);
        namePaint.setTextAlign(Paint.Align.CENTER);
       // System.out.println(MainActivity.deck.toArray().toString());
       // game.closePlayingState();
        arrowX = MainActivity.screenWidth/2;
        arrowY= MainActivity.screenHeight;
        cardHand = new Card[5];

        Collections.shuffle(deck);
//
//        cardDeck[0] = new Card(MainActivity.screenWidth/2-800- (103*2),MainActivity.screenHeight-300, "Fire Spell", cardClass.FIRE.getValue());
//        cardDeck[1] = new Card(MainActivity.screenWidth/2-400-(103*2),MainActivity.screenHeight-300, "Grass Spell", cardClass.GRASS.getValue());
//        cardDeck[2] = new Card(MainActivity.screenWidth/2-(103*2),MainActivity.screenHeight-300, "Water Spell", cardClass.WATER.getValue());
//        cardDeck[3] = new Card(MainActivity.screenWidth/2+400-(103*2),MainActivity.screenHeight-300, "Life Spell", cardClass.ELECTRIC.getValue());
//        cardDeck[4] = new Card(MainActivity.screenWidth/2+800-(103*2) ,MainActivity.screenHeight-300,"Earth Spell", cardClass.ROCK.getValue());
        for (Card card : deck) {

            System.out.println("Card Name: " + card.getName());
            // Print other attributes as needed
        }

        for (int i = 0; i < Math.min(5, MainActivity.deck.size()); i++) {
            Card card = deck.remove(0);
            card.setCardSpot(i + 1);
            cardHand[i] = card;
        }



        run = new CustomButton( 75,
                MainActivity.screenHeight -ButtonImages.RUNBUTTON.getHeight()-75,
                ButtonImages.RUNBUTTON.getWidth(),
                ButtonImages.RUNBUTTON.getHeight());
        skipTurn = new CustomButton( 75,
                MainActivity.screenHeight -ButtonImages.SKIPBUTTON.getHeight()-275,
                ButtonImages.SKIPBUTTON.getWidth(),
                ButtonImages.SKIPBUTTON.getHeight());
        arrow = new CustomButton(arrowX- ButtonImages.ARROWBUTTON.getWidth()/2,
                arrowY- ButtonImages.ARROWBUTTON.getHeight(),
                ButtonImages.ARROWBUTTON.getWidth(),
                ButtonImages.ARROWBUTTON.getHeight());

        closeDialog = new CustomButton( Icons.DIALOGBOX.getBitmapWidth(),MainActivity.screenHeight-Icons.DIALOGBOX.getBitmapHeight(),
                ButtonImages.CLOSEMENUBUTTON.getWidth(),ButtonImages.CLOSEMENUBUTTON.getHeight());

        try{
            wiz = MainActivity.getWizard();
            serverUrl = BASE_URL+wiz.getInt("id")+"/"+MainActivity.getEnemyID();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(serverUrl,Battle.this);


        mapManager = new MapManager("Battle");
    }

    /**
     * updates method takes in delta time to determine the proper update distance
     * @param delta
     */
    @Override
    public void update(double delta) {

        WebSocketManager.getInstance().setWebSocketListener(serverUrl,Battle.this);

        synchronized(characterMap) {
            for (Map.Entry<Integer, BattleCharacter> entry : characterMap.entrySet()) {

                BattleCharacter character = entry.getValue();

                character.update(delta,0,0);


            }
        }
    }

    /**
     * render displays the canvas drawing for the battle
     * @param c
     */
    @Override
    public void render(Canvas c) {

        mapManager.draw(c);
        mapManager.setCameraPos(-mapManager.getMaxWidth()/5,-mapManager.getMaxHeight()/3);

        drawPlayers(c);



        if(!dead) {
            c.drawBitmap(Icons.CARDHOLDER.getSpriteLogo(),
                    MainActivity.screenWidth / 2 - 800 - (103 * 4) - (103 / 4),
                    arrow.getHitbox().top + 70,
                    null);
            c.drawBitmap(
                    ButtonImages.ARROWBUTTON.getButtonImage(arrow.isClicked()),
                    arrow.getHitbox().left,
                    arrow.getHitbox().top,
                    null
            );
        }
                if(cardsVisable && !cardMoving && !dead){
                    for (Card cards : cardHand){

                        cards.draw(c);



                    }
                }else if(cardsVisable && cardMoving&& !dead){
                    for (Card cards : cardHand){
                        if(cards.getClicked()){
                            cards.draw(c);
                        }

                    }
                }
                if(!cardsVisable && !dead){
                    c.drawBitmap(
                            ButtonImages.RUNBUTTON.getButtonImage(run.isClicked()),
                            run.getHitbox().left,
                            run.getHitbox().top,
                            null
                    );
                    c.drawBitmap(ButtonImages.SKIPBUTTON.getButtonImage(skipTurn.isClicked()),
                            skipTurn.getHitbox().left,
                            skipTurn.getHitbox().top,null);

                }





        drawUI(c);
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
                wiz.put("element", wizard.get("element"));
                wiz.put("extension", wizard.get("extension"));
                wiz.put("deckSize",wizard.getInt("deckSize"));
                wiz.put("currhp",wizard.getInt("currhp"));
                wiz.put("max_mana",wizard.getInt("max_mana"));
                wiz.put("curr_mana",wizard.getInt("curr_mana"));
                wiz.put("wallet",wizard.getInt("wallet"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> System.out.println(error));

        MainActivity.wiz = wiz;
        queue.add(jsonArrayRequest);
    }
    private void drawPlayers(Canvas c) {
        namePaint.setTypeface(loadFont(MainActivity.getGameContext(), R.font.pressstart2pregular));

        Paint hpPaint = createHpPaint();
        Paint manaPaint = createManaPaint();

        synchronized(characterMap) {
            for (BattleCharacter character : characterMap.values()) {
                drawCharacterSprite(c, character);
                if(character.getType() == 0){
                    drawCharacterName(c, character);
                }
                drawElementLogo(c, character);
                drawHpArc(c, character, hpPaint);
                drawManaArc(c, character, manaPaint);
            }
        }
    }

    private Paint createHpPaint() {
        Paint hpPaint = new Paint();
        hpPaint.setColor(Color.RED);
        hpPaint.setStyle(Paint.Style.STROKE);
        hpPaint.setStrokeWidth(10);
        return hpPaint;
    }
    private Paint createManaPaint() {
        Paint hpPaint = new Paint();
        hpPaint.setColor(Color.BLUE);
        hpPaint.setStyle(Paint.Style.STROKE);
        hpPaint.setStrokeWidth(10);
        return hpPaint;
    }

    private void drawCharacterSprite(Canvas c, BattleCharacter character) {
        c.drawBitmap(character.getGameCharType().getSprite(character.getFaceDir(),character.getAniIndex()),
                character.getHitbox().left,
                character.getHitbox().top,
                null);
    }

    private void drawCharacterName(Canvas c, BattleCharacter character) {
        c.drawText(character.getName(),
                character.getHitbox().left  + GameCharacters.PLAYERFIRE.getSprite(0,0).getWidth()/2,
                character.getHitbox().top + GameCharacters.PLAYERFIRE.getSprite(0, 0).getHeight() + 40,
                namePaint);
    }
    private void drawElementLogo(Canvas c, BattleCharacter character) {
        Icons icon = getIcon(character.getElement());
        float width = namePaint.measureText(character.getName());
        if(icon != null) {
            float logoX;
            if(character.getType() == 0) {
                logoX = character.getHitbox().left + GameCharacters.PLAYERFIRE.getSprite(0,0).getWidth()/2 - width/2 - Icons.FIREICON.getSpriteLogo().getWidth() -5;
            } else {
                logoX = character.getHitbox().left;
            }
            c.drawBitmap(icon.getSpriteLogo(),
                    logoX,
                    character.getHitbox().top  + GameCharacters.PLAYERFIRE.getSprite(0, 0).getHeight(),
                    null);
        }
    }

    private void drawHpArc(Canvas c, BattleCharacter character, Paint hpPaint) {
        float radius = 20;
        float width = namePaint.measureText(character.getName());
        float centerX;
        if(character.getType() == 0) {
            centerX = character.getHitbox().left + GameCharacters.PLAYERFIRE.getSprite(0,0).getWidth()/2 + width/2 + radius + 10;
        } else {
            centerX = character.getHitbox().left + Icons.FIREICON.getSpriteLogo().getWidth() + 30;
        }
        float centerY = character.getHitbox().top + GameCharacters.PLAYERFIRE.getSprite(0, 0).getHeight() + 25;
        RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        float startAngle = -90;
        float sweepAngle = 360 * character.getCurrentHP() / character.getMaxHP();
        c.drawArc(oval, startAngle, sweepAngle, false, hpPaint);
    }
    private void drawManaArc(Canvas c, BattleCharacter character, Paint manaPaint) {
        float radius = 20;
        float width = namePaint.measureText(character.getName());
        float centerX;
        if(character.getType() == 0) {
            centerX = character.getHitbox().left + GameCharacters.PLAYERFIRE.getSprite(0,0).getWidth()/2 + width/2 + radius + 60;
        } else {
            centerX = character.getHitbox().left + Icons.FIREICON.getSpriteLogo().getWidth() + 80;
        }
        float centerY = character.getHitbox().top + GameCharacters.PLAYERFIRE.getSprite(0, 0).getHeight() + 25;
        RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        float startAngle = -90;
        float sweepAngle = 360 * character.getCurrentMana() / character.getMaxMana();
        c.drawArc(oval, startAngle, sweepAngle, false, manaPaint);
    }
    private int textIndex = 0; // Add this line at the class level

    /**
     * sets the dialog of the textbox
     * @param newDialog
     */
    public void setDialog(String newDialog) {
        this.dialog = newDialog;
        dialogVisable = true;
        this.textIndex = 0;
    }

    private void drawUI(Canvas c){
        c.drawBitmap(Icons.TURNBACKGROUND.getSpriteLogo(),20,20,null);
        for (Map.Entry<Integer, BattleCharacter> entry : characterMap.entrySet()){
            BattleCharacter character = entry.getValue();

            if(character.getName().compareTo(turn) == 0){
                c.drawBitmap(character.getGameCharType().getSprite(0,0),Icons.TURNBACKGROUND.getBitmapWidth()/4,Icons.TURNBACKGROUND.getBitmapHeight()/5,null);
            }

        }
        c.drawBitmap(Icons.TURNLOGO.getSpriteLogo(),Icons.TURNBACKGROUND.getBitmapWidth()/4 ,Icons.TURNBACKGROUND.getBitmapHeight()-Icons.TURNLOGO.getBitmapHeight()/2,null);
        if(dialogVisable){
            c.drawBitmap(Icons.DIALOGBOX.getSpriteLogo(), MainActivity.screenWidth/10,MainActivity.screenHeight-Icons.DIALOGBOX.getBitmapHeight(),null);
            c.drawBitmap( ButtonImages.CLOSEMENUBUTTON.getButtonImage(closeDialog.isClicked()),
                    Icons.DIALOGBOX.getBitmapWidth(),MainActivity.screenHeight-Icons.DIALOGBOX.getBitmapHeight(),
                    null);
            // Create a new Paint object for the text
            Paint textPaint = new Paint();
            textPaint.setTextSize(46); // Set the desired text size
            textPaint.setColor(Color.BLACK); // Set the text color
            textPaint.setTypeface(loadFont(MainActivity.getGameContext(), R.font.pressstart2pregular));

            // Enable text wrapping within the dialog box
            String[] words = dialog.split(" ");
            StringBuilder line = new StringBuilder();
            StringBuilder wrappedText = new StringBuilder();

            for (String word : words) {
                if ((line.length() + word.length() + 1) <= 40) { // +1 for the space
                    line.append(" ").append(word);
                } else {
                    wrappedText.append(line).append("\n");
                    line = new StringBuilder(word);
                }
            }
            wrappedText.append(line);

            // Draw the text letter by letter
            String textToDraw = wrappedText.toString().substring(0, Math.min(textIndex, wrappedText.length()));
            float y = MainActivity.screenHeight-Icons.DIALOGBOX.getBitmapHeight() +140;
            float x = MainActivity.screenWidth/10 + 30;
            for (char ch : textToDraw.toCharArray()) {
                if (ch == '\n') {
                    y += textPaint.getTextSize()+30; // Move to the next line
                    x = MainActivity.screenWidth/10 + 30; // Reset the x-coordinate
                    continue;
                }
                c.drawText(String.valueOf(ch), x, y, textPaint);
                x += textPaint.measureText(String.valueOf(ch)); // Update the x-coordinate
            }

            // Increment the text index
            if (textIndex < wrappedText.length()) {
                textIndex++;
            }
        }



    }


    /**
     * gets the touch event specified from interacting with the screen
     * these events and be up,down, or move
     * @param event
     */
    @Override
    public void touchEvents(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                checkButton(x, y, run);
                checkButton(x, y, arrow);
                checkButton(x,y,skipTurn);
                checkButton(x,y,closeDialog);
                for (Card dragO : cardHand) {
                    if (dragO != null && dragO.isTouched(event.getX(), event.getY()) && cardsVisable) {
                        dragO.setClicked(true);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                try {

                    for (int i = 0; i < cardHand.length; i++) {
                        Card cards = cardHand[i];
                        if (cards != null && cards.getClicked() && cardsVisable && !dead) {
                            for (Map.Entry<Integer, BattleCharacter> entry : characterMap.entrySet()) {
                                BattleCharacter character = entry.getValue();
                                if (character.isTouched(event.getX(), event.getY())) {
                                    if (turn.compareTo(wiz.getString("displayName")) == 0) {
                                        deck.add(cards);
                                        //dialogVisable = true;
                                        cardsVisable = !cardsVisable;
                                        arrow.getHitbox().top = MainActivity.screenHeight - ButtonImages.ARROWBUTTON.getHeight();
                                        //textIndex = 0;
                                        if (character.getType() == 1) {
                                            // Add dialog here stating who the card was dropped on with what card
                                            // dialog = "Card " + cards.getName() + " was dropped on " + character.getCharacter();
                                            JSONObject jsonObject = new JSONObject();

                                            // Put key-value pairs into the JSON object
                                            try {
                                                jsonObject.put("wizardId", wiz.getInt("id"));
                                                jsonObject.put("enemyId", character.getId());
                                                jsonObject.put("cardId", cards.getID());
                                                sendMessage(jsonObject);
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }


                                        } else {
                                            // Add dialog here stating who the card was dropped on with what card
                                            // dialog = "Card " + cards.getName() + " was dropped on " + character.getName();
                                            JSONObject jsonObject = new JSONObject();

                                            // Put key-value pairs into the JSON object
                                            try {
                                                jsonObject.put("wizardId", wiz.getInt("id"));
                                                jsonObject.put("enemyId", character.getId());
                                                jsonObject.put("cardId", cards.getID());
                                                sendMessage(jsonObject);
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }

                                        }
                                        Card card = deck.remove(0);
                                        if (card != null) {
                                            card.setCardSpot(i + 1);
                                            cardHand[i] = card; // Corrected line
                                        }
                                    }else{
                                        setDialog("A great wizard said: be patience as it  is not your turn ");
                                    }
                                }
                            }
                        }

                    }
                        if (isIn(event, run) && !dead) {
                            if (run.isClicked() ) {
                                if( turn.compareTo(wiz.getString("displayName")) == 0) {
                                    // game.initPlayingState();
                                    //showKeyboard = !showKeyboard; experimenting with keyboards
                                    game.setCurrentState(Game.GameState.PLAYING);
                                    updateWiz();
                                    Playing.callUpdateWiz();
                                    Playing.setCollisionTimer(5);
                                    WebSocketManager.getInstance().disconnectWebSocket(serverUrl);
                                    game.closeBattleState();

                                }else{
                                    setDialog("A great wizard said: be patience as it  is not your turn ");
                                }


                            }

                        }
                    if (isIn(event, skipTurn) && !dead) {
                        if (skipTurn.isClicked()) {
                            if (turn.compareTo(wiz.getString("displayName")) == 0) {
                                JSONObject jsonObject = new JSONObject();

                                // Put key-value pairs into the JSON object
                                try {
                                    jsonObject.put("wizardId", wiz.getInt("id"));
                                    jsonObject.put("enemyId", -1);
                                    jsonObject.put("cardId", -1);
                                    sendMessage(jsonObject);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }


                            }else{
                                setDialog("A great wizard said: be patience as it  is not your turn ");
                            }
                        }
                    }
                        if (isIn(event, arrow)) {
                            if (arrow.isClicked() && !cardMoving ) {
                                //showKeyboard = !showKeyboard; experimenting with keyboards
                                cardsVisable = !cardsVisable;
                                if (!cardsVisable) {
                                    arrow.getHitbox().top = MainActivity.screenHeight - ButtonImages.ARROWBUTTON.getHeight();
                                } else {

                                    int i = 0;
                                    while (i < 724) {
                                        arrow.getHitbox().top -= .5;
                                        i += 1;

                                    }
                                }

                            }

                        }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }



                if (isIn(event, closeDialog)) {
                    if (closeDialog.isClicked() && dialogVisable) {
                        dialogVisable = false;
                        if(dead){
                            game.setCurrentState(Game.GameState.PLAYING);
                            Playing.setCollisionTimer(5);
                            WebSocketManager.getInstance().disconnectWebSocket(serverUrl);
                            game.closeBattleState();
                            updateWiz();
                            Playing.callUpdateWiz();

                        }

                    }

                }



                run.setClicked(false);
                skipTurn.setClicked(false);
                closeDialog.setClicked(false);
                arrow.setClicked(false);
                cardMoving = false;

                for (Card cards : cardHand) {
                    if (cards != null) {
                        cards.setClicked(false);
                        cards.setFlashing(false);
                        cards.returnToPos();
                    }
                }
                if (cardsVisable && arrow.getHitbox().top >= MainActivity.screenHeight - ButtonImages.ARROWBUTTON.getHeight()) {
                    int i = 0;
                    while (i < 724) {
                        arrow.getHitbox().top -= .5;
                        i += 1;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:

                    for (Card cards : cardHand) {
                        if (cards != null && cards.getClicked() && cardsVisable) {
                            cardMoving = true;

                            boolean isCardOnCharacter = false;
                            for (Map.Entry<Integer, BattleCharacter> entry : characterMap.entrySet()) {
                                BattleCharacter character = entry.getValue();

                                if (character.isTouched(event.getX(), event.getY())) {
                                    //System.out.println("Hover");
                                    cards.setPosition(character.position.x + CardIcons.cardBackgrounds.getWidth() / 2, character.position.y + CardIcons.cardBackgrounds.getHeight() / 2);
                                    //System.out.println(cards.getX());
                                    //System.out.println(cards.getY());
                                    isCardOnCharacter = true;
                                    cards.setFlashing(true); // Start flashing when hovering over a character
                                    break;
                                }
                            }

                            if (!isCardOnCharacter) {
                                cards.setPosition(event.getX(), event.getY());
                                cards.setFlashing(false); // Stop flashing when not hovering over a character
                            }

                            arrow.getHitbox().top = MainActivity.screenHeight - ButtonImages.ARROWBUTTON.getHeight();
                        } else if (cards != null) {
                            cards.returnToPos();
                            cards.setClicked(false);
                            cards.setFlashing(false); // Stop flashing when the card is not clicked
                        }
                    }

                break;
        }
    }
            private void checkButton(float x,float y,CustomButton button){
        // bro this was the most useful debug message i have ever made
        // System.out.println("x: "+x+"y: "+y+ "bottom: "+button.getHitbox().bottom + "top: "+button.getHitbox().top +"height: " +ButtonImages.SETTINGSBUTTON.getHeight());
        if(x > button.getHitbox().left && x < button.getHitbox().right){
            if(y < button.getHitbox().bottom && y > button.getHitbox().top)
                button.setClicked(true);
        }
    }
    private boolean isIn(MotionEvent e, CustomButton b) {
        return b.getHitbox().contains(e.getX(), e.getY());
    }

    /**
     * called when open on webscoket connection
     * @param handshakedata Information about the server handshake.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    /**
     * received message and interprets the message
     * @param message The received WebSocket message.
     */
    @Override
    public void onWebSocketMessage(String message) {
        System.out.println(message);
        if(message.startsWith("Card")){

            dialogVisable = true;
            textIndex = 0;
            String card = message.trim();
            dialog = card;


        }
        else if (message.contains("died")) {
            // Split the message into words
            String[] words = message.split(" ");
            // The wizard's name should be the second word in the message
            String wizardName = words[1];
            try {
                if(wizardName.compareTo(wiz.getString("displayName"))==0){
                    dead = true;
                    textIndex= 0;
                    dialogVisable = true;
                    dialog = "You have DIED";
                } else {
                    textIndex= 0;
                    dialogVisable = true;
                    dialog = message;

                    // If the wizard's name is not your name, remove the wizard from the characterMap
                    for (Integer key : characterMap.keySet()) {
                        BattleCharacter character = characterMap.get(key);
                        if (character.getName().equals(wizardName)) {
                            characterMap.remove(key);
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        else{
        try {
            System.out.println("Message" + message);

            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            String json = message.substring(message.indexOf("{"));

            // Parse JSON data into a JsonNode
            JsonNode jsonNode = objectMapper.readTree(json);

            // Check if the JSON is an object
            if (jsonNode.isObject()) {
                // Parse turn
                turn = jsonNode.get("turn").asText();
                System.out.println(turn);

                String jsonString = jsonNode.get("players").toString();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode arrayNode = mapper.readTree(jsonString);

                int spot = 0;


                Iterator<Map.Entry<Integer, BattleCharacter>> iterator = characterMap.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<Integer, BattleCharacter> entry = iterator.next();
                    boolean found = false;

                    for (JsonNode playerNode : arrayNode) {
                        if (playerNode.asText().equals(entry.getKey().toString())) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        iterator.remove();
                    }
                }

                for (JsonNode playerNode : arrayNode) {


                    PointF pos = getSpot(0,spot);
                    spot += 1;
                    GameCharacters playerType = getType(playerNode.get("extension").asText());
                    if(playerNode.get("extension").asText().compareTo("boss") == 0){
                        characterMap.put(playerNode.get("id").asInt(),new BattleCharacter(pos,playerType,3,
                                playerNode.get("id").asInt(),playerNode.get("maxHP").asInt(),
                                playerNode.get("currHP").asInt(),playerNode.get("maxMana").asInt(),
                                playerNode.get("currMana").asInt(),
                                playerNode.get("lvl").asInt(),
                                playerNode.get("element").asText(),
                                0, playerNode.get("displayName").asText(),playerNode.get("extension").asText()));


                    }else{
                        characterMap.put(playerNode.get("id").asInt(),new BattleCharacter(pos,playerType,2,
                                playerNode.get("id").asInt(),playerNode.get("maxHP").asInt(),
                                playerNode.get("currHP").asInt(),playerNode.get("maxMana").asInt(),
                                playerNode.get("currMana").asInt(),
                                playerNode.get("lvl").asInt(),
                                playerNode.get("element").asText(),
                                0, playerNode.get("displayName").asText(),playerNode.get("extension").asText()));



                    }
                    }


                // Parse enemies

                String jsonEnemyString = jsonNode.get("enemies").toString();
                ObjectMapper enemyMapper = new ObjectMapper();
                JsonNode arrayEnemyNode = enemyMapper.readTree(jsonEnemyString);
                spot = 0;

                for (JsonNode enemyNode : arrayEnemyNode) {

                    PointF pos = getSpot(1,spot);
                    spot += 1;
                    if(enemyNode.get("extension").asText().compareTo("boss") == 0){
                        GameCharacters playerType = getEType(enemyNode.get("extension").asText());
                        characterMap.put(enemyNode.get("id").asInt(),new BattleCharacter(pos,playerType,3,
                                enemyNode.get("id").asInt(),enemyNode.get("maxHP").asInt(),
                                enemyNode.get("currHP").asInt(),enemyNode.get("maxMana").asInt(),
                                enemyNode.get("currMana").asInt(),
                                enemyNode.get("lvl").asInt(),
                                enemyNode.get("element").asText(),
                                1, enemyNode.get("displayName").asText(),enemyNode.get("extension").asText()));
                    }else{
                    GameCharacters playerType = getEType(enemyNode.get("extension").asText());
                    characterMap.put(enemyNode.get("id").asInt(),new BattleCharacter(pos,playerType,2,
                            enemyNode.get("id").asInt(),enemyNode.get("maxHP").asInt(),
                            enemyNode.get("currHP").asInt(),enemyNode.get("maxMana").asInt(),
                            enemyNode.get("currMana").asInt(),
                            enemyNode.get("lvl").asInt(),
                            enemyNode.get("element").asText(),
                            1, enemyNode.get("displayName").asText(),enemyNode.get("extension").asText()));
                }
                }

                // Now you have the turn, players, and enemies as Java objects
                // You can process them as needed
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }}
    }

    private PointF getSpot(int type, int spot) {
        if(type == 0){
            switch(spot){
                case 0:
                    return new PointF(MainActivity.screenWidth/2+100,MainActivity.screenHeight/2+200);
                case 1:
                    return new PointF(MainActivity.screenWidth/2-300,MainActivity.screenHeight/2+200);
                case 2:
                    return new PointF(MainActivity.screenWidth/2+300,MainActivity.screenHeight/2+200);
                case 3:
                    return new PointF(MainActivity.screenWidth/2-500,MainActivity.screenHeight/2+200);
            }
        }else{
            switch(spot){
                case 0:
                    return new PointF(MainActivity.screenWidth/2+100,MainActivity.screenHeight/2-500);
                case 1:
                    return new PointF(MainActivity.screenWidth/2-300,MainActivity.screenHeight/2-500);
                case 2:
                    return new PointF(MainActivity.screenWidth/2+300,MainActivity.screenHeight/2-500);
                case 3:
                    return new PointF(MainActivity.screenWidth/2-500,MainActivity.screenHeight/2-500);
            }
        }
        return null;
    }

    /**
     * called on the close of the websocket
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    /**
     * called on error of the websocket
     * @param ex The exception that describes the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {
    }

    public static void sendMessage(String message){
        try {
            // send message
            WebSocketManager.getInstance().sendMessage(serverUrl,message);
        } catch (Exception e) {
            Log.d("ExceptionSendMessage:", e.getMessage().toString());
        }
    }
    public static void sendMessage(JSONObject jsonMessage){
        try {
            // Convert JSONObject to String
            String message = jsonMessage.toString();

            // send message
            WebSocketManager.getInstance().sendMessage(serverUrl, message);
        } catch (Exception e) {
            Log.d("ExceptionSendMessage:", e.getMessage().toString());
        }
    }
    private GameCharacters getEType(String ext) {
        switch(ext){
            case "test_guy":
                return  GameCharacters.BADGUY;
            case "wisp":
                return GameCharacters.WISP;
            case "mimic":
                return GameCharacters.MIMIC;
            case "witch":
                return GameCharacters.WITCH;
            case "boss":
                return GameCharacters.WARLOCK;
        }
        return  GameCharacters.BADGUY;
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

}
