package com.example.mysqlconnectiontest.Wizard;

import com.example.mysqlconnectiontest.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Blob;

@Getter
@Setter
@Entity
public class Wizard {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String extension;

    @JsonIgnore
    @Lob
    private Blob avatar;


    @ManyToOne
    @JsonIgnore
    private User user;

    public Wizard(String displayName, int maxHP, int currHP, int maxMana, int currMana, int lvl, int nextLvlXp, int currXp) {
        this.displayName = displayName;
        this.maxHP = maxHP;
        this.currHP = currHP;
        this.maxMana = maxMana;
        this.currMana = currMana;
        this.lvl = lvl;
        this.nextLvlXp = nextLvlXp;
        this.currXp = currXp;
    }

    public Wizard() {

    }


    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setAvatar(Blob avatar) {
        this.avatar = avatar;
    }
}
