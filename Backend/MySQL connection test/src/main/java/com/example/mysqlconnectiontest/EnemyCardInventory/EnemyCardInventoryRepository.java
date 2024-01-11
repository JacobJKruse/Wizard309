package com.example.mysqlconnectiontest.EnemyCardInventory;

import com.example.mysqlconnectiontest.Card.Card;
import com.example.mysqlconnectiontest.CardInventory.CardInventory;
import com.example.mysqlconnectiontest.CardInventory.CardInventoryKey;
import com.example.mysqlconnectiontest.Enemy.Enemy;
import com.example.mysqlconnectiontest.Wizard.Wizard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EnemyCardInventoryRepository extends JpaRepository<EnemyCardInventory, EnemyCardInventoryKey> {
    List<EnemyCardInventory> getAllByEnemy(Enemy enemy);

    List<EnemyCardInventory> getAllByEnemyId(int enemyId);

    EnemyCardInventory findByEnemyAndCard(Enemy enemy, Card card);

    @Transactional
    void deleteByEnemyAndCard(Enemy enemy, Card card);


}
