package com.example.mysqlconnectiontest.Card;
import java.util.List;
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
public class CardController {
    @Autowired
    CardRepository cardRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/cards")
    List<Card> getAllCards(){  return cardRepository.findAll();
    }

    @GetMapping(path = "/cards/{id}")
    Card getCardById(@PathVariable int id){
        return cardRepository.findById(id);
    }

    @PostMapping(path = "/cards")
    String createCard(@RequestBody Card card){
        if (card == null)
            return failure;
        cardRepository.save(card);
        return success;
    }

    @PutMapping(path = "/cards/{id}")
    Card updateCard(@PathVariable int id, @RequestBody Card request){
        Card card = cardRepository.findById(id);
        if(card == null)
            return null;
        cardRepository.save(request);
        return cardRepository.findById(id);
    }

    @DeleteMapping(path = "/cards/{id}")
    String deleteCard(@PathVariable int id){
        cardRepository.deleteById(id);
        return success;
    }
}
