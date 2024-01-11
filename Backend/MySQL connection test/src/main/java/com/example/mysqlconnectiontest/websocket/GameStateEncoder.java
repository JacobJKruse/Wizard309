package com.example.mysqlconnectiontest.websocket;

import com.example.mysqlconnectiontest.Battle.GameState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

public class GameStateEncoder implements Encoder.Text<GameState> {

    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();


    @Override
    public String encode(GameState gameState) throws EncodeException {
        return gson.toJson(gameState);
    }
}
