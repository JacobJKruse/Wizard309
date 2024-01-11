package com.example.h2_database_and_classes.Card;

import com.example.h2_database_and_classes.Character.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class CardController {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    CharacterRepository characterRepository;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @PostMapping("/Card")
    String createCard(Card card) {
        if (card == null)
            return failure;
        cardRepository.save(card);
        return success;
    }
}
