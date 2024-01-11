package com.example.mysqlconnectiontest.websocket;


import com.example.mysqlconnectiontest.Battle.PlayerAttack;
import com.google.gson.Gson;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

public class PlayerAttackEncoder implements Encoder.Text<PlayerAttack> {

    private static Gson gson = new Gson();

    @Override
    public String encode(PlayerAttack attack) throws EncodeException {
        return gson.toJson(attack);
    }


}
