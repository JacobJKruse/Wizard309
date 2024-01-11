package com.example.wizard309.gamestates;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wizard309.helpers.interfaces.GameStatesInterface;
import com.example.wizard309.main.Game;
import com.example.wizard309.main.MainActivity;
import com.example.wizard309.ui.ButtonImages;
import com.example.wizard309.ui.CustomButton;
import com.example.wizard309.volley.net_utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * custom main menu state - NOT used just for testing purposes
 */
public class MainMenu extends BaseState implements GameStatesInterface {

    private Paint paint;

//    private JSONObject wizard = new JSONObject();
//      String url = Const.URL_WIZARDS_JSON_ARRAY;
//    RequestQueue queue = Volley.newRequestQueue(MainActivity.getGameContext());

    private boolean setWizardJson = false;
    private CustomButton buttonStart;
    private CustomButton aKey;


    private String userInput = "test";



    private boolean showKeyboard = false;

    public MainMenu(Game game){
        super(game);
        paint = new Paint();
        paint.setTextSize(60);
        paint.setColor(Color.WHITE);

        //getWizardJson();
        //getWizardJson();
//        buttonStart = new CustomButton(300, 200, ButtonImages.MAINMENU_START.getWidth(), ButtonImages.MAINMENU_START.getHeight());
        //aKey = new CustomButton(32,MainActivity.screenHeight-100, ButtonImages.KEYA.getWidth(), ButtonImages.KEYA.getHeight());


    }

    @Override
    public void update(double delta) {
        //System.out.println("UPDATE" + wizard);
        if(!setWizardJson){
           // postWizardJson(wizard);
            setWizardJson = true;
        }
    }

    @Override
    public void render(Canvas c) {

        c.drawText("Menu", 800, 200, paint);
        c.drawBitmap(
                ButtonImages.MAINMENU_START.getButtonImage(buttonStart.isClicked()),
                buttonStart.getHitbox().left,
                buttonStart.getHitbox().top,
                null
        );

        }




    @Override
    public void touchEvents(MotionEvent event) {
//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (isIn(event, buttonStart)) {
//                buttonStart.setClicked(true);
//            }
//        }
//        else if(event.getAction() == MotionEvent.ACTION_UP){
//
//            if(isIn(event,buttonStart)){
//                if(buttonStart.isClicked()) {
//                    //showKeyboard = !showKeyboard; experimenting with keyboards
//                    game.setCurrentState(Game.GameState.PLAYING);
//
//                }
//
//            }
//
//
//            buttonStart.setClicked(false);
//            }





    }

//    private void setWizardJsonObject(JSONObject obj, JSONObject response){
//        try {
//            obj.put("id", response.getString("id"));
//            obj.put("displayName", "philip");
//            obj.put("maxHP", 972/*response.getString("maxHP")*/);
//            obj.put("lvl", response.getString("lvl"));
//            obj.put("nextLvlXp", response.getString("nextLvlXp"));
//            obj.put("currXp", response.getString("currXp"));
//            obj.put("currhp", response.getString("currhp"));
//            obj.put("max_mana", response.getString("max_mana"));
//            obj.put("curr_mana", response.getString("curr_mana"));
//            System.out.println(obj);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//    private void getWizardJson(){
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
//            for (int i = 0; i < response.length(); i++) {
//                try {
//                    JSONObject responseObj = response.getJSONObject(i);
//                    System.out.println(responseObj);
//                    if(responseObj.getInt("id") == 18){
//                        setWizardJsonObject(wizard,responseObj);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, error -> System.out.println(error));
//
//        queue.add(jsonArrayRequest);
//
//    }
//
//    //USE WHEN MAKING A NEW WIZARD OR UPDATING THEM
//    private void postWizardJson(JSONObject newWizard){
//        //POST OBJECT
//        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, newWizard,
//                response -> {
//                    System.out.println("WIZARD JSON SET");
//                }, error -> {
//            error.printStackTrace();
//        });
//
//        queue.add(jsonRequest);
//    }
//
//
}
