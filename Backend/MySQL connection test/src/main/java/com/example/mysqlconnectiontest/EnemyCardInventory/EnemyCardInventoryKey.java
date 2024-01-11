package com.example.mysqlconnectiontest.EnemyCardInventory;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class EnemyCardInventoryKey implements Serializable {

    @Column(name = "enemy_id")
    int enemyId;

    @Column(name = "card_id")
    int cardId;

    public EnemyCardInventoryKey(int enemyId, int cardId) {
        this.enemyId = enemyId;
        this.cardId = cardId;
    }

    public EnemyCardInventoryKey() {

    }

}
