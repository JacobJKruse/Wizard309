package com.example.h2_database_and_classes.Card;

import com.example.h2_database_and_classes.Character.Character;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Id;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String SpellName;
    private com.example.h2_database_and_classes.Card.Element Element;
    private int AttackPower;
    private int ManaCost;
    private com.example.h2_database_and_classes.Card.AttackType AttackType;

    @OneToOne
    @JsonIgnore
    private Character character;
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
    public int getCardId() {
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
