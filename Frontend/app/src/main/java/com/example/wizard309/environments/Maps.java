package com.example.wizard309.environments;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wizard309.main.Login;
import com.example.wizard309.main.MainActivity;
import com.example.wizard309.main.MainMenuActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * public enum for maps
 */
public enum Maps {

    GRASSYMAP(new String[]{"grassOverworld"});

    private String[] namedLayers;
    RequestQueue queue;
    private  Map< String, int[][] > mapLayers = new Hashtable < > ();

    private String url = "http://coms-309-032.class.las.iastate.edu:8080/map/";


    /**
     * checks to make sure request are completed before sending
     */
    public interface OnAllRequestsCompleted {
        void onAllRequestsCompleted();
    }

    /**
     * constructor for maps sends a request for the specified map
     * @param layers
     */
    Maps(String[] layers){
        this.namedLayers = layers;
        queue = Volley.newRequestQueue(MainActivity.getGameContext());
        getLayers(() -> {

        });
    }

    /**
     * makes request to get the map layers
     * @param callback
     */
    public void getLayers(OnAllRequestsCompleted callback){
        AtomicInteger counter = new AtomicInteger(namedLayers.length);
        for (String item:namedLayers) {
            String newUrl = url+item.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, newUrl,
                    response -> {
                        int[][] layers = to2dArr(response.toString());
                        mapLayers.put(item.toString(),layers);
                        if(counter.decrementAndGet() == 0){
                            callback.onAllRequestsCompleted();
                            System.out.println("GOT MAP");
                        }
                    },
                    error -> {
                        System.out.println(error.getMessage());
                    });

            queue.add(stringRequest);
        }
    }


    private int[][] to2dArr(String mapGrid) {


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

    /**
     * returns the maplayers hashmap
     * @return
     */
    public Map<String, int[][]> getMapLayers() {
        return mapLayers;
    }
}
