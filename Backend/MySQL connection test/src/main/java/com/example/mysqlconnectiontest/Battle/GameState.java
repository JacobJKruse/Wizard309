package com.example.mysqlconnectiontest.Battle;

import com.example.mysqlconnectiontest.Enemy.Enemy;
import com.example.mysqlconnectiontest.Entities;
import com.example.mysqlconnectiontest.Wizard.Wizard;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class GameState {

    @Expose
    String turn;
    @Expose
    ArrayList<Wizard> players;
    @Expose
    ArrayList<Enemy> enemies;

    public GameState(String turn, ArrayList<Wizard> players, ArrayList<Enemy> enemies) {
        this.turn = turn;
        this.players = players;
        this.enemies = enemies;
    }

    public GameState() {

    }
}
