package com.example.h2_database_and_classes.Character;

import com.example.h2_database_and_classes.Card.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CharacterController {
    @Autowired
    CharacterRepository characterRepository;
    @Autowired
    CardRepository cardRepository;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";
}
