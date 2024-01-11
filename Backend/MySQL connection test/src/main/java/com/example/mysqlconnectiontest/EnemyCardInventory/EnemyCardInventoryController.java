package com.example.mysqlconnectiontest.EnemyCardInventory;

import com.example.mysqlconnectiontest.Card.Card;
import com.example.mysqlconnectiontest.Card.CardRepository;
import com.example.mysqlconnectiontest.CardInventory.CardInventory;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventory;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventoryKey;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventoryRepository;
import com.example.mysqlconnectiontest.Enemy.Enemy;
import com.example.mysqlconnectiontest.Enemy.EnemyRepository;
import com.example.mysqlconnectiontest.Wizard.Wizard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name="Enemy Card Inventory", description = "Manages the Enemies' inventory of cards")
public class EnemyCardInventoryController {

    int MAXDECKSIZE = 64;

    @Autowired
    EnemyCardInventoryRepository enemyCardInventoryRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    EnemyRepository enemyRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @Operation(
            summary ="Retrieves all Enemy/card pairs",
            description = "Retrieves all Enemy/card pairs"
    )
    @GetMapping(path = "/EnemyCardInventory")
    List<EnemyCardInventory> getAllInventory() {
        return enemyCardInventoryRepository.findAll();
    }

    //Gets an enemy's card inventory
    //Card inventory and deck are mutually exclusive
    @Operation(
            summary ="Retrieves all cards in an enemy's inventory",
            description = "Retrieves all cards in an enemy's inventory"
    )
    @GetMapping(path = "/EnemyCardInventory/{enemyId}")
    List<Card> getEnemyCard(@PathVariable int enemyId) {
        Enemy e = enemyRepository.findById(enemyId);
        List<EnemyCardInventory> l = enemyCardInventoryRepository.getAllByEnemy(e);

        List<Card> c = new ArrayList<>();
        for(int i = 0; i < l.size(); i++) {
            if(l.get(i).isInDeck() == true) {
                c.add(l.get(i).card);
            }
        }

        return c;
    }


    public void  addDeck(List<Card> d, Enemy e) {
        for (Card card : d) {
            EnemyCardInventory eci = new EnemyCardInventory(e, card, true);
            enemyCardInventoryRepository.save(eci);
        }
    }

    //Adds a card to Enemy's card inventory
    @Operation(
            summary ="Adds a card to an enemy's card inventory",
            description = "Adds a card to an enemy's card inventory using the id's of a wizard and a card"
    )
    @PostMapping(path = "/EnemyCardInventory/add/{enemyId}/{cardId}")
    EnemyCardInventory addCardToInventory(@PathVariable int cardId, @PathVariable int enemyId) {
        Card card = cardRepository.findById(cardId);
        if(card == null) {
            return null;
        }
        Enemy  enemy = enemyRepository.findById(enemyId);
        if(enemy == null) {
            return null;
        }


        //Creates a Card Inventory Object and adds it to the database
        EnemyCardInventoryKey x = new EnemyCardInventoryKey(enemyId, cardId);
        EnemyCardInventory ci = new EnemyCardInventory(enemy, card, true);
        ci.setId(x);
        enemy.addCard(ci);
        enemy.setDeckSize(enemy.getDeckSize()+1);
        card.addEnemy(ci);
        enemyCardInventoryRepository.save(ci);


        return ci;
     }

     //Moves a card from inventory to deck
     @Operation(
             summary ="Moves a card from a enemy's inventory to deck",
             description = "Moves a card from a enemy's inventory to their deck to be used during combat phase"
     )
     @PutMapping(path = "/EnemyCardInventory/Deck/{enemyId}/{cardId}")
     String addCardToDeck(@PathVariable int cardId, @PathVariable int enemyId) {

         Card card = cardRepository.findById(cardId);
         if(card == null) {
             return null;
         }
         Enemy  enemy = enemyRepository.findById(enemyId);
         if(enemy == null) {
             return null;
         }

         //If deck is full cancel request
         if(enemy.getDeckSize() >= MAXDECKSIZE) {
             return "Deck is full. Remove a card to add another one";
         }

         //finds the card in the inventory
        EnemyCardInventory ci = enemyCardInventoryRepository.findByEnemyAndCard(enemy, card);
         if(ci.isInDeck()) {
             return "Card is already in deck";
         }

         //moves card from inventory to deck by changing the boolaen value
         ci.setInDeck(true);
         enemyCardInventoryRepository.save(ci);

         //increase deck size by 1
         enemy.setDeckSize(enemy.getDeckSize() + 1);
         enemyRepository.save(enemy);

         return success;
     }


     //Gets all the Cards in a Enemy's deck
     @Operation(
             summary ="Gets all the Cards in an enemy's deck",
             description = "Gets all the Cards in an enemy's deck"
     )
     @GetMapping(path = "/EnemyDeck/{enemyId}")
     List<Card> getDeck(@PathVariable int enemyId) {
         Enemy  enemy = enemyRepository.findById(enemyId);
         if(enemy == null) {
             return null;
         }

         List<EnemyCardInventory> ci = enemy.getEnemyCardInventory();
         List<Card> c = new ArrayList<>();

         for (EnemyCardInventory cardInventory : ci) {
             if (cardInventory.inDeck) {
                 c.add(cardInventory.card);
             }
         }
         return c;
     }

    @Operation(
            summary ="Removes a Card from an enemy's deck",
            description = "Removes a Card from an enemy's deck"
    )
     @PutMapping(path = "/EnemyDeck/remove/{enemyId}/{cardId}")
     String deckToInventory(@PathVariable int enemyId, @PathVariable int cardId) {

         Card card = cardRepository.findById(cardId);
         if(card == null) {
             return null;
         }
         Enemy  enemy = enemyRepository.findById(enemyId);
         if(enemy == null) {
             return null;
         }
         //finds the card in the deck
         EnemyCardInventory ci = enemyCardInventoryRepository.findByEnemyAndCard(enemy, card);
         if(ci == null) {
             return "Card not found";
         }
         if(ci.isInDeck() == false) {
             return "Card is already in inventory";
         }

         //moves card from deck to inventory by setting the boolean value to false
         ci.setInDeck(false);
         enemyCardInventoryRepository.save(ci);

         //decreases deck size by 1
         enemy.setDeckSize(enemy.getDeckSize() - 1);
         enemyRepository.save(enemy);

         return success;

     }

    @Operation(
            summary ="Swaps the place of a card in the deck with a card from the inventory",
            description = "Swaps the place of a card in the deck with a card from the inventory"
    )
     @PutMapping(path = "/EnemyDeck/swap/{enemyId}/{deckCardId}/{inventoryCardId}")
     String deckAndInventoryCardSwap(@PathVariable int enemyId, @PathVariable int deckCardId, @PathVariable int inventoryCardId) {

        Enemy enemy = enemyRepository.findById(enemyId);
        if(enemy == null) {
            return "Enemy not found";
        }

        Card deckCard = cardRepository.findById(deckCardId);
        if(deckCard == null) {
            return "Card not found";
        }

        Card inventoryCard = cardRepository.findById(inventoryCardId);
        if(inventoryCard == null) {
            return "Card not found";
        }

        EnemyCardInventory c1 = enemyCardInventoryRepository.findByEnemyAndCard(enemy, deckCard);
        if(c1 == null) {
            return "Enemy does not own this card";
        }
        if(c1.isInDeck() == false) {
            return "deck card is in inventory";
        }

        EnemyCardInventory c2 = enemyCardInventoryRepository.findByEnemyAndCard(enemy, inventoryCard);
         if(c2 == null) {
             return "Enmey does not own this card";
         }
         if(c2.isInDeck() == true) {
             return "inventory card is in deck";
         }

         //updates the cards and saves it to the database
         c1.setInDeck(false);
         enemyCardInventoryRepository.save(c1);
         c2.setInDeck(true);
         enemyCardInventoryRepository.save(c2);

         return success;
     }

    @Operation(
            summary ="Deletes a card from an enemy's inventory",
            description = "Deletes a card from a enemy's inventory"
    )
    @DeleteMapping(path = "/EnemyCardInventory/{enemyId}/{cardId}")
    String removeCardFromEnemy(@PathVariable int enemyId, @PathVariable int cardId ) {
        Enemy e = enemyRepository.findById(enemyId);
        if(e == null) {
            return failure;
        }
        Card c = cardRepository.findById(cardId);
        if(c == null) {
            return failure;
        }

        EnemyCardInventory ci = enemyCardInventoryRepository.findByEnemyAndCard(e, c);
        e.getEnemyCardInventory().remove(ci);
        c.getEnemyCardInventory().remove(ci);
        enemyCardInventoryRepository.deleteByEnemyAndCard(e, c);
        e.setDeckSize(e.getDeckSize()-1);

        return success;
    }





}
