package com.example.mysqlconnectiontest;

import com.example.mysqlconnectiontest.Card.AttackType;
import com.example.mysqlconnectiontest.Card.Card;
import com.example.mysqlconnectiontest.Element.Element;
import com.example.mysqlconnectiontest.Enemy.EnemyFactory;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@RunWith(SpringRunner.class)
public class SystemTesting {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }


    @Test
    public void getAllCards() throws JSONException {
        Response response = RestAssured.given().when().get("/cards");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        response.then().body("size()",equalTo(48));

        JSONArray returnArray = new JSONArray(response.getBody().asString());
        JSONObject obj = returnArray.getJSONObject(0);
        assertEquals(8,obj.get("id"), "id is Correct");
        String returnString = response.getBody().asString();
        System.out.println(returnString);
    }
    @Test
    public void getCardsById() {
        Response response = RestAssured.given().when().get("/cards/8");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        response.then().body("extension", equalTo("firefox.jpg"));
        String returnString = response.getBody().asString();
        System.out.println(returnString);
    }
    @Test
    public  void  AddAndDeleteCard() throws JSONException {
        String requestBody  = "{\n" +
                "    \"spell_name\": \"test\",\n" +
                "    \"extension\": \"test\",\n" +
                "    \"attack_power\": 100,\n" +
                "    \"element\": \"Fire\",\n" +
                "    \"mana_cost\": 10,\n" +
                "    \"attack_type\": \"Single\"\n" +
                "}";
        Response response = RestAssured.given().
                header("Content-Type", "application/json").body(requestBody).when().post("/cards");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        Card c = new Card("firefox", Element.Fire, 130, 10, AttackType.Single, "firefox.jpg");
        Response response2 = RestAssured.given().when().get("/cards");

        JSONArray returnArray = new JSONArray(response2.getBody().asString());
        JSONObject obj = returnArray.getJSONObject(48);
        int id = (int) obj.get("id");

        Response response4 = RestAssured.given().
                header("Content-Type", "application/json").body(requestBody).when().put("/cards/"+id);
        int statusCode4 = response.getStatusCode();
        assertEquals(200, statusCode);

        Response response3 = RestAssured.given().when().delete("/cards/" +id);
        int statusCode3 = response3.getStatusCode();
        assertEquals(200, statusCode3);

    }

    @Test
    public void CardInventory() throws JSONException {
        Response response = RestAssured.given().when().get("/CardInventory");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        JSONArray returnArray = new JSONArray(response.getBody().asString());
        JSONObject obj = returnArray.getJSONObject(0);
        assertEquals(true,obj.get("inDeck"), "id is Correct");

        Response response1 = RestAssured.given().when().get("/CardInventory/1");
        int statusCode1 = response.getStatusCode();
        assertEquals(200, statusCode);
        JSONArray returnArray1 = new JSONArray(response1.getBody().asString());
        JSONObject obj1 = returnArray1.getJSONObject(0);
        assertEquals(8,obj1.get("id"), "id is Correct");

        Response response2 = RestAssured.given().when().post("/CardInventory/add/1/58");
        int statusCode2 = response2.getStatusCode();
        assertEquals(200, statusCode2);

        Response response3 = RestAssured.given().when().put("/CardInventory/Deck/1/58");
        int statusCode3 = response3.getStatusCode();
        assertEquals(200, statusCode3);

        Response response4 = RestAssured.given().when().get("/Deck/1");
        int statusCode4 = response4.getStatusCode();
        assertEquals(200, statusCode4);
        JSONArray returnArray4 = new JSONArray(response1.getBody().asString());
        JSONObject obj4 = returnArray1.getJSONObject(0);
        assertEquals(8,obj1.get("id"), "id is Correct");

        Response response5 = RestAssured.given().when().put("/Deck/remove/1/58");
        int statusCode5 = response5.getStatusCode();
        assertEquals(200, statusCode5);

        Response response6 = RestAssured.given().when().put("/Deck/remove/1/60");
        int statusCode6 = response6.getStatusCode();
        assertEquals(200, statusCode6);

        Response response7 = RestAssured.given().when().put("/Deck/remove/0/58");
        int statusCode7 = response7.getStatusCode();
        assertEquals(200, statusCode7);

        Response response8 = RestAssured.given().when().delete("/Deck/1/58");
        int statusCode8 = response8.getStatusCode();
        assertEquals(200, statusCode8);

        Response response9 = RestAssured.given().when().delete("/Deck/0/58");
        int statusCode9 = response9.getStatusCode();
        assertEquals(200, statusCode9);

        Response response10 = RestAssured.given().when().delete("/Deck/1/60");
        int statusCode10 = response10.getStatusCode();
        assertEquals(200, statusCode10);
    }

    @Test
    public void EnemyCardInventory() throws JSONException {
        Response response = RestAssured.given().when().get("/EnemyCardInventory");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        JSONArray returnArray = new JSONArray(response.getBody().asString());
        JSONObject obj = returnArray.getJSONObject(0);
        assertEquals(true,obj.get("inDeck"), "id is Correct");

        Response response1 = RestAssured.given().when().get("/EnemyCardInventory/10");
        int statusCode1 = response.getStatusCode();
        assertEquals(200, statusCode);
        JSONArray returnArray1 = new JSONArray(response1.getBody().asString());
        JSONObject obj1 = returnArray1.getJSONObject(0);
        assertEquals(8,obj1.get("id"), "id is Correct");

        Response response2 = RestAssured.given().when().post("/EnemyCardInventory/add/10/12");
        int statusCode2 = response2.getStatusCode();
        assertEquals(200, statusCode2);

        Response response3 = RestAssured.given().when().put("/EnemyCardInventory/Deck/10/12");
        int statusCode3 = response3.getStatusCode();
        assertEquals(200, statusCode3);

        Response response4 = RestAssured.given().when().get("/EnemyDeck/10");
        int statusCode4 = response4.getStatusCode();
        assertEquals(200, statusCode4);
        JSONArray returnArray4 = new JSONArray(response1.getBody().asString());
        JSONObject obj4 = returnArray1.getJSONObject(0);
        assertEquals(8,obj1.get("id"), "id is Correct");

        Response response5 = RestAssured.given().when().put("/EnemyDeck/remove/10/12");
        int statusCode5 = response5.getStatusCode();
        assertEquals(200, statusCode5);

        Response response6 = RestAssured.given().when().put("/EnemyDeck/remove/1/60");
        int statusCode6 = response6.getStatusCode();
        assertEquals(200, statusCode6);

        Response response7 = RestAssured.given().when().put("/EnemyDeck/remove/0/12");
        int statusCode7 = response7.getStatusCode();
        assertEquals(200, statusCode7);

        Response response8 = RestAssured.given().when().delete("/Deck/1/58");
        int statusCode8 = response8.getStatusCode();
        assertEquals(200, statusCode8);

        Response response9 = RestAssured.given().when().delete("/EnemyCardInventory/10/12");
        int statusCode9 = response9.getStatusCode();
        assertEquals(200, statusCode9);

        Response response10 = RestAssured.given().when().delete("/EnemyCardInventory/1/60");
        int statusCode10 = response10.getStatusCode();
        assertEquals(200, statusCode10);

        Response response11 = RestAssured.given().when().put("/EnemyDeck/swap/10/8/12");
        int statusCode11 = response11.getStatusCode();
        assertEquals(200, statusCode11);

        Response response12 = RestAssured.given().when().put("/EnemyDeck/swap/1/8/12");
        int statusCode12 = response12.getStatusCode();
        assertEquals(200, statusCode12);

        Response response13 = RestAssured.given().when().put("/EnemyDeck/swap/10/1/12");
        int statusCode13 = response11.getStatusCode();
        assertEquals(200, statusCode13);


    }

    @Test
    public void Enemy() throws JSONException {
        Response response = RestAssured.given().when().get("/enemies");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        response.then().body("size()",equalTo(25));

        Response response1 = RestAssured.given().when().get("/enemies/10");
        int statusCode1 = response1.getStatusCode();
        assertEquals(200, statusCode1);
        response1.then().body("extension", equalTo("test_guy"));



        Response response2 = RestAssured.given().
                header("Content-Type", "application/json").when().post("/enemies/Wisp");
        int statusCode2 = response2.getStatusCode();
        assertEquals(200, statusCode2);


        Response response4 = RestAssured.given().when().get("/enemies");
        JSONArray returnArray = new JSONArray(response4.getBody().asString());
        JSONObject obj = returnArray.getJSONObject(25);
        int id = (int) obj.get("id");

        Response response5 = RestAssured.given().when().delete("/EnemyCardInventory/"+id+"/8");
        int statusCode5 = response5.getStatusCode();
        assertEquals(200, statusCode5);

        Response response3 = RestAssured.given().when().delete("/enemies/" +id);
        int statusCode3 = response3.getStatusCode();
        assertEquals(200, statusCode3);

    }

    @Test
    public void EnemyMovement() throws JSONException {
        Response response = RestAssured.given().when().get("/coords");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        response.then().body("size()",equalTo(6));

        Response response1 = RestAssured.given().when().get("/coords/3");
        int statusCode1 = response1.getStatusCode();
        assertEquals(200, statusCode1);
        response1.then().body("coords", equalTo("0,3000 2000,3000"));

        String requestBody = "{\"coords\":\"0,3000 2000,3000\"}";
        Response response2 = RestAssured.given().header("Content-Type", "application/json").body(requestBody).when().put("/coords/3");
        int statusCode2 = response2.getStatusCode();
        assertEquals(200, statusCode2);

        Response response3 = RestAssured.given().when().delete("/coords/0");
        int statusCode3 = response3.getStatusCode();
        assertEquals(200, statusCode3);

        Response response4 = RestAssured.given().header("Content-Type", "application/json").body(requestBody).when().post("/coords");
        int statusCode4 = response4.getStatusCode();
        assertEquals(200, statusCode4);

        Response response5 = RestAssured.given().when().get("/coords");
        JSONArray returnArray = new JSONArray(response5.getBody().asString());
        JSONObject obj = returnArray.getJSONObject(6);
        int id = (int) obj.get("id");

        Response response6 = RestAssured.given().when().delete("/coords/"+id);
        int statusCode6 = response6.getStatusCode();
        assertEquals(200, statusCode6);


    }



}

