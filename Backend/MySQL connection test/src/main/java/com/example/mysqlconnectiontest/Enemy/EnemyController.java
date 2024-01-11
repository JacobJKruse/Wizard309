package com.example.mysqlconnectiontest.Enemy;

import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventoryController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Enemy", description = "Controls the enemies")
@RestController
public class EnemyController {

    @Autowired
    EnemyRepository enemyRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(
            summary = "Gets all the enemies in the database",
            description = "Gets all the enemies in the database"
    )
    @GetMapping(path = "/enemies")
    List<Enemy> getAllEnemies() {
        return enemyRepository.findAll();
    }

    @Operation(
            summary = "Gets an enemy by id",
            description = "Gets an enemy by id"
    )
    @GetMapping(path = "/enemies/{id}")
    Enemy getEnemy(@PathVariable int id){
        return enemyRepository.findById(id);
    }


    @Operation(
            summary = "Adds an enemy to the database",
            description = "Adds an enemy to the database"
    )
    @PostMapping(path = "/enemies")
    String createEnemy(@RequestBody Enemy enemy) {
        if(enemy == null) {
            return failure;
        }
        enemyRepository.save(enemy);
        return  (String.valueOf(enemy.getId()));

    }

    @PostMapping(path = "/enemies/{request}")
    String creatEnemy(@PathVariable String request) {
        EnemyFactory factory = new EnemyFactory();
        Enemy e = factory.createEnemy(request);
        int i = (int)(Math.random() * 100000);
        e.setDisplayName(e.getDisplayName() + i);
        enemyRepository.save(e);
        e.setDeck();
        enemyRepository.save(e);
        return success;
    }


    @Operation(
            summary = "Updates an enemy",
            description = "Updates an enemy"
    )
    @PutMapping(path = "/enemies/{id}")
    Enemy updateEnemy(@PathVariable int id, @RequestBody Enemy request) {
        Enemy enemy = enemyRepository.findById(id);
        if(enemy == null) {
            return null;
        }
        enemy.setDisplayName(request.getDisplayName());
        enemy.setMaxHP(request.getMaxHP());
        enemy.setCurrHP(request.getCurrHP());
        enemy.setMaxMana(request.getMaxMana());
        enemy.setCurrMana(request.getCurrMana());
        enemy.setLvl(request.getLvl());
        enemy.setXpDrop(request.getXpDrop());
        enemy.setGoldDrop(request.getGoldDrop());
        enemyRepository.save(enemy);
        return enemyRepository.findById(id);
    }

    @Operation(
            summary = "Deletes an enemy",
            description = "Deletes an enemy"
    )
    @DeleteMapping(path = "enemies/{id}")
    String deleteEnemy(@PathVariable int id) {
        Enemy enemy = enemyRepository.findById(id);
        if(enemy == null) {
            return failure;
        }

        enemyRepository.deleteById(Long.valueOf(id));

        return success;
    }
}
