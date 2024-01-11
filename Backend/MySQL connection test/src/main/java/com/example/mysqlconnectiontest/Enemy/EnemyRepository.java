package com.example.mysqlconnectiontest.Enemy;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnemyRepository extends JpaRepository<Enemy, Long> {
    Enemy findById(int id);
}
