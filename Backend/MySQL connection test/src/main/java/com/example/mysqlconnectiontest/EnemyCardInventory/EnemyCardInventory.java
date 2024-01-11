package com.example.mysqlconnectiontest.EnemyCardInventory;

import com.example.mysqlconnectiontest.Card.Card;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventoryKey;
import com.example.mysqlconnectiontest.Enemy.Enemy;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class EnemyCardInventory {

    @EmbeddedId
    EnemyCardInventoryKey id;

    @ManyToOne
    @MapsId("enemyid")
    @JoinColumn(name = "enemy_id")
    Enemy enemy;

    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(name = "card_id")
    Card card;

    boolean inDeck;

    public EnemyCardInventory(Enemy enemy, Card card, boolean inDeck) {
        this.enemy = enemy;
        this.card = card;
        this.inDeck = inDeck;
    }

    public EnemyCardInventory() {

    }
}
