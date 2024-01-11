package com.example.mysqlconnectiontest.Card;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class
Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "spell_name")
    @JsonProperty("spell_name")
    private String SpellName;
    @Column(name = "element")
    @JsonProperty("element")
    private Element Element;
    @Column(name = "attack_power")
    @JsonProperty("attack_power")
    private int AttackPower;
    @Column(name = "mana_cost")
    @JsonProperty("mana_cost")
    private int ManaCost;
    @Column(name = "attack_type")
    @JsonProperty("attack_type")
    private AttackType AttackType;

    //@OneToOne
    //@JsonIgnore

    public Card(){

    }
    public Card(String SpellName,Element Element, int AttackPower,int ManaCost, AttackType AttackType){
        this.SpellName = SpellName;
        this.Element = Element;
        this.AttackPower = AttackPower;
        this.ManaCost = ManaCost;
        this.AttackType = AttackType;
    }

    //region Get Methods
    public int getId() {
        return id;
    }
    public String getSpellName(){
        return SpellName;
    }
    public Element getElement() {
        return Element;
    }
    public int getAttackPower(){
        return AttackPower;
    }
    public int getManaCost(){
        return ManaCost;
    }
    public AttackType getAttackType() {
        return AttackType;
    }
    //endregion


    //region Set Methods

    public void setId(int id) {
        this.id = id;
    }

    public void setSpellName(String spellName) {
        SpellName = spellName;
    }

    public void setElement(Element element) {
        Element = element;
    }
    public void setAttackPower(int attackPower) {
        AttackPower = attackPower;
    }

    public void setManaCost(int manaCost) {
        ManaCost = manaCost;
    }

    public void setAttackType(AttackType attackType) {
        AttackType = attackType;
    }
    //endregion


}
