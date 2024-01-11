package com.example.mysqlconnectiontest.Users;


import com.example.mysqlconnectiontest.Wizard.Wizard;
import com.example.mysqlconnectiontest.Wizard.WizardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;


    @Autowired
    WizardRepository wizardRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }

    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }

    @PutMapping("/users/{userId}/create")
    String assignWizardToUser(@PathVariable int userId, @RequestBody Wizard s) {
        User user = userRepository.findById(userId);
        user.addWizard(s);
        userRepository.save(user);
        return success;
    }

    @DeleteMapping("/users/{userId}")
    String deleteUser(@PathVariable int userId) {
        User user = userRepository.findById(userId);
        if(user == null) {
            return failure;
        }

        user.getWizards().forEach(wizard -> wizardRepository.deleteById(new Long(wizard.getId())));
        userRepository.deleteById(user.getId());
        return success;
    }





}
