package com.example.wizard309.gamestates;

import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.wizard309.main.Game;
import com.example.wizard309.main.MainActivity;
import com.example.wizard309.volley.net_utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * public abstract for base state this implements the game
 */
public abstract class BaseState {
    protected Game game;
    private static JSONObject wiz = new JSONObject();


    public BaseState(Game game){

        this.game = game;
    }

    public Game getGame(){
        return game;
    }



}
