package com.example.mysqlconnectiontest.EnemyMovement;
import com.example.mysqlconnectiontest.Enemy.Enemy;
import com.example.mysqlconnectiontest.Enemy.EnemyFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Coordinate", description = "Coordinate Lists for enemy paths")
@RestController
public class CoordinateController {
    @Autowired
    CoordinateRepository coordinateRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Operation(
            summary = "Gets all the coordinate lists in the database",
            description = "Gets all the coordinate lists in the database"
    )
    @GetMapping(path = "/coords")
    List<Coordinate> getAllCoords() {
        return coordinateRepository.findAll();
    }

    @Operation(
            summary = "Gets an coordinate list by id",
            description = "Gets an coordinate list by id"
    )
    @GetMapping(path = "/coords/{id}")
    Coordinate getCoordinate(@PathVariable int id){
        return coordinateRepository.findById(id);
    }

    @Operation(
            summary = "Adds an coordinate list to the database",
            description = "Adds an coordinate list to the database"
    )
    @PostMapping(path = "/coords")
    String createCoordinate(@RequestBody Coordinate coordinate) {
        if(coordinate == null) {
            return failure;
        }
        coordinateRepository.save(coordinate);
        return  (String.valueOf(coordinate.getId()));

    }


    @Operation(
            summary = "Updates an coordinate list",
            description = "Updates an coordinate list"
    )
    @PutMapping(path = "/coords/{id}")
    Coordinate updateCoordinate(@PathVariable int id, @RequestBody Coordinate request) {
        Coordinate coordinate = coordinateRepository.findById(id);
        if(coordinate == null) {
            return null;
        }
        coordinate.setCoords(request.getCoords());
        return coordinateRepository.findById(id);
    }

    @Operation(
            summary = "Deletes a coordinate list",
            description = "Deletes a coordinate list"
    )
    @DeleteMapping(path = "coords/{id}")
    String deleteCoordinate(@PathVariable int id) {
        Coordinate coordinate = coordinateRepository.findById(id);
        if(coordinate == null) {
            return failure;
        }

        coordinateRepository.deleteById(Long.valueOf(id));

        return success;
    }
}
