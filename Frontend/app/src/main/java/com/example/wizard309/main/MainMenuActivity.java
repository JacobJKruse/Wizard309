package com.example.wizard309.main;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wizard309.R;
import com.example.wizard309.entities.Icons;
import com.example.wizard309.entities.Player;
import com.example.wizard309.helpers.BackgroundAudioPlayer;
import com.example.wizard309.volley.net_utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainMenuActivity extends AppCompatActivity {
    private JSONObject wizard1 = new JSONObject();
    private JSONObject wizard2 = new JSONObject();
    private JSONObject wizard3 = new JSONObject();
    private JSONArray wizards = new JSONArray();
    String url = Const.URL_WIZARDS_JSON_ARRAY;
    private static Context gameContext;
    RequestQueue queue;
    BackgroundAudioPlayer mediaPlayer;

    List<ImageView> images = new ArrayList<>();
    List<ImageButton> buttons = new ArrayList<>();

    private boolean loadWizardUIOpen = false;
    List<View> loadWizard = new ArrayList<>();
    Animator anim;

    TextView cardNameOneText,cardNameTwoText,cardNameThreeText;
    TextView cardOneHp,cardOneMana,cardOneXp,cardOneLevel;
    TextView cardTwoHp,cardTwoMana,cardTwoXp,cardTwoLevel;
    TextView cardThreeHp,cardThreeMana,cardThreeXp,cardThreeLevel;
    ImageView cardOneImage,cardTwoImage,cardThreeImage;
    ImageButton deleteOne,deleteTwo,deleteThree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameContext = this;
        mediaPlayer = new BackgroundAudioPlayer(this,R.raw.wizardmenu);
        mediaPlayer.play();
        queue = Volley.newRequestQueue(MainMenuActivity.getGameContext());
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Hide the system bars.
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        setContentView(R.layout.activity_main_menu);

        images.add(findViewById(R.id.background));
        images.add(findViewById(R.id.loadWizardBack));

        getWizardJson();

        deleteOne = findViewById(R.id.deleteOne); deleteTwo = findViewById(R.id.deleteTwo); deleteThree=findViewById(R.id.deleteThree);

        handleButtons();

        View v = findViewById(R.id.loadWizardBack);
        loadWizard.add(v);
        for(View x : loadWizard) x.setVisibility(View.INVISIBLE);


        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setFilers(images,buttons);

    }

    /**
     *
     * @return current game context
     */
    public static Context getGameContext() {return gameContext;}
    private void setFilers(List<ImageView> i,List<ImageButton> b){
        for(ImageView x : i){
            x.getDrawable().setFilterBitmap(false);
        }
        for(ImageButton x : b){
            x.getDrawable().setFilterBitmap(false);
        }
    }
    private void setWizardJsonObject(JSONArray response){
        try {
            for(int i = 0; i < response.length(); i++){
                if(i > 2){
                    return;
                }
                wizards.getJSONObject(i).put("id", response.getJSONObject(i).get("id"));
                wizards.getJSONObject(i).put("element",response.getJSONObject(i).getString("element"));
                wizards.getJSONObject(i).put("displayName", response.getJSONObject(i).getString("displayName"));
                wizards.getJSONObject(i).put("extension", response.getJSONObject(i).getString("extension"));
                wizards.getJSONObject(i).put("maxHP", response.getJSONObject(i).getString("maxHP"));
                wizards.getJSONObject(i).put("lvl", response.getJSONObject(i).getString("lvl"));
                wizards.getJSONObject(i).put("nextLvlXp", response.getJSONObject(i).getString("nextLvlXp"));
                wizards.getJSONObject(i).put("currXp", response.getJSONObject(i).getString("currXp"));
                wizards.getJSONObject(i).put("currhp", response.getJSONObject(i).getString("currhp"));
                wizards.getJSONObject(i).put("max_mana", response.getJSONObject(i).getString("max_mana"));
                wizards.getJSONObject(i).put("curr_mana", response.getJSONObject(i).getString("curr_mana"));
                wizards.getJSONObject(i).put("deckSize",response.getJSONObject(i).getInt("deckSize"));
                wizards.getJSONObject(i).put("wallet",response.getJSONObject(i).getInt("wallet"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getWizardJson(){
        wizards = new JSONArray();
        wizards.put(wizard1);
        wizards.put(wizard2);
        wizards.put(wizard3);

        String newUrl = url +"/user/" +  getIntent().getIntExtra("PlayerID",-99);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, newUrl, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject responseObj = response.getJSONObject(i);
                    System.out.println(responseObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setWizardJsonObject(response);
            }

        }, error -> System.out.println(error));

        queue.add(jsonArrayRequest);

    }
    private void createWizard(){
        JSONObject wiz = new JSONObject();
        String newUrl = url;
        try {
            newUrl = "http://10.0.2.2:8080/wizards" +"/"+wizard1.getInt("id");
            wizard1.remove("deckSize");
            wizard1.put("deckSize", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.PUT, newUrl, wizard1, response -> {

        }, error -> System.out.println("CREATE ERROR"));
        queue.add(jsonArrayRequest);

        getWizardJson();
    }
    private void deleteWizard(int id, int wizardNum) throws InterruptedException {
        String newUrl = url +"/"+id;
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.DELETE, newUrl, null, response -> {
            switch (wizardNum){
                case 0:
                    wizard1 = new JSONObject();
                    break;
                case 1:
                    wizard2 = new JSONObject();
                case 2:
                    wizard3 = new JSONObject();
                    break;
            }
        }, error -> System.out.println("DELETE ERROR"));
        queue.add(jsonArrayRequest);
    }
    private void updateWizard() {
        String newUrl = null;
        try {
            wizard3.put("currhp", "69");
            newUrl = url +"/"+wizard3.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.PUT, newUrl, wizard3, response -> {

        }, error -> System.out.println("UPDATE ERROR"));

        queue.add(jsonArrayRequest);
    }
    private void showWizards(List<View>loadWizardViews) throws InterruptedException {
        if(anim != null && anim.isRunning())
            return;
        getWizardJson();
        TimeUnit.MILLISECONDS.sleep(200);
        setUpLoad();
        loadWizardUIOpen = true;
        for(View x : loadWizardViews){
            View view = x;

            int cx = view.getWidth()/2;
            int cy = view.getHeight()/2;

            float finalRadius = (float) Math.hypot(cx,cy);

            anim = ViewAnimationUtils.createCircularReveal(view,cx,cy,0,finalRadius);
            view.setVisibility(View.VISIBLE);
            anim.start();
        }


    }
    private void hideWizards(List<View>loadWizardViews){
        if(anim != null && anim.isRunning())
            return;
        getWizardJson();
        loadWizardUIOpen = false;
        for(View x : loadWizardViews) {
            View view = x;

            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;

            float initRadius = (float) Math.hypot(cx, cy);

            anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initRadius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        }

    }
    private void passWizard(JSONObject wiz, Intent intent){
        try {
            intent.putExtra("id",wiz.getInt("id"));
            intent.putExtra("displayName",wiz.getString("displayName"));
            intent.putExtra("maxHP",wiz.getInt("maxHP"));
            intent.putExtra("lvl",wiz.getInt("lvl"));
            intent.putExtra("nextLvlXp",wiz.getInt("nextLvlXp"));
            intent.putExtra("currXp",wiz.getInt("currXp"));
            intent.putExtra("element", wiz.getString("element"));
            intent.putExtra("extension", wiz.getString("extension"));
            intent.putExtra("deckSize",wiz.getInt("deckSize"));
            intent.putExtra("currhp",wiz.getInt("currhp"));
            intent.putExtra("max_mana",wiz.getInt("max_mana"));
            intent.putExtra("curr_mana",wiz.getInt("curr_mana"));
            intent.putExtra("wallet",wiz.getInt("wallet"));
            intent.putExtra("roomID", wiz.getInt("roomID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void handleButtons(){
        ImageButton button3 = findViewById(R.id.continueGame);
        button3.setOnClickListener(v -> {
            getWizardJson();
            Intent i = new Intent(MainMenuActivity.this, MainActivity.class);
            System.out.println(wizard1);
            passWizard(wizard1,i);
            int playerId = getIntent().getIntExtra("PlayerID",-999);

            i.putExtra("PlayerID",playerId);
            System.out.println("PLAYER ID" + playerId);
            mediaPlayer.stop();
            startActivity(i);
        });

        ImageButton button = findViewById(R.id.newWizard);
        button.setOnClickListener(v ->{
            int playerId = getIntent().getIntExtra("PlayerID",-999);
            Intent intent = new Intent(MainMenuActivity.this, CharacterCreator.class);
            intent.putExtra("PlayerID",playerId);
            System.out.println("PLAYER ID" + playerId);

            mediaPlayer.stop();
            startActivity(intent);
        });

        ImageButton button1 = findViewById(R.id.loadWizard);
        button1.setOnClickListener(v -> {
            if(loadWizardUIOpen)
                hideWizards(loadWizard);
            else {
                try {
                    showWizards(loadWizard);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        ImageButton button2 = findViewById(R.id.settings);
        button2.setOnClickListener(v ->{
            //updateWizard();
        });

        ImageButton loadWizardClose = findViewById(R.id.loadWizardClose);
        loadWizardClose.setOnClickListener(v ->{
            hideWizards(loadWizard);
        });

        ImageButton card1 = findViewById(R.id.loadCardOne);
        card1.setOnClickListener( v ->{
            Intent i = new Intent(MainMenuActivity.this, MainActivity.class);
            if(!wizard1.has("id"))
                return;
            System.out.println(wizard1);
            passWizard(wizard1,i);
            int playerId = getIntent().getIntExtra("PlayerID",-999);

            i.putExtra("PlayerID",playerId);
            System.out.println("PLAYER ID" + playerId);
            mediaPlayer.stop();
            startActivity(i);
        });
        ImageButton card2 = findViewById(R.id.loadCardTwo);
        card2.setOnClickListener(v ->{
            Intent i = new Intent(MainMenuActivity.this, MainActivity.class);
            if(!wizard2.has("id"))
                return;
            System.out.println(wizard2);
            passWizard(wizard2,i);
            int playerId = getIntent().getIntExtra("PlayerID",-999);

            i.putExtra("PlayerID",playerId);
            System.out.println("PLAYER ID" + playerId);
            mediaPlayer.stop();
            startActivity(i);
        });
        ImageButton card3 = findViewById(R.id.loadCardThree);
        card3.setOnClickListener(v -> {
            Intent i = new Intent(MainMenuActivity.this, MainActivity.class);
            if(!wizard3.has("id"))
                return;
            System.out.println(wizard3);
            passWizard(wizard3,i);
            int playerId = getIntent().getIntExtra("PlayerID",-999);

            i.putExtra("PlayerID",playerId);
            System.out.println("PLAYER ID" + playerId);
            mediaPlayer.stop();
            startActivity(i);
        });

        cardOneImage = findViewById(R.id.cardOneImage);
        cardTwoImage = findViewById(R.id.cardTwoImage);
        cardThreeImage = findViewById(R.id.cardThreeImage);



        deleteOne.setOnClickListener( v ->{
            try {
                deleteWizard(wizard1.getInt("id"),0);
                wizard1 = wizard2;
                wizard2 = wizard3;
                wizard3 = new JSONObject();
                cardNameOneText.setText("empty slot");
                cardOneImage.setImageResource(R.drawable.empty);
            } catch (JSONException | InterruptedException e) {
                System.out.println("WIZARD 1: "+wizard1 +"\n WIZARD 2: " + wizard2);
                e.printStackTrace();
            }
        });
        deleteTwo.setOnClickListener( v ->{
            try {
                deleteWizard(wizard2.getInt("id"),1);
                wizard2 = wizard3;
               wizard3 = new JSONObject();
                cardNameTwoText.setText("empty slot");
                cardTwoImage.setImageResource(R.drawable.empty);
            } catch (JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        deleteThree.setOnClickListener( v ->{
            try {
                deleteWizard(wizard3.getInt("id"),2);
                wizard3 = new JSONObject();
                cardNameThreeText.setText("empty slot");
                cardThreeImage.setImageResource(R.drawable.empty);
            } catch (JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        });


        cardNameOneText = findViewById(R.id.loadWizardOneText);cardOneHp = findViewById(R.id.loadWizardOneHp);cardOneMana = findViewById(R.id.loadWizardOneMana);cardOneXp = findViewById(R.id.loadWizardOneXP);cardOneLevel = findViewById(R.id.loadWizardOneLvl);
        cardNameTwoText = findViewById(R.id.loadWizardTwoText);cardTwoHp = findViewById(R.id.loadWizardTwoHp);cardTwoMana = findViewById(R.id.loadWizardTwoMana);cardTwoXp = findViewById(R.id.loadWizardTwoXP);cardTwoLevel = findViewById(R.id.loadWizardTwoLvl);
        cardNameThreeText = findViewById(R.id.loadWizardThreeText);cardThreeHp = findViewById(R.id.loadWizardThreeHp);cardThreeMana = findViewById(R.id.loadWizardThreeMana);cardThreeXp = findViewById(R.id.loadWizardThreeXP);cardThreeLevel = findViewById(R.id.loadWizardThreeLvl);

        buttons.add(button);buttons.add(button1);buttons.add(button2);buttons.add(button3);
        buttons.add(loadWizardClose);loadWizard.add(loadWizardClose);
        buttons.add(card1);loadWizard.add(card1);
        buttons.add(card2);loadWizard.add(card2);
        buttons.add(card3);loadWizard.add(card3);
        loadWizard.add(deleteOne);loadWizard.add(deleteTwo);loadWizard.add(deleteThree);buttons.add(deleteOne);buttons.add(deleteThree);buttons.add(deleteTwo);
        loadWizard.add(cardNameOneText);loadWizard.add(cardOneHp);loadWizard.add(cardOneMana);loadWizard.add(cardOneXp);loadWizard.add(cardOneLevel);
        loadWizard.add(cardNameTwoText);loadWizard.add(cardTwoHp);loadWizard.add(cardTwoMana);loadWizard.add(cardTwoXp);loadWizard.add(cardTwoLevel);
        loadWizard.add(cardNameThreeText);loadWizard.add(cardThreeHp);loadWizard.add(cardThreeMana);loadWizard.add(cardThreeXp);loadWizard.add(cardThreeLevel);

        loadWizard.add(cardOneImage);loadWizard.add(cardTwoImage);loadWizard.add(cardThreeImage);



    }
    private void setUpLoad(){
        try {
            if(wizard1.has("displayName")) {
                cardNameOneText.setText(wizard1.getString("displayName"));
                cardOneHp.setText(wizard1.getString("currhp") +"/"+wizard1.getString("maxHP"));
                cardOneMana.setText(wizard1.getString("curr_mana") +"/"+wizard1.getString("max_mana"));
                cardOneXp.setText(wizard1.getString("currXp") +"/"+wizard1.getString("nextLvlXp") +"XP");
                cardOneLevel.setText("Lvl." +wizard1.getString("lvl"));
            }
            else
                cardNameOneText.setText("empty slot");


            if(wizard2.has("displayName")) {
                cardNameTwoText.setText(wizard2.getString("displayName"));
                cardTwoHp.setText(wizard2.getString("currhp") +"/"+wizard2.getString("maxHP"));
                cardTwoMana.setText(wizard2.getString("curr_mana") +"/"+wizard2.getString("max_mana"));
                cardTwoXp.setText(wizard2.getString("currXp") +"/"+wizard2.getString("nextLvlXp") +"XP");
                cardTwoLevel.setText("Lvl." +wizard2.getString("lvl"));
            }
            else
                cardNameTwoText.setText("empty slot");


            if(wizard3.has("displayName")){
                cardNameThreeText.setText(wizard3.getString("displayName"));
                cardThreeHp.setText(wizard3.getString("currhp") +"/"+wizard3.getString("maxHP"));
                cardThreeMana.setText(wizard3.getString("curr_mana") +"/"+wizard3.getString("max_mana"));
                cardThreeXp.setText(wizard3.getString("currXp") +"/"+wizard3.getString("nextLvlXp") +"XP");
                cardThreeLevel.setText("Lvl." +wizard3.getString("lvl"));

            }
            else {
                cardNameThreeText.setText("empty slot");
            }

            Context context = getGameContext();
            int resID = 0;
            if(wizard1.has("extension")){
                resID = context.getResources().getIdentifier(wizard1.getString("extension"), "drawable", context.getPackageName());
                cardOneImage.setImageResource(resID);
                cardOneImage.getDrawable().setFilterBitmap(false);
            }else{
                cardOneImage.setImageResource(R.drawable.empty);
            }
            if(wizard2.has("extension")){
                resID = context.getResources().getIdentifier(wizard2.getString("extension"), "drawable", context.getPackageName());
                cardTwoImage.setImageResource(resID);
                cardTwoImage.getDrawable().setFilterBitmap(false);
            }else{
                cardTwoImage.setImageResource(R.drawable.empty);
            }
            if(wizard3.has("extension")){
                resID = context.getResources().getIdentifier(wizard3.getString("extension"), "drawable", context.getPackageName());
                cardThreeImage.setImageResource(resID);
                cardThreeImage.getDrawable().setFilterBitmap(false);
            }else{
                cardThreeImage.setImageResource(R.drawable.empty);
            }

            System.out.println("WIZARDS: " + wizard1 + "\n WIZARD 2" + wizard2 + "\n WIZARD 3" + wizard3);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}