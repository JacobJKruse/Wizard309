package com.example.mysqlconnectiontest.Wizard;

import java.util.List;

import com.example.mysqlconnectiontest.Users.User;
import com.example.mysqlconnectiontest.Users.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;

@RestController
public class WizardController {

    @Autowired
    WizardRepository wizardRepository;

    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @GetMapping(path = "/wizards")
    List<Wizard> getAllWizards() {
        return wizardRepository.findAll();
    }

//    @GetMapping(path = "/wizards/user/{id}")
//    List<Wizard> getUserWizard(@PathVariable int id){
//
//    }

    @PostMapping(path = "/wizards")
    String createWizard(@RequestBody Wizard wizard) {
        if(wizard == null) {
            return failure;
        }
        wizardRepository.save(wizard);
        return  success;

    }

    @PutMapping("/wizards")
    User createWizard(@RequestParam("id") int id, @RequestParam("avatar")MultipartFile avatar, @RequestParam("wizard") String wizardString) throws Exception {

        User user = userRepository.findById(id);
        if(user == null) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Wizard wizard = objectMapper.readValue(wizardString, Wizard.class);

        wizard.setExtension(avatar.getOriginalFilename());

        user.addWizard(wizard);
//        wizardRepository.save(wizard);

//        if(avatar != null) {
            byte[] file = avatar.getBytes();
            SerialBlob blob = new SerialBlob(file);
            Blob image = blob;
            wizard.setAvatar(image);
            wizardRepository.save(wizard);
//        }



//        user.addWizard(request);
//        wizardRepository.save(request);
//
        return userRepository.findById(id);

    }

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
        wizardRepository.save(wizard);
        return wizardRepository.findById(id);
    }

    @DeleteMapping(path = "wizards/{id}")
    String deleteWizard(@PathVariable int id) {
        Wizard wizard = wizardRepository.findById(id);
        if(wizard == null) {
            return failure;
        }
        wizard.getUser().deleteWizard(wizard);
        wizardRepository.deleteById(Long.valueOf(id));

        return success;
    }

}
