package com.example.mysqlconnectiontest.Card;

import com.example.mysqlconnectiontest.CardInventory.CardInventory;
import com.example.mysqlconnectiontest.Element.Element;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventory;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@EqualsAndHashCode
public class
Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "spell_name", unique = true)
    @JsonProperty("spell_name")
    private String SpellName;
    @Column(name = "element")
    @JsonProperty("element")
    private com.example.mysqlconnectiontest.Element.Element Element;
    @Column(name = "attack_power")
    @JsonProperty("attack_power")
    private int AttackPower;
    @Column(name = "mana_cost")
    @JsonProperty("mana_cost")
    private int ManaCost;
    @Column(name = "attack_type")
    @JsonProperty("attack_type")
    private AttackType AttackType;
    private String extension;

    //@OneToOne
    //@JsonIgnore

    public Card(){

    }
    public Card(String SpellName,Element Element, int AttackPower,int ManaCost, AttackType AttackType, String extension){
        this.SpellName = SpellName;
        this.Element = Element;
        this.AttackPower = AttackPower;
        this.ManaCost = ManaCost;
        this.AttackType = AttackType;
        this.extension = extension;

        cardInventory = new ArrayList<>();
    }

    @OneToMany(mappedBy = "card")
    @JsonIgnore
    private List<CardInventory> cardInventory;

    @OneToMany(mappedBy = "card")
    @JsonIgnore
    private List<EnemyCardInventory> enemyCardInventory;

    public void addOwner(CardInventory ci) {
        cardInventory.add(ci);
    }

    public void addEnemy (EnemyCardInventory ci) {enemyCardInventory.add(ci);}
}
