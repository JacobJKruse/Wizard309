package com.example.mysqlconnectiontest.Wizard;

import com.example.mysqlconnectiontest.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Student {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String displayName;
    private int maxHP;
    @Column(name = "currhp")
    @JsonProperty("currhp")
    private int currHP;
    @Column(name = "max_mana")
    @JsonProperty("max_mana")
    private int maxMana;
    @Column(name = "curr_mana")
    @JsonProperty("curr_mana")
    private  int currMana;
    private int lvl;
    private int nextLvlXp;
    private int currXp;




    @ManyToOne
    @JsonIgnore
    private User user;

    public Student(String displayName, int maxHP, int currHP, int maxMana, int currMana, int lvl, int nextLvlXp, int currXp) {
        this.displayName = displayName;
        this.maxHP = maxHP;
        this.currHP = currHP;
        this.maxMana = maxMana;
        this.currMana = currMana;
        this.lvl = lvl;
        this.nextLvlXp = nextLvlXp;
        this.currXp = currXp;
    }

    public Student() {

    }


    public String getDisplayName() {
        return displayName;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getCurrHP() {
        return currHP;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getCurrMana() {
        return currMana;
    }

    public int getLvl() {
        return lvl;
    }

    public int getNextLvlXp() {
        return nextLvlXp;
    }

    public int getCurrXp() {
        return currXp;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public void setCurrHP(int currHP) {
        this.currHP = currHP;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public void setCurrMana(int currMana) {
        this.currMana = currMana;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public void setNextLvlXp(int nextLvlXp) {
        this.nextLvlXp = nextLvlXp;
    }

    public void setCurrXp(int currXp) {
        this.currXp = currXp;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }
    public void setUser(User user) {
        this.user = user;
    }



}
