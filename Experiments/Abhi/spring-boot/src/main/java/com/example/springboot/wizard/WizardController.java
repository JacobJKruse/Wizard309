package com.example.springboot.wizard;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class WizardController {

    ArrayList<Wizard> wList = new ArrayList<Wizard>();

    @GetMapping("/name={name}school={school}id={playerID}")
    public String createPlayer(@PathVariable String name, @PathVariable String school, @PathVariable long playerID) {
        Wizard w1 = new Wizard(((long) playerID), name, 1, new Integer(10), new Integer(5), school);
        wList.add(w1);
        return "added " + w1.toString();

    }


    @GetMapping("/list")
    public String list() {
        if (wList.isEmpty()) {
            return "List is Empty";
        }

        StringBuilder wL = new StringBuilder();
        for (Wizard wizard : wList) {
            wL.append(wizard.toString()).append("<br>");
        }

//        String l = wList.stream().map(Wizard::toString).forEach(String::concat);

        return wL.toString();
    }

    @DeleteMapping("/delete/{id}")
    String deleteWizard(@PathVariable Long id) {
        wList.removeIf(w -> (w.getPlayerID().equals(id)));

        return "Wizard " + id + " has been deleted";
    }


    @GetMapping("/wizard/{id}")
    String findWizard(@PathVariable Long id) {
        for (Wizard wizard : wList) {
            if (wizard.getPlayerID().equals(id)) {
                return wizard.toString();
            }
        }

        return "Wizard with ID:" + id + " was not found";
    }

    @PostMapping("/wizard/create")
    public @ResponseBody String createWizard(@RequestBody Wizard w) {
        wList.add(w);

        return w.getName() + " has been added";
    }

    @PutMapping("/wizard/levelup/{id}")
    public String updateWizard(@PathVariable long id) {
        for (Wizard wizard : wList) {
            if (wizard.getPlayerID().equals(id)) {
                wizard.setLevel(wizard.getLevel() + 1);
                return wizard.toString();
            }
        }
        return "Wizard not found";

    }
}