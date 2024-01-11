package com.example.springboot.wizard;

import lombok.Getter;

@Getter
public class Wizard {
    private Long playerID;
    private String name;
    private Integer level;
    private Integer health;
    private Integer mana;
    private String school;

    public Wizard() {
    }

    public Wizard(Long playerID, String name, Integer level, Integer health, Integer mana, String school) {
        this.playerID = playerID;
        this.name = name;
        this.level = level;
        this.health = health;
        this.mana = mana;
        this.school = school;
    }

    public void setPlayerID(Long playerID) {
        this.playerID = playerID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public void setMana(Integer mana) {
        this.mana = mana;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "Wizard{" +
                "playerID=" + playerID +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", health=" + health +
                ", mana=" + mana +
                ", school='" + school + '\'' +
                '}';
    }
}
