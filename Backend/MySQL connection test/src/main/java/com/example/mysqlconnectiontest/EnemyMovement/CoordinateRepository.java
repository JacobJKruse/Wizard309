package com.example.mysqlconnectiontest.EnemyMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface CoordinateRepository extends JpaRepository<Coordinate,Long>{
    Coordinate findById(int Id);

    @Transactional
    void deleteById(int Id);

}
