package com.example.mysqlconnectiontest;

import com.example.mysqlconnectiontest.Map.Map;
import com.example.mysqlconnectiontest.Users.User;
import com.example.mysqlconnectiontest.Users.UserLevel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@RunWith(SpringRunner.class)
public class MapSystemTesting {

    @LocalServerPort
    int port;


    String map = "{{113, 75, 41},{113, 113, 30}}";
    String map2 = "{{113, 75, 41},{113, 60, 30}}";
    Map m = new Map("SystemTest", map);
    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void mapCreateEditDeleteTest() {
        Gson g = new Gson();
        Response response = RestAssured.given().header("Content-Type", "application/json").body(g.toJson(m)).when().post("/map");
        assertEquals(200, response.getStatusCode());

        Response response2 = RestAssured.given().when().get("/map/SystemTest");
        response2.then().body(equalTo(map));

        m.setMapGrid(map2);
        Response response3 = RestAssured.given().header("Content-Type", "application/json").body(g.toJson(m)).when().put("/map/SystemTest");
        assertEquals(200, response3.getStatusCode());
        response3.then().body(equalTo("Success"));

        Response deleteResponse = RestAssured.given().delete("/map/SystemTest");
        assertEquals(200, deleteResponse.getStatusCode());

    }
}
