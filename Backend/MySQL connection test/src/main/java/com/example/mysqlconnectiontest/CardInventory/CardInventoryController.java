package com.example.mysqlconnectiontest.CardInventory;

import com.example.mysqlconnectiontest.Card.Card;
import com.example.mysqlconnectiontest.Card.CardRepository;
import com.example.mysqlconnectiontest.Wizard.Wizard;
import com.example.mysqlconnectiontest.Wizard.WizardRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name="Card Inventory"  , description = "Manages the wizards' inventory of cards")
public class CardInventoryController {

    int MAXDECKSIZE = 20;

    @Autowired
    CardInventoryRepository cardInventoryRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    WizardRepository wizardRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(
        summary ="Retrieves all wizard/card pairs",
        description = "Retrieves all wizard/card pairs"
    )
    @GetMapping(path = "/CardInventory")
    List<CardInventory> getAllInventory() {
        return cardInventoryRepository.findAll();
    }

    //Gets a wizard's card inventory
    //Card inventory and deck are mutually exclusive
    @Operation(
            summary ="Retrieves all cards in a wizards inventory",
            description = "Retrieves all cards in a wizards inventory"
    )
    @GetMapping(path = "/CardInventory/{wizardId}")
    List<Card> getWizardsCard(@PathVariable int wizardId) {
        Wizard w = wizardRepository.findById(wizardId);
        List<CardInventory> l = cardInventoryRepository.getAllByWizard(w);

        List<Card> c = new ArrayList<>();
        for(int i = 0; i < l.size(); i++) {
            //if(l.get(i).isInDeck() == false) {
                c.add(l.get(i).card);
            //}
        }

        return c;
    }

    //Adds a card to wizard's card inventory
    @Operation(
            summary ="Adds a card to a wizard's card inventory",
            description = "Adds a card to a wizard's card inventory using the id's of a wizard and a card"
    )
    @PostMapping(path = "/CardInventory/add/{wizardId}/{cardId}")
    CardInventory addCardToInventory(@PathVariable int cardId, @PathVariable int wizardId) {
        Card card = cardRepository.findById(cardId);
        if(card == null) {
            return null;
        }
        Wizard wizard = wizardRepository.findById(wizardId);
        if(wizard == null) {
            return null;
        }

        //Creates a Card Inventory Object and adds it to the database
        CardInventoryKey x = new CardInventoryKey(wizardId, cardId);
        CardInventory ci = new CardInventory(wizard, card, false);
        ci.setId(x);
        wizard.addCard(ci);
        card.addOwner(ci);
        cardInventoryRepository.save(ci);


        return ci;
     }

     //Moves a card from inventory to deck
     @Operation(
             summary ="Moves a card from a wizard's inventory to deck",
             description = "Moves a card from a wizard's inventory to their deck to be used during combat phase"
     )
     @PutMapping(path = "/CardInventory/Deck/{wizardId}/{cardId}")
     String addCardToDeck(@PathVariable int cardId, @PathVariable int wizardId) {

         Card card = cardRepository.findById(cardId);
         if(card == null) {
             return null;
         }
         Wizard wizard = wizardRepository.findById(wizardId);
         if(wizard == null) {
             return null;
         }

         //If deck is full cancel request
         if(wizard.getDeckSize() >= MAXDECKSIZE) {
             return "Deck is full. Remove a card to add another one";
         }

         //finds the card in the inventory
        CardInventory ci = cardInventoryRepository.findByWizardAndCard(wizard, card);
         if(ci.isInDeck()) {
             return "Card is already in deck";
         }

         //moves card from inventory to deck by changing the boolaen value
         ci.setInDeck(true);
         cardInventoryRepository.save(ci);

         //increase deck size by 1
         wizard.setDeckSize(wizard.getDeckSize() + 1);
         wizardRepository.save(wizard);

         return success;
     }


     //Gets all the Cards in a Wizard's deck
     @Operation(
             summary ="Gets all the Cards in a Wizard's deck",
             description = "Gets all the Cards in a Wizard's deck"
     )
     @GetMapping(path = "/Deck/{wizardId}")
     List<Card> getDeck(@PathVariable int wizardId) {
         Wizard wizard = wizardRepository.findById(wizardId);
         if(wizard == null) {
             return null;
         }

         List<CardInventory> ci = wizard.getCardInventory();
         List<Card> c = new ArrayList<>();

         for (CardInventory cardInventory : ci) {
             if (cardInventory.inDeck) {
                 c.add(cardInventory.card);
             }
         }
         return c;
     }

    @Operation(
            summary ="Removes a Card from a Wizard's deck",
            description = "Removes a Card from a Wizard's deck"
    )
     @PutMapping(path = "/Deck/remove/{wizardId}/{cardId}")
     String deckToInventory(@PathVariable int wizardId, @PathVariable int cardId) {

         Card card = cardRepository.findById(cardId);
         if(card == null) {
             return null;
         }
         Wizard wizard = wizardRepository.findById(wizardId);
         if(wizard == null) {
             return null;
         }

         //finds the card in the deck
         CardInventory ci = cardInventoryRepository.findByWizardAndCard(wizard, card);
         if(ci == null) {
             return "Card not found";
         }
         if(ci.isInDeck() == false) {
             return "Card is already in inventory";
         }

         //moves card from deck to inventory by setting the boolean value to false
         ci.setInDeck(false);
         cardInventoryRepository.save(ci);

         //decreases deck size by 1
         wizard.setDeckSize(wizard.getDeckSize() - 1);
         wizardRepository.save(wizard);

         return success;

     }



    @Operation(
            summary ="Swaps the place of a card in the deck with a card from the inventory",
            description = "Swaps the place of a card in the deck with a card from the inventory"
    )
     @PutMapping(path = "/Deck/swap/{wizardId}/{deckCardId}/{inventoryCardId}")
     String deckAndInventoryCardSwap(@PathVariable int wizardId, @PathVariable int deckCardId, @PathVariable int inventoryCardId) {

        Wizard wizard = wizardRepository.findById(wizardId);
        if(wizard == null) {
            return "Wizard not found";
        }

        Card deckCard = cardRepository.findById(deckCardId);
        if(deckCard == null) {
            return "Card not found";
        }

        Card inventoryCard = cardRepository.findById(inventoryCardId);
        if(inventoryCard == null) {
            return "Card not found";
        }

        CardInventory c1 = cardInventoryRepository.findByWizardAndCard(wizard, deckCard);
        if(c1 == null) {
            return "Wizard does not own this card";
        }
        if(c1.isInDeck() == false) {
            return "deck card is in inventory";
        }

        CardInventory c2 = cardInventoryRepository.findByWizardAndCard(wizard, inventoryCard);
         if(c2 == null) {
             return "Wizard does not own this card";
         }
         if(c2.isInDeck() == true) {
             return "inventory card is in deck";
         }

         //updates the cards and saves it to the database
         c1.setInDeck(false);
         cardInventoryRepository.save(c1);
         c2.setInDeck(true);
         cardInventoryRepository.save(c2);

         return success;
     }


    @Operation(
            summary ="Deletes a card from a wizard's inventory",
            description = "Deletes a card from a wizard's inventory"
    )
     @DeleteMapping(path = "/Deck/{wizardId}/{cardId}")
     String removeCardFromWizard(@PathVariable int wizardId, @PathVariable int cardId ) {
        Wizard w = wizardRepository.findById(wizardId);
        if(w == null) {
            return failure;
        }
        Card c = cardRepository.findById(cardId);
        if(c == null) {
            return failure;
        }

        CardInventory ci = cardInventoryRepository.findByWizardAndCard(w, c);
        w.getCardInventory().remove(ci);
        c.getCardInventory().remove(ci);
        cardInventoryRepository.deleteByWizardAndCard(w, c);

        return success;
     }





}
