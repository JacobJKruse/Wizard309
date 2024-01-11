package com.example.mysqlconnectiontest;
import com.example.mysqlconnectiontest.Element.Element;
import com.example.mysqlconnectiontest.Wizard.Wizard;
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

import static java.lang.Integer.parseInt;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@RunWith(SpringRunner.class)
public class WizardSystemTesting {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void getAllWizardsTest() throws JSONException {
        Response response = RestAssured.given().when().get("/wizards");
        assertEquals(200, response.getStatusCode());

        response.then().body("size()", equalTo(16));

        JSONArray returnArr = new JSONArray(response.getBody().asString());
        JSONObject returnObj = returnArr.getJSONObject(0);

        assertEquals(1, returnObj.get("id"), "id is incorrect");
        assertEquals("Boxcar", returnObj.get("displayName"), "name is incorrect");
        assertEquals("Death", returnObj.get("element"), "element is incorrect");
        assertEquals("life_icon", returnObj.get("extension"), "extension is wrong");

    }


    @Test
    public void getWizardTest() throws JSONException {
        Response response = RestAssured.given().when().get("/wizards/1");
        assertEquals(200, response.getStatusCode());
        JSONObject obj = new JSONObject(response.getBody().asString());

        assertEquals(1, obj.get("id"), "id is incorrect");
        assertEquals("Boxcar", obj.get("displayName"), "name is incorrect");
        assertEquals("Death", obj.get("element"), "element is incorrect");
        assertEquals("life_icon", obj.get("extension"), "extension is wrong");

    }

    @Test
    public void createUpdateDeleteWizardTest() throws JSONException {
        Wizard w = new Wizard("tester", 100, 100, 100, 100, 1, 100, 0, Element.Fire, 100);
        Gson g = new Gson();

        Response response = RestAssured.given().header("Content-Type", "application/json").body(g.toJson(w)).when().post("/wizards/39/test.png");
        assertEquals(200, response.getStatusCode());

        Response response2 = RestAssured.given().get("/wizards/user/39");
        assertEquals(200, response2.getStatusCode());
        response2.then().body("size()", equalTo(1));
        JSONArray returnArr = new JSONArray(response2.getBody().asString());
        JSONObject returnObj = returnArr.getJSONObject(0);

        assertEquals(1, returnObj.get("lvl"), "lvl is incorrect");

        Wizard w2 = g.fromJson(returnObj.toString(), Wizard.class);
        int wId = w2.getId();
        int ogWallet = w2.getWallet();
        w2.setLvl(2);

        Response response3 = RestAssured.given().header("Content-Type", "application/json").body(g.toJson(w2)).when().put("/wizards/" + wId);
        response3.then().body("lvl", equalTo(2));

        Wizard w3 = g.fromJson(response3.asString(), Wizard.class);

        Response response4 = RestAssured.given().when().put("/wizards/wallet/" + wId + "/1000");
        assertEquals(response4.getBody().asString(), String.valueOf((w3.getWallet() + 1000)));

        Response response5 = RestAssured.given().when().get("/wizards/wallet/" + wId);
        assertEquals(response5.getBody().asString(), String.valueOf(w3.getWallet() + 1000));


        Response response6 = RestAssured.given().when().delete("/wizards/" + wId);
        assertEquals(200, response6.getStatusCode());
    }

}
