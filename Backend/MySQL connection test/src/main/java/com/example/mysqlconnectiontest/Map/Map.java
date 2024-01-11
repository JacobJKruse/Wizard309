package com.example.mysqlconnectiontest.Map;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Setter
@Getter
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int xBound;
    private int yBound;

    @Column(columnDefinition = "varchar(20000)")
    private String mapGrid;

    public Map(String name, String mapGrid) {
        this.name = name;
        this.mapGrid = mapGrid;
        this.xBound = to2dArr(mapGrid)[0].length;
        this.yBound = to2dArr(mapGrid).length;
    }

    public Map() {

    }

    public int[][] to2dArr(String mapGrid) {


        String str = mapGrid; // your string here

// Remove the outer braces and split the string into an array of string rows
        String[] rows = str.substring(1, str.length() - 1).split("\\},");

// Initialize the array
        int[][] array = new int[rows.length][];

// Loop through the rows
        for (int i = 0; i < rows.length; i++) {
            // Remove any remaining braces from the row and split it into an array of string numbers
            String[] numbers = rows[i].replaceAll("\\{|\\}", "").split(",");

            // Initialize this row in the array
            array[i] = new int[numbers.length];

            // Loop through the numbers
            for (int j = 0; j < numbers.length; j++) {
                // Parse each number and add it to the array
                array[i][j] = Integer.parseInt(numbers[j].trim());
            }
        }
        return array;
    }
}
