package com.example.h2_database_and_classes.Character;

import com.example.h2_database_and_classes.Character.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character,Long> {
    //Character findById(int id);

    //void deleteById(int id);

    //Character findByCard_Id(int id);
}
