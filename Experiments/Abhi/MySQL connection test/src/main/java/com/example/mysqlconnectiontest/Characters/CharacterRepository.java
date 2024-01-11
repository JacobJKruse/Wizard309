package com.example.mysqlconnectiontest.Characters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CharacterRepository extends JpaRepository<Characters,Long>{
    Characters findById(int Id);

    @Transactional
    void deleteById(int Id);
}
