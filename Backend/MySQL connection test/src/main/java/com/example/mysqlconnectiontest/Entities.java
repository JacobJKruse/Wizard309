package com.example.mysqlconnectiontest;


import com.example.mysqlconnectiontest.Element.Element;

public interface Entities {

    public int getId();
    public String getDisplayName();
    public int getMaxHP();
    public int getCurrHP();
    public int getMaxMana();
    public int getCurrMana();
    public int getLvl();
    public int getDeckSize();
    public Element getElement();

    public void setDisplayName(String displayName);
    public void setMaxHP(int maxHP);
    public void setCurrHP(int currHP);
    public void setMaxMana(int maxMana);
    public void setCurrMana(int currMana);
    public void setLvl(int lvl);
    public void setDeckSize(int deckSize);
    public void setElement(Element element);


}
