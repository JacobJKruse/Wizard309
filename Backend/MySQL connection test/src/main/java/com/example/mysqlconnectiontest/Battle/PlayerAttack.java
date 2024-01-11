package com.example.mysqlconnectiontest.Battle;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerAttack {
    private int wizardId;
    private int enemyId;
    private int cardId;

    public PlayerAttack(int wizardId,  int enemyId, int cardId) {
        this.wizardId = wizardId;
        this.enemyId = enemyId;
        this.cardId = cardId;
    }

    public PlayerAttack() {

    }
}
