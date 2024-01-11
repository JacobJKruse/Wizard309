package com.example.mysqlconnectiontest.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface CardRepository extends JpaRepository<Card,Long> {
    Card findById(int Id);

    @Transactional
    void deleteById(int Id);
}
