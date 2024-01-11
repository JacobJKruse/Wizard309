package com.example.mysqlconnectiontest.Users;


import com.example.mysqlconnectiontest.Wizard.Wizard;
import com.example.mysqlconnectiontest.Wizard.WizardRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "User", description = "Manages Users")
public class UserController {

    @Autowired
    UserRepository userRepository;


    @Autowired
    WizardRepository wizardRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @Operation(
            summary = "Retrieves all the users",
            description = "Retrieves all the users"
    )
    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Operation(
            summary = "finds the user by id",
            description = "finds the user by id"
    )
    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    @Operation(
            summary = "returns the user's id by name",
            description = "returns the user's id by name"
    )
    @GetMapping(path = "/users/{uname}/{pass}")
    int getUserByAccount(@PathVariable String uname, @PathVariable String pass)
    {
        return userRepository.findByuserNameAndPassword(uname, pass).getId();

    }


    @Operation(
            summary = "Creates a new User",
            description = "Creates a new user"
    )
    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }

    @Operation(
            summary = "Changes the details of a user",
            description = "Changes the details of a user"
    )
    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }

    @Operation(
            summary = "Adds a wizard to a user",
            description = "Adds a wizard to a user"
    )
    @PutMapping("/users/{userId}/create")
    String assignWizardToUser(@PathVariable int userId, @RequestBody Wizard s) {
        User user = userRepository.findById(userId);
        user.addWizard(s);
        userRepository.save(user);
        return success;
    }

    @Operation(
            summary = "Deletes the User",
            description = "Deletes the User"
    )
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
