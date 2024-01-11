package com.example.h2_database_and_classes.Card;

import com.example.h2_database_and_classes.Card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CardRepository extends JpaRepository<Card,Long> {
    Card findById(int id);

    @Transactional
    void deleteById(int id);
}
