package com.example.mysqlconnectiontest.Enemy;

import com.example.mysqlconnectiontest.Card.Card;
import com.example.mysqlconnectiontest.Card.CardRepository;
import com.example.mysqlconnectiontest.CardInventory.CardInventory;
import com.example.mysqlconnectiontest.CardInventory.CardInventoryRepository;
import com.example.mysqlconnectiontest.Element.Element;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventory;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventoryController;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventoryKey;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventoryRepository;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Entity
@Component
public class BasicEnemy extends Enemy {

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




    public BasicEnemy() {

        setCurrHP(100);
        setMaxHP(100);
        setDeckSize(0);
        setDisplayName("enemy");
        setElement(Element.Fire);
        setExtension("test_guy");
        setGoldDrop(20);
        setLvl(1);
        setCurrHP(100);
        setMaxMana(100);
        setCurrMana(100);
        setXpDrop(100);
        setStartX(2300);
        setStartY(2300);

    }


    @Override
    public void setDeck() {
        List<Card> cards = new ArrayList<>();
        cards.add(cardRepository.findById(8));
        cards.add(cardRepository.findById(9));
        cards.add(cardRepository.findById(10));
        cards.add(cardRepository.findById(11));
        cards.add(cardRepository.findById(12));
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

    }
}
