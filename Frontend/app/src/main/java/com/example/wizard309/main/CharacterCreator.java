package com.example.wizard309.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wizard309.R;
import com.example.wizard309.volley.net_utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * character creator layout
 */
public class CharacterCreator extends AppCompatActivity {
    private static Context gameContext;
    RequestQueue queue;
    //String url = Const.URL_JSON_ARRAY_LOCAL_PC;
    String url = Const.URL_WIZARDS_JSON_ARRAY;
    EditText name;
    TextView element,elementBack;
    ImageView styleBack,classBack,styleImage,classImage;
    ImageButton styleLeft,styleRight,classLeft,classRight,finishBack;
    List<ImageView> images = new ArrayList<>();
    List<ImageButton> buttons = new ArrayList<>();

    int styleIndex = 0;
    List<Integer> styleImages = new ArrayList<>();
    int classIndex = 0;
    List<Integer> classImages = new ArrayList<>();
    private String currentElement = "Fire";

    /**
     * on create layout constructor
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameContext = this;
        queue = Volley.newRequestQueue(MainMenuActivity.getGameContext());
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Hide the system bars.
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        setContentView(R.layout.activity_character_creator);

        name = findViewById(R.id.name);
        styleBack = findViewById(R.id.styleBack);
        classBack = findViewById(R.id.classBack);
        styleImage = findViewById(R.id.styleImage);
        classImage = findViewById(R.id.classImage);
        element = findViewById((R.id.classText));
        elementBack = findViewById(R.id.classText2);

        images.add(styleBack);images.add(classBack);images.add(styleImage);images.add(classImage);
        setUpImages();
        handleButtons();
        setFilers(images,buttons);

        System.out.println(getIntent().getIntExtra("PlayerID",-99));
    }

    private void setFilers(List<ImageView> i,List<ImageButton> b){
        for(ImageView x : i){
            x.getDrawable().setFilterBitmap(false);
        }
        for(ImageButton x : b){
            x.getDrawable().setFilterBitmap(false);
        }
    }

    /**
     * @return the current gameContext
     */
    public static Context getGameContext() {return gameContext;}
    private void setUpImages(){
        styleImages.add(R.drawable.fire_icon);
        styleImages.add(R.drawable.ice_icon);
        styleImages.add(R.drawable.death_icon);
        styleImages.add(R.drawable.life_icon);
        styleImages.add(R.drawable.storm_icon);
        styleImages.add(R.drawable.earth_icon);
        classImages.add(R.drawable.fire_element);
        classImages.add(R.drawable.ice_element);
        classImages.add(R.drawable.death_element);
        classImages.add(R.drawable.life_element);
        classImages.add(R.drawable.storm_element);
        classImages.add(R.drawable.earth_element);

        updateImages();
    }
    private void handleButtons(){

        styleLeft = findViewById(R.id.styleLeft);
        styleLeft.setOnClickListener(v->{
            if(styleIndex > 0)
                styleIndex--;
            else
                styleIndex = styleImages.size() - 1;
            updateImages();
        });
        styleRight=findViewById(R.id.styleRight);
        styleRight.setOnClickListener(v ->{
            if(styleIndex == styleImages.size()  - 1)
                styleIndex = 0;
            else
                styleIndex++;
            updateImages();
        });
        classLeft = findViewById(R.id.classLeft);
        classLeft.setOnClickListener(v ->{
            if(classIndex > 0)
                classIndex--;
            else
                classIndex = classImages.size() - 1;
            updateImages();
        });
        classRight=findViewById(R.id.classRight);
        classRight.setOnClickListener(v -> {
            if(classIndex == classImages.size() - 1)
                classIndex = 0;
            else
                classIndex++;
            updateImages();
        });
        finishBack = findViewById(R.id.finishButton);
        finishBack.setOnClickListener(v -> {
            if(!name.getText().toString().trim().isEmpty()) {
                int playerId = getIntent().getIntExtra("PlayerID", -999);
                JSONObject wiz = createWizard();
                Intent intent = new Intent(CharacterCreator.this, MainMenuActivity.class);
                intent.putExtra("PlayerID", playerId);
//                try {
//                    intent.putExtra("displayName",wiz.getString("displayName"));
//                    intent.putExtra("maxHP",wiz.getInt("maxHP"));
//                    intent.putExtra("lvl",wiz.getInt("lvl"));
//                    intent.putExtra("nextLvlXp",wiz.getInt("nextLvlXp"));
//                    intent.putExtra("currXp",wiz.getInt("currXp"));
//                    intent.putExtra("element", wiz.getString("element"));
//                    intent.putExtra("extension", wiz.getString("extension"));
//                    intent.putExtra("deckSize",wiz.getInt("deckSize"));
//                    intent.putExtra("currhp",wiz.getInt("currhp"));
//                    intent.putExtra("max_mana",wiz.getInt("max_mana"));
//                    intent.putExtra("curr_mana",wiz.getInt("curr_mana"));
//                    //add rest
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
                //}
                //WRITE CODE TO PARSE ALL THE WIZARD DATA INTO NEW WIZARD IN MAINACTIVITY
                startActivity(intent);
            }
        });
        buttons.add(styleLeft);buttons.add(styleRight);buttons.add(classLeft);buttons.add(classRight);buttons.add(finishBack);
    }

    private void updateImages(){
        styleImage.setImageResource(styleImages.get(styleIndex));
        styleImage.getDrawable().setFilterBitmap(false);
        classImage.setImageResource(classImages.get(classIndex));
        classImage.getDrawable().setFilterBitmap(false);
        switch (classIndex){
            case 0:
                currentElement = "Fire";
                element.setText("Fire");
                elementBack.setText("Fire");
                break;
            case 1:
                currentElement = "Ice";
                element.setText("Ice");
                elementBack.setText("Ice");
                break;
            case 2:
                currentElement = "Death";
                element.setText("Death");
                elementBack.setText("Death");
                break;
            case 3:
                currentElement = "Life";
                element.setText("Life");
                elementBack.setText("Life");
                break;
            case 4:
                currentElement = "Storm";
                element.setText("Storm");
                elementBack.setText("Storm");
                break;
            case 5:
                currentElement = "Earth";
                element.setText("Earth");
                elementBack.setText("Earth");
                break;
        }
    }
    private JSONObject createWizard(){
        JSONObject wiz = new JSONObject();
        TypedValue value = new TypedValue();
        getResources().getValue(styleImages.get(styleIndex), value, true);
        String extention = (String) value.string.subSequence(13,value.string.length());
        extention = extention.substring(0,extention.length()-4);
        System.out.println(extention);
        String newUrl = url +"/" + getIntent().getIntExtra("PlayerID",-99)+"/"+extention;
        //newUrl = "http://10.0.2.2:8080/wizards/9/necro_icon";
        try {
            if(name.getText().toString().trim().isEmpty()){
                wiz.put("displayName","new wizard");

            }else{
                wiz.put("displayName",name.getText());
            }
            wiz.put("extension", extention);
            wiz.put("maxHP",100);
            wiz.put("lvl",1);
            wiz.put("nextLvlXp",100);
            wiz.put("currXp",0);
            wiz.put("currhp",100);
            wiz.put("max_mana",100);
            wiz.put("curr_mana",100);
            wiz.put("element",currentElement);
            wiz.put("deckSize",0);
            wiz.put("wallet",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(wiz);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, newUrl, wiz, response -> {
        }, error -> System.out.println("CREATE ERROR"));
        queue.add(jsonArrayRequest);
        return wiz;
    }

}