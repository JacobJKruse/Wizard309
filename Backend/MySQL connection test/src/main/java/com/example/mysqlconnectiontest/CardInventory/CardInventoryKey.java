package com.example.mysqlconnectiontest.CardInventory;

import io.swagger.v3.oas.annotations.tags.Tag;
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

public class CardInventoryKey implements Serializable {

    @Column(name = "wizard_id")
    int wizardId;

    @Column(name = "card_id")
    int cardId;


    public CardInventoryKey(int wizardId, int cardId) {
        this.wizardId = wizardId;
        this.cardId = cardId;
    }

    public CardInventoryKey() {

    }

}
