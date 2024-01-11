package com.example.mysqlconnectiontest.websocket;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


import com.example.mysqlconnectiontest.Battle.PlayerAttack;
import com.example.mysqlconnectiontest.Battle.Battle;
import com.example.mysqlconnectiontest.Enemy.Enemy;
import com.example.mysqlconnectiontest.Enemy.EnemyFactory;
import com.example.mysqlconnectiontest.Enemy.EnemyRepository;
import com.example.mysqlconnectiontest.RepositoryHolder;
import com.example.mysqlconnectiontest.Wizard.Wizard;
import com.example.mysqlconnectiontest.Wizard.WizardRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
@ServerEndpoint(value = "/BattleRoom/{WizardId}/{roomId}",
decoders = PlayerAttackDecoder.class,
encoders = {PlayerAttackEncoder.class, GameStateEncoder.class})
public class BattleServer {

private static WizardRepository wizardRepository;

private static EnemyRepository enemyRepository;


//private static ArrayList<Battle> battles = new ArrayList<>();
public static Map<Integer, Battle> battleSessionMap = Battle.battleSessionMap;


private final Logger logger = LoggerFactory.getLogger(BattleServer.class);

@Autowired
public void setWizardRepository(WizardRepository repo) {
    wizardRepository = repo;
}

@Autowired
public void setEnemyRepository(EnemyRepository repo) { enemyRepository = repo; }

    public static void setBattleSessionMap(Map<Integer, Battle> battleSessionMap) {
        BattleServer.battleSessionMap = battleSessionMap;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("WizardId") int wizardId, @PathParam("roomId") int roomId) throws IOException, EncodeException {
    Wizard w = RepositoryHolder.getWizardRepository().findById(wizardId);
        EnemyFactory ef = new EnemyFactory();
    if(battleSessionMap.containsKey(roomId)) {
        battleSessionMap.get(roomId).addPlayer(w, session);
        if(enemyRepository.findById(roomId).getClass().getSimpleName() != "Boss") {
            Enemy e = enemyRepository.findById(roomId);
            String request = e.getClass().getSimpleName();
            Enemy newEnemy = ef.createEnemy(request);
            newEnemy.setId(battleSessionMap.get(roomId).getEnemies().get(battleSessionMap.get(roomId).getEnemyCount() - 1).getId() + 1);
            newEnemy.setDeckNoPersistence();

            battleSessionMap.get(roomId).addEnemy(newEnemy);
        }
        logger.info("Wizard " + w.getDisplayName() + " has joined room " + roomId );

    }
    else {
        Battle b = new Battle(5);
        b.setRoomId(roomId);
        Enemy e = enemyRepository.findById(roomId);
        battleSessionMap.put(roomId, b);
        battleSessionMap.get(roomId).addPlayer(w, session);
        b.addEnemy(e);
        logger.info("New Battle room has been made");
        logger.info("Wizard " + w.getDisplayName() + " has joined room " + roomId );
        battleSessionMap.get(roomId).startGameThread();
    }

}


    @OnMessage
    public void onMessage(Session session, String message, @PathParam("roomId") int roomId) throws IOException {
        // Parse the JSON string to a JSONObject
        JSONObject jsonObject = null;
        try {
            logger.info(message);
            System.out.println(message);
            jsonObject = new JSONObject(message);
            // Extract values for wizardId, enemyId, and cardId
            int wizardId = jsonObject.getInt("wizardId");
            int enemyId = jsonObject.getInt("enemyId");
            int cardId = jsonObject.getInt("cardId");
            // Create a new PlayerAttack object
            PlayerAttack attack = new PlayerAttack(wizardId, enemyId, cardId);

            // Call the attackEnemy method
            battleSessionMap.get(roomId).attackEnemy(attack, session);
        } catch (JSONException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }




@OnClose
public void onClose(Session session, @PathParam("roomId") int roomId) throws IOException {
    wizardRepository.save(battleSessionMap.get(roomId).getSessionWizardMap().get(session));
    battleSessionMap.get(roomId).removePlayer(session);
    if(battleSessionMap.get(roomId).getPlayers().size() <= 0) {
        battleSessionMap.get(roomId).killGame();
    }

    battleSessionMap.get(roomId).nextTurn();
}



}
