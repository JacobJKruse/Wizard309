package com.example.mysqlconnectiontest.Characters;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.mysqlconnectiontest.Card.Card;
import com.example.mysqlconnectiontest.Card.CardRepository;
@RestController
public class CharacterController {
    @Autowired
    CharacterRepository characterRepository;
    @Autowired
    CardRepository cardRepository;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/characters")
    List<Characters> getAllCharacter(){
        return characterRepository.findAll();
    }

    @GetMapping(path = "/characters/{id}")
    Characters getCharacterById(@PathVariable int id){
        return characterRepository.findById(id);
    }

    @PostMapping(path = "/characters")
    String createUser(Characters character){
        if (character == null)
            return failure;
        characterRepository.save(character);
        return success;
    }

    @PutMapping("/characters/{id}")
    Characters updateUser(@PathVariable int id, @RequestBody Characters request){
        Characters character = characterRepository.findById(id);
        if(character == null)
            return null;
        characterRepository.save(request);
        return characterRepository.findById(id);
    }

    @PutMapping("/characters/{characterId}/cards/{cardId}")
    String assignCardToCharacter(@PathVariable int characterId,@PathVariable int cardId){
        Characters character = characterRepository.findById(characterId);
        Card card = cardRepository.findById(cardId);
        if(character == null || card == null)
            return failure;
        card.setCharacter(character);
        character.setCard(card);
        characterRepository.save(character);
        return success;
    }

    @DeleteMapping(path = "/characters/{id}")
    String deleteUser(@PathVariable int id){
        characterRepository.deleteById(id);
        return success;
    }
}
