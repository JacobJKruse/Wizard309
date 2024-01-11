package com.example.mysqlconnectiontest.EnemyMovement;

import com.example.mysqlconnectiontest.Card.AttackType;
import com.example.mysqlconnectiontest.Element.Element;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Coordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String coords;

    public Coordinate(){

    }
    public Coordinate(String coords){
        this.coords = coords;
    }

}


