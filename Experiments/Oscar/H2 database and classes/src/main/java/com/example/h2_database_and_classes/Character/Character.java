package com.example.h2_database_and_classes.Character;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import com.example.h2_database_and_classes.Card.Card;


@Entity
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int UserRefID;
    private String DisplayName;
    private int MaxHP;
    private int CurrHP;
    private int MaxMana;
    private int CurrMana;
    private int Lvl;
    private int NextLvlXP;
    private int CurrXP;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="card_id")
    private Card card;
    public Character(){

    }
    public Character(int UserRefID,String DisplayName,int MaxHP,int CurrHP,int MaxMana,int CurrMana, int Lvl,
                     int NextLvlXP,int CurrXP){
        this.UserRefID = UserRefID;
        this.DisplayName = DisplayName;
        this.MaxHP = MaxHP;
        this.CurrHP = CurrHP;
        this.MaxMana = MaxMana;
        this.CurrMana = CurrMana;
        this.Lvl = Lvl;
        this.NextLvlXP = NextLvlXP;
        this.CurrXP = CurrXP;
    }

    //region Get Methods
    public int getCharID() {
        return id;
    }

    public int getUserRefID() {
        return UserRefID;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public int getMaxHP() {
        return MaxHP;
    }

    public int getCurrHP() {
        return CurrHP;
    }

    public int getMaxMana() {
        return MaxMana;
    }

    public int getCurrMana() {
        return CurrMana;
    }

    public int getLvl() {
        return Lvl;
    }

    public int getCurrXP() {
        return CurrXP;
    }
    public int getNextLvlXP(){
        return NextLvlXP;
    }


    //endregion
    //region Set Methods
    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public void setMaxHP(int maxHP) {
        MaxHP = maxHP;
    }

    public void setCurrHP(int currHP) {
        CurrHP = currHP;
    }

    public void setMaxMana(int maxMana) {
        MaxMana = maxMana;
    }

    public void setCurrMana(int currMana) {
        CurrMana = currMana;
    }

    public void setLvl(int lvl) {
        Lvl = lvl;
    }

    public void setCurrXP(int currXP) {
        CurrXP = currXP;
    }

    public void setNextLvlXP(int nextLvlXP) {
        NextLvlXP = nextLvlXP;
    }

    //endregion
}
