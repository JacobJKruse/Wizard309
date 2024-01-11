package com.example.wizard309.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Insets;
import android.graphics.Typeface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.wizard309.WebSockets.WebSocketListener;
import com.example.wizard309.WebSockets.WebSocketManager;
import com.example.wizard309.entities.Card;
import com.example.wizard309.environments.GameMap;
import com.example.wizard309.helpers.BackgroundAudioPlayer;
import com.example.wizard309.volley.net_utils.Const;
//import com.example.wizard309.volley.net_utils.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.wizard309.R;

import org.java_websocket.handshake.ServerHandshake;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * main activity for the game
 */
public class MainActivity extends AppCompatActivity {

    private GamePanel pannel;
    public static int screenWidth, screenHeight;
    private static Context gameContext;
    private static WindowInsetsControllerCompat windowInsetsController;
    public static JSONObject wiz = new JSONObject();
    public static float camX = screenWidth/2;
    public static float camY =  screenHeight/2;

    public static GameMap gameMap;
    public static ArrayList<Card> deck = new ArrayList<>();
    public static ArrayList<Card> cardInv = new ArrayList<>();
    private static int userID;
    private static BackgroundAudioPlayer mediaPlayer;


    public static int enemyID;
    @Override
    public void onBackPressed() {
        //makes back button do nothin
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameContext = this;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        screenHeight = height;
        screenWidth = width;

        windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Hide the system bars.
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

        setWizard();
        userID = getIntent().getIntExtra("PlayerID",-99);
        pannel = new GamePanel(this,height,width);
        
        setContentView(pannel);
        // Initialize the media player with the audio resource for the overworld
        mediaPlayer = new BackgroundAudioPlayer(this, R.raw.overworld);

        // Start playing the audio
        mediaPlayer.play();
    }

    /**
     * switches the audio on the game states
     * @param id
     */
    public static void swtichAudio(int id){
        mediaPlayer.switchAudio(id);
    }

    /**
     * gets the user ID
     * @return
     */
    public static int getUser(){


        return userID;
    }

    /**
     * returns the the menu
     *
     */
    public void backToMenu(){
        Intent i = new Intent(MainActivity.getGameContext(), MainMenuActivity.class);
        startActivity(i);
    }

    /**
     * player kicked sends a toast message when you have been kicked
     */
    public static void playerKicked(){
        Toast.makeText(getGameContext(), "You have been kicked", Toast.LENGTH_SHORT).show();

    }



    private void setWizard() {
        try{
            wiz.put("id",getIntent().getIntExtra("id",-99));
            wiz.put("displayName",getIntent().getStringExtra("displayName"));
            wiz.put("maxHP",getIntent().getIntExtra("maxHP",-99));
            wiz.put("lvl",getIntent().getIntExtra("lvl",-99));
            wiz.put("nextLvlXp",getIntent().getIntExtra("nextLvlXp",-99));
            wiz.put("currXp",getIntent().getIntExtra("currXp",-99));
            wiz.put("element", getIntent().getStringExtra("element"));
            wiz.put("extension", getIntent().getStringExtra("extension"));
            wiz.put("deckSize",getIntent().getIntExtra("deckSize",-99));
            wiz.put("currhp",getIntent().getIntExtra("currhp",-99));
            wiz.put("max_mana",getIntent().getIntExtra("max_mana",-99));
            wiz.put("curr_mana",getIntent().getIntExtra("curr_mana",-99));
            wiz.put("wallet",getIntent().getIntExtra("wallet",0));
            System.out.println("set wiz: " + wiz);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    /**
     *
     * @return the selected wizard
     */
    public static JSONObject getWizard(){
        return wiz;
    }

    /**
     *
     * @return current game context
     */
    public static Context getGameContext() {
        return gameContext;
    }

    /**
     *
     * @return windowInsetsController
     */
    public static WindowInsetsControllerCompat getWindowInsetsController() {
        return windowInsetsController;
    }

    /**
     * updates camx and camy
     * @param x
     * @param y
     */
    public static void updateXY(float x, float y){
        camX = x;
        camY = y;
    }

    /**
     *
     * @return enemyID
     */
    public static int getEnemyID(){
        return enemyID;
    }

    /**
     * sets enemyID to id
     * @param id
     */
    public static void setEnemyID(int id){
        enemyID = id;
    }

    /**
     * sets GameMap to map
     * @param map
     */
    public static void setGameMap(GameMap map){
        gameMap = map;
    }


}