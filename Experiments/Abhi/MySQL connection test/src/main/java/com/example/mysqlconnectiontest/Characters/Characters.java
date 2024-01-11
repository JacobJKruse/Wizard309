package com.example.mysqlconnectiontest.Characters;

import com.example.mysqlconnectiontest.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.example.mysqlconnectiontest.Card.Card;


@Entity
public class Characters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userRefID;
    private String displayName;
    private int maxHP;
    private int currHP;
    private int maxMana;
    private int currMana;
    private int lvl;
    private int nextLvlXP;
    private int currXP;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CardId")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Characters(int userRefID, String displayName, int maxHP, int currHP, int maxMana, int currMana, int lvl,
                      int nextLvlXP, int currXP){
        this.userRefID = userRefID;
        this.displayName = displayName;
        this.maxHP = maxHP;
        this.currHP = currHP;
        this.maxMana = maxMana;
        this.currMana = currMana;
        this.lvl = lvl;
        this.nextLvlXP = nextLvlXP;
        this.currXP = currXP;
    }
    public Characters(String displayName, int lvl){
        this.displayName =  displayName;
        this.lvl = lvl;

    }

    public Characters() {

    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    //region Get Methods
    public int getID() {
        return id;
    }

//    public int getUserRefID() {
//        return UserRefID;
//    }
//
//    public String getDisplayName() {
//        return DisplayName;
//    }
//
//    public int getMaxHP() {
//        return MaxHP;
//    }
//
//    public int getCurrHP() {
//        return CurrHP;
//    }
//
//    public int getMaxMana() {
//        return MaxMana;
//    }
//
//    public int getCurrMana() {
//        return CurrMana;
//    }
//
//    public int getLvl() {
//        return Lvl;
//    }
//
//    public int getCurrXP() {
//        return CurrXP;
//    }
//    public int getNextLvlXP(){
//        return NextLvlXP;
//    }
    public Card getCard(){return card;}


    //endregion
    //region Set Methods    public void setID(int id) {
    //        this.id = id;
    //    }
    //
    //    public void setUserRefID(int UserRefID) {this.UserRefID = UserRefID;}
    //    public void setDisplayName(String displayName) {
    //        DisplayName = displayName;
    //    }
    //
    //    public void setMaxHP(int maxHP) {
    //        MaxHP = maxHP;
    //    }
    //
    //    public void setCurrHP(int currHP) {
    //        CurrHP = currHP;
    //    }
    //
    //    public void setMaxMana(int maxMana) {
    //        MaxMana = maxMana;
    //    }
    //
    //    public void setCurrMana(int currMana) {
    //        CurrMana = currMana;
    //    }
    //
    //    public void setLvl(int lvl) {
    //        Lvl = lvl;
    //    }
    //
    //    public void setCurrXP(int currXP) {
    //        CurrXP = currXP;
    //    }
    //
    //    public void setNextLvlXP(int nextLvlXP) {
    //        NextLvlXP = nextLvlXP;
    //    }

    public void setCard(Card card) {this.card = card;}

    public void setUser(User user) {
        this.user = user;
    }

}
