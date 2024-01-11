package com.example.mysqlconnectiontest.Enemy;

import com.example.mysqlconnectiontest.Card.Card;
import com.example.mysqlconnectiontest.Card.CardRepository;
import com.example.mysqlconnectiontest.Element.Element;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventory;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventoryKey;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventoryRepository;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Entity
@Component
public class Wisp extends Enemy{

    public static CardRepository cardRepository;

    @Autowired
    public void setCardRepository(CardRepository repo) {
        cardRepository = repo;
    }

    public static EnemyCardInventoryRepository enemyCardInventoryRepository;

    @Autowired
    public void setEnemyCardInventory(EnemyCardInventoryRepository repo) {
        enemyCardInventoryRepository = repo;
    }

    public Wisp() {
        super.setDisplayName("Wisp");
        super.setMaxHP(100);
        super.setCurrHP(100);
        super.setMaxMana(123);
        super.setCurrMana(123);
        super.setLvl(1);
        super.setXpDrop(100);
        super.setGoldDrop(12);
        super.setDeckSize(0);
        super.setElement(Element.Fire);
        super.setExtension("wisp");
        super.setStartX(2100);
        super.setStartY(2200);
    }

    @Override
    public void setDeck() {
        List<Card> cards = new ArrayList<>();
        cards.add(cardRepository.findById(8));
        List<EnemyCardInventory> deck = new ArrayList<>();
        for(Card c: cards) {
            EnemyCardInventory eci = new EnemyCardInventory(this, c, true);
            EnemyCardInventoryKey x = new EnemyCardInventoryKey(this.getId(), c.getId());
            eci.setId(x);
            deck.add(eci);

            enemyCardInventoryRepository.save(eci);
        }
        setEnemyCardInventory(deck);
        this.setDeckSize(cards.size());
    }

    @Override
    public void setDeckNoPersistence() {
        List<Card> cards = new ArrayList<>();
        cards.add(cardRepository.findById(8));
        cards.add(cardRepository.findById(9));
        cards.add(cardRepository.findById(10));
        cards.add(cardRepository.findById(11));
        cards.add(cardRepository.findById(12));
        cards.add(cardRepository.findById(13));
        cards.add(cardRepository.findById(14));

        cards.add(cardRepository.findById(16));
        cards.add(cardRepository.findById(17));
        cards.add(cardRepository.findById(18));
        cards.add(cardRepository.findById(19));
        cards.add(cardRepository.findById(20));
        cards.add(cardRepository.findById(21));
        cards.add(cardRepository.findById(22));

        List<EnemyCardInventory> deck = new ArrayList<>();
        for(Card c: cards) {
            EnemyCardInventory eci = new EnemyCardInventory(this, c, true);
            EnemyCardInventoryKey x = new EnemyCardInventoryKey(this.getId(), c.getId());
            eci.setId(x);
            deck.add(eci);
        }
        setEnemyCardInventory(deck);
        this.setDeckSize(cards.size());
    }



}
