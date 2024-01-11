package com.example.mysqlconnectiontest.CardInventory;

import com.example.mysqlconnectiontest.Card.Card;
import com.example.mysqlconnectiontest.Wizard.Wizard;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class CardInventory {

    @EmbeddedId
    CardInventoryKey id;

    @ManyToOne
    @MapsId("wizardId")
    @JoinColumn(name = "wizard_id")
    Wizard wizard;

    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(name = "card_id")
    Card card;

    boolean inDeck;

    public CardInventory(Wizard wizard, Card card, boolean inDeck) {
        this.wizard = wizard;
        this.card = card;
        this.inDeck = inDeck;
    }

    public CardInventory() {

    }
}
