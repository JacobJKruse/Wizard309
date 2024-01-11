package com.example.mysqlconnectiontest.Wizard;

import com.example.mysqlconnectiontest.CardInventory.CardInventory;
import com.example.mysqlconnectiontest.CardInventory.CardInventoryKey;
import com.example.mysqlconnectiontest.Element.Element;
import com.example.mysqlconnectiontest.Entities;
import com.example.mysqlconnectiontest.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Wizard implements Entities {


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
    private int nextLvlXp;
    private int currXp;
    @Expose
    private Element element;
    @Expose
    private String extension;
    private int deckSize;
    private int wallet;

    @JsonIgnore
    @Lob
    private Blob avatar;


    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "wizard")
    @JsonIgnore
    private List<CardInventory> cardInventory;

    public Wizard(String displayName, int maxHP, int currHP, int maxMana, int currMana, int lvl, int nextLvlXp,
                  int currXp, Element element, int wallet) {
        this.displayName = displayName;
        this.maxHP = maxHP;
        this.currHP = currHP;
        this.maxMana = maxMana;
        this.currMana = currMana;
        this.lvl = lvl;
        this.nextLvlXp = nextLvlXp;
        this.currXp = currXp;
        this.deckSize = 0;
        this.element = element;
        this.wallet = wallet;

        cardInventory = new ArrayList<>();
    }

    public Wizard() {

    }


    public void addCard(CardInventory ci) {
        cardInventory.add(ci);
    }



}
