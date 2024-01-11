package com.example.mysqlconnectiontest;

import com.example.mysqlconnectiontest.Users.User;
import com.example.mysqlconnectiontest.Users.UserLevel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
public class UserSystemTesting {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void getUserTest() {
        Response response = RestAssured.given().when().get("/users/1");

        int statusCode = response.getStatusCode();

        assertEquals(200, statusCode);

        response.then().body("id", equalTo(1));
        response.then().body("email", equalTo("abhi@gmail.com"));
        response.then().body("userName", equalTo("abhi"));
        response.then().body("password", equalTo("a"));
        response.then().body("age", equalTo(100));
        response.then().body("userLevel", equalTo("Admin"));

    }

    @Test
    public void getAllUsers() throws JSONException {
        Response response = RestAssured.given().when().get("/users");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        response.then().body("size()", equalTo(19));

//        JSONArray jsonArray = new JSONArray(new JSONObject(response.toString()));
//        System.out.println(jsonArray);
        JSONArray returnArr = new JSONArray(response.getBody().asString());
        JSONObject returnObj = returnArr.getJSONObject(1);

        assertEquals(3, returnObj.get("id"), "id is incorrect");
        assertEquals(12, returnObj.get("age"), "age is incorrect");
        assertEquals("philip@gmail.com", returnObj.get("email"), "email is incorrect");
        assertEquals("philip2", returnObj.get("userName"), "user name is incorrect");
        assertEquals("Mod", returnObj.get("userLevel"), "user level is incorrect");
    }


    @Test
    public void createUpdateDeleteUserTest() {

        User u = new User("test@gmail.com", "testing123", "pass", 100, UserLevel.Player);

        Gson g = new Gson();

        //creates new user
        Response response = RestAssured.given().header("Content-Type", "application/json").body(g.toJson(u)).when().post("/users");
        assertEquals(200, response.getStatusCode());
        assertEquals("{\"message\":\"success\"}", response.asString());

        //gets the new user's Id
        Response response2 = RestAssured.given().when().get("/users/" + u.getUserName() + "/" + u.getPassword());
        assertEquals(200 , response2.getStatusCode());
        assertNotEquals("0", response2.asString());

        //gets the user from the database
        Response response3 = RestAssured.given().when().get("/users/" + response2.asString());
        assertEquals(200, response3.getStatusCode());
        User u2 = g.fromJson(response3.asString(), User.class);
        assertNotNull(u2);

        //changes the users values
        String newUserName = "putTest";
        u2.setUserName(newUserName);

        //change username
        Response response4 = RestAssured.given().header("Content-Type", "application/json").body(g.toJson(u2)).when().put("/users/" + u2.getId());
        assertEquals(200, response4.getStatusCode());

        //get user from the database again after the update
        Response response5 = RestAssured.given().when().get("/users/" + u2.getId());
        assertEquals(200, response5.getStatusCode());
        User u3 = g.fromJson(response5.asString(), User.class);
        assertEquals(u3.getUserName(), u2.getUserName());

        Response response6 = RestAssured.given().when().delete("/users/" + u3.getId());
        assertEquals(200, response6.getStatusCode());

    }

}
