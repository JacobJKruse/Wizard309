package com.example.mysqlconnectiontest.websocket;


import com.example.mysqlconnectiontest.Battle.PlayerAttack;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import com.google.gson.Gson;

public class PlayerAttackDecoder implements Decoder.Text<PlayerAttack> {

    private static Gson gson = new Gson();

    @Override
    public PlayerAttack decode(String s) throws DecodeException {
        return gson.fromJson(s, PlayerAttack.class);
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }
}
