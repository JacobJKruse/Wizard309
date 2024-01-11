package com.example.mysqlconnectiontest.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Map", description = "Manages the map")
@RestController
public class MapController {

    @Autowired
    MapRepository mapRepository;

//    @GetMapping(path = "/map/{id}")
//    String getMapGridById(@PathVariable int id) {
//        return mapRepository.findById(id).getMapGrid();
//    }


    @Operation(
            summary = "Gets the map by the name",
            description = "Gets the map by name"
    )
    @GetMapping(path = "/map/{name}")
    String getMapGridByName(@PathVariable String name) {
        return mapRepository.findMapByName(name).getMapGrid();
    }


    @Operation(
            summary = "Adds a new map into the database",
            description = "Adds a new map into the database"
    )
    @PostMapping(path = "/map")
    void createMap(@RequestBody Map map) {
        map.setXBound((map.to2dArr(map.getMapGrid()))[0].length);
        map.setYBound((map.to2dArr(map.getMapGrid())).length);


        mapRepository.save(map);
    }

    @Operation(
            summary = "changes how the map looks like",
            description = "changes how the map looks like"
    )
    @PutMapping(path = "/map/{name}")
    String editMap(@PathVariable String name, @RequestBody Map map) {
        Map m = mapRepository.findMapByName(name);
        m.setMapGrid(map.getMapGrid());
        m.setXBound((map.to2dArr(map.getMapGrid()))[0].length);
        m.setYBound((map.to2dArr(map.getMapGrid())).length);
        mapRepository.save(m);
        return "Success";
    }

    @Operation(
            summary = "Deletes a map by name",
            description = "Deletes a map by name"
    )
    @DeleteMapping(path = "/map/{name}")
    String deleteMap(@PathVariable String name) {
        mapRepository.deleteByName(name);
        return "Success";
    }
}
