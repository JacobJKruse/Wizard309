package com.example.mysqlconnectiontest.Enemy;

import com.example.mysqlconnectiontest.Card.Card;
import com.example.mysqlconnectiontest.CardInventory.CardInventory;
import com.example.mysqlconnectiontest.CardInventory.CardInventoryRepository;
import com.example.mysqlconnectiontest.Element.Element;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventory;
import com.example.mysqlconnectiontest.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public abstract class Enemy implements Entities {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private int id;
    @Column(unique = true)
    @Expose
    private String displayName;
    @Expose
    private int maxHP;
    @Column(name = "currhp")
    @JsonProperty("currhp")
    @Expose
    private int currHP;
    @Column(name = "max_mana")
    @JsonProperty("max_mana")
    @Expose
    private int maxMana;
    @Column(name = "curr_mana")
    @JsonProperty("curr_mana")
    @Expose
    private  int currMana;
    @Expose
    private int lvl;
    private int xpDrop;
    private int goldDrop;
    @Expose
    private Element element;
    @Expose
    private String extension;
    private int deckSize;
    private int startX;
    private int startY;

    @OneToMany(mappedBy = "enemy")
    @JsonIgnore
    private List<EnemyCardInventory> enemyCardInventory;

    public Enemy(String displayName, int maxHP, int currHP, int maxMana, int currMana, int lvl, int xpDrop,
                  int goldDrop, Element element, String extension, int startX, int startY) {
        this.displayName = displayName;
        this.maxHP = maxHP;
        this.currHP = currHP;
        this.maxMana = maxMana;
        this.currMana = currMana;
        this.lvl = lvl;
        this.xpDrop = xpDrop;
        this.goldDrop = goldDrop;
        this.deckSize = 0;
        this.element = element;
        this.extension = extension;
        this.startX = startX;
        this.startY = startY;
        enemyCardInventory = new ArrayList<>();

    }

    public Enemy(){

    }

    public void addCard(EnemyCardInventory ci) {
        enemyCardInventory.add(ci);
    }

    public abstract void setDeck();

    public abstract void setDeckNoPersistence();

}
