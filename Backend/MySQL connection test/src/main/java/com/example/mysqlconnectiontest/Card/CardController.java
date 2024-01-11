package com.example.mysqlconnectiontest.Card;
import java.util.List;

import com.example.mysqlconnectiontest.CardInventory.CardInventory;
import com.example.mysqlconnectiontest.CardInventory.CardInventoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name ="Card Controller", description = "Manages the database of cards")
public class CardController {
    @Autowired
    CardRepository cardRepository;

    @Autowired
    CardInventoryRepository cardInventoryRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(
            summary ="Retrieves all cards",
            description = "Retrieves all cards"
    )
    @GetMapping(path = "/cards")
    List<Card> getAllCards(){  return cardRepository.findAll();
    }

    @Operation(
            summary ="Retrieves all cards by the card's id",
            description = "Retrieves all cards by the card's id"
    )
    @GetMapping(path = "/cards/{id}")
    Card getCardById(@PathVariable int id){
        return cardRepository.findById(id);
    }

    @Operation(
            summary ="Adds a card to the database",
            description = "Adds a card to the database, incrementally sets id in database"
    )
    @PostMapping(path = "/cards")
    String createCard(@RequestBody Card card){
        if (card == null)
            return failure;
        cardRepository.save(card);
        return success;
    }

    @Operation(
            summary ="updates a card by id value",
            description = "updates a card by id value"
    )
    @PutMapping(path = "/cards/{id}")
    Card updateCard(@PathVariable int id, @RequestBody Card request){
        Card card = cardRepository.findById(id);
        if(card == null)
            return null;
        request.setId(card.getId());
        cardRepository.save(request);
        return cardRepository.findById(id);
    }

    @Operation(
            summary ="deletes a card by id value",
            description = "deletes a card by id value"
    )
    @DeleteMapping(path = "/cards/{id}")
    String deleteCard(@PathVariable int id){

        Card c = cardRepository.findById(id);
        //not sure if this will work correctly
        cardInventoryRepository.deleteByCard(c);

        cardRepository.deleteById(id);
        return success;
    }

    @Operation(
            summary ="deletes all cards from the database",
            description = "deletes all cards from the database"
    )
    @DeleteMapping(path = "/cards/delete")
    String deleteAllCards() {
        cardInventoryRepository.deleteAll();
        cardRepository.deleteAll();
        return success;
    }
}
