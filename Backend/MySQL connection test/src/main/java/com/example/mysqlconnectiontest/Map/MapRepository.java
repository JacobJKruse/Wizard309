package com.example.mysqlconnectiontest.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface MapRepository extends JpaRepository<Map, Long> {
    Map findById(int id);
    Map findMapByName(String name);

    @Transactional
    void deleteByName(String name);
}
