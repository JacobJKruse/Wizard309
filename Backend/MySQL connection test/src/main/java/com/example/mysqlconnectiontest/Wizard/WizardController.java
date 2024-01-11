package com.example.mysqlconnectiontest.Wizard;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.example.mysqlconnectiontest.Card.Card;
import com.example.mysqlconnectiontest.Card.CardRepository;
import com.example.mysqlconnectiontest.CardInventory.CardInventory;
import com.example.mysqlconnectiontest.CardInventory.CardInventoryKey;
import com.example.mysqlconnectiontest.CardInventory.CardInventoryRepository;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventory;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventoryKey;
import com.example.mysqlconnectiontest.Users.User;
import com.example.mysqlconnectiontest.Users.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;

@RestController
@Tag(name = "Wizard", description = "Manages Wizards")
public class WizardController {

    @Autowired
    WizardRepository wizardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CardInventoryRepository cardInventoryRepository;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @Operation(
            summary = "Returns a list of all the wizards in the database",
            description = "Returns a list of all the wizards in the database"
    )
    @GetMapping(path = "/wizards")
    List<Wizard> getAllWizards() {
        return wizardRepository.findAll();
    }

    @Operation(
            summary = "Returns a wizard by its id",
            description = "Returns a wizard by its id"
    )
    @GetMapping(path = "/wizards/{id}")
    Wizard getUserWizard(@PathVariable int id){
        return wizardRepository.findById(id);
    }

    @Operation(
            summary = "Returns a list of wizards that a user has",
            description = "Returns a list of wizards that a user has"
    )
    @GetMapping(path = "/wizards/user/{userId}")
    List<Wizard>  getWizardsByUserId(@PathVariable int userId)
    {

        return userRepository.findById(userId).getWizards();
    }


    @Operation(
            summary = "Creates a wizard",
            description = "Creates a wizard"
    )
    @PostMapping(path = "/wizards")
    String createWizard(@RequestBody Wizard wizard) {
        if(wizard == null) {
            return failure;
        }
        wizardRepository.save(wizard);
        return  (String.valueOf(wizard.getId()));

    }

    @Operation(
            summary = "Creates a wizard and adds an avatar",
            description = "Creates a wizard and adds an avatar"
    )
    @PostMapping(path = "/wizards/{id}/{file}")
    User createWizardNoImage(@PathVariable("id") int id, @RequestBody Wizard wizard, @PathVariable("file") String file) {
        wizard.setExtension(file);
        User user = userRepository.findById(id);
        wizard.setUser(user);
        user.addWizard(wizard);
        wizardRepository.save(wizard);
        List<Card> cards = new ArrayList<>();
        cards.add(cardRepository.findById(8));
        cards.add(cardRepository.findById(11));
        cards.add(cardRepository.findById(16));
        cards.add(cardRepository.findById(20));
        cards.add(cardRepository.findById(22));
        cards.add(cardRepository.findById(26));
        List<CardInventory> deck = new ArrayList<>();
        for(Card c: cards) {
            CardInventory ci = new CardInventory(wizard, c, true);
            CardInventoryKey x = new CardInventoryKey(wizard.getId(), c.getId());
            ci.setId(x);
            deck.add(ci);
            cardInventoryRepository.save(ci);
        }
        wizard.setCardInventory(deck);
        wizard.setDeckSize(cards.size());
        wizardRepository.save(wizard);


        return user;
    }

    @Operation(
            summary = "Updates a wizard",
            description = "Updates a wizard"
    )
    @PutMapping(path = "/wizards/{id}")
    Wizard updateWizard(@PathVariable int id, @RequestBody Wizard request) {
        Wizard wizard = wizardRepository.findById(id);
        if(wizard == null) {
            return null;
        }
        wizard.setDisplayName(request.getDisplayName());
        wizard.setMaxHP(request.getMaxHP());
        wizard.setCurrHP(request.getCurrHP());
        wizard.setMaxMana(request.getMaxMana());
        wizard.setCurrMana(request.getCurrMana());
        wizard.setLvl(request.getLvl());
        wizard.setNextLvlXp(request.getNextLvlXp());
        wizard.setCurrXp(request.getCurrXp());
        wizard.setDeckSize(request.getDeckSize());
        wizard.setWallet(request.getWallet());
        wizardRepository.save(wizard);
        return wizardRepository.findById(id);
    }

    @Operation(
            summary = "deletes a wizard",
            description = "deletes a wizard"
    )
    @DeleteMapping(path = "wizards/{id}")
    String deleteWizard(@PathVariable int id) {
        Wizard wizard = wizardRepository.findById(id);
        if(wizard == null) {
            return failure;
        }
        wizard.getUser().deleteWizard(wizard);
        List<CardInventory> cardInventory = cardInventoryRepository.getAllByWizard(wizard);
        for (CardInventory ci : cardInventory
             ) {
            Card c = ci.getCard();
            wizard.getCardInventory().remove(ci);
            c.getCardInventory().remove(ci);
            cardInventoryRepository.deleteByWizardAndCard(wizard, c);
        }
        wizardRepository.deleteById(Long.valueOf(id));

        return success;
    }

    @Operation(
            summary = "Gets a wizard wallet",
            description = "Gets a wizard wallet"
    )
    @GetMapping(path = "/wizards/wallet/{id}")
    int updateWallet(@PathVariable int id){
        Wizard wizard = wizardRepository.findById(id);
        if(wizard == null) {
            return -1;
        }
        return wizard.getWallet();
    }

    @Operation(
            summary = "updates the wizard's gold amount",
            description = "updates the wizard's gold amount"
    )
    @PutMapping(path = "/wizards/wallet/{id}/{amount}")
    int updateWallet(@PathVariable int id, @PathVariable int amount){
        Wizard wizard = wizardRepository.findById(id);
        if(wizard == null) {
            return -1;
        }
        wizard.setWallet(wizard.getWallet()+amount);
        wizardRepository.save(wizard);
        return wizard.getWallet();
    }

}
