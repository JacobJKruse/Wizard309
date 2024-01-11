package com.example.mysqlconnectiontest.EnemyMovement;

import com.example.mysqlconnectiontest.Enemy.Enemy;
import com.example.mysqlconnectiontest.Enemy.EnemyRepository;
import com.example.mysqlconnectiontest.Map.MapRepository;
import com.example.mysqlconnectiontest.websocket.MovementServer;
import com.example.mysqlconnectiontest.websocket.Player;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.Session;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.PublicKey;
import java.util.*;

import static java.lang.Thread.sleep;

@Setter
@Getter
@Component
public class MoveLoop implements Runnable {

    private final Object lock = new Object();
    Thread moveThread;

    private static EnemyRepository enemyRepository;
    private static MapRepository mapRepository;
    private static CoordinateRepository coordinateRepository;

    @Autowired
    public void setEnemyRepository(EnemyRepository repo) {
        enemyRepository = repo;
    }

    @Autowired
    public void setMapRepository(MapRepository repo) { mapRepository = repo;}

    @Autowired
    public void setCoordinateRepository(CoordinateRepository repo) { coordinateRepository = repo;}

    private Enemy enemy;
    private Random rand = new Random();
    private Random coordRand = new Random();
    private String[] path;
    private int faceDir;
    private long lastDirectionChange;

    //wisp
    private List<Integer> xPath = new ArrayList<Integer>();
    private List<Integer> yPath = new ArrayList<Integer>();
    Boolean goingForward = true;
    private int currentIndex = 0;

    //change later
    private double maxX = 70*192;
    private double maxY = 50*192;

    private long lastDelta;
    private long nanoSecond;
    private long nowDelta;
    private double timeSinceLast;
    private double delta;

    private static Map<String, Session> usernameSessionMap = MovementServer.usernameSessionMap;
    private static Map<Session, String> sessionUsernameMap = MovementServer.sessionUsernameMap;
    private static Map<String, Player> players = MovementServer.players;
    private Player player;

    public MoveLoop(Enemy enemy){
        this.enemy = enemy;
    }

    public MoveLoop() {

    }

    public void startMoveThread(){
        moveThread = new Thread(this);
        moveThread.start();
    }

    @Override
    public void run() {

        if(enemy.getDisplayName().equals("Mitra")) {
            player = new Player(enemy.getStartX(), enemy.getStartY(), enemy.getDisplayName(), enemy.getId(), enemy.getExtension(),
                    enemy.getElement(), 1, 2);
        }
        else{
             player = new Player(enemy.getStartX(), enemy.getStartY(), enemy.getDisplayName(), enemy.getId(), enemy.getExtension(),
                    enemy.getElement(), 1, 0);
        }

        players.put(player.getUsername(), player);
        lastDelta = System.nanoTime();
        nanoSecond = 1000000000;

        if(player.getExtension().equals("wisp")) {

            List<Coordinate> coords = coordinateRepository.findAll();
            int value = player.getId() % coords.size();

            //System.out.println(coords.size());
            for (int i = 0; i < coords.size(); i++) {
                if(i == value){
                    path = coords.get(i).getCoords().split(" ");
                }
            }

            for(int i = 0; i< path.length; i++){
                //System.out.println(path[i]);
                String[] c = path[i].split(",");
                xPath.add(Integer.parseInt(c[0]));
                //System.out.println(c[0]);
                yPath.add(Integer.parseInt(c[1]));
                //System.out.println(c[1]);

            }


        }

        ObjectMapper om = new ObjectMapper();
        String json = "";

        while (true) {
            nowDelta = System.nanoTime();
            timeSinceLast = nowDelta - lastDelta;
            delta = timeSinceLast / nanoSecond;
            lastDelta = nowDelta;

            try {
                sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            EnemyMove(player);

            try {
                json = om.writeValueAsString(players);
            } catch (JsonProcessingException e) {
                System.out.println(e);
            }

            sendText(json);
        }
    }

    private void setPath() {
        List<Coordinate> coords = coordinateRepository.findAll();
        int value = coordRand.nextInt(1,coords.size())-1;

        Coordinate chosenCoordinate = coords.get(value);
        path = chosenCoordinate.getCoords().split(" ");

        for(String coord : path){
            String[] c = coord.split(",");
            xPath.add(Integer.parseInt(c[0]));
            yPath.add(Integer.parseInt(c[1]));
        }
    }

    private void sendText(String json) {
        synchronized (lock) {
            sessionUsernameMap.forEach((session, username) -> {
                synchronized (session) {
                    try {
                        if (session.isOpen()) {
                            try {
                                session.getBasicRemote().sendText(json);
                            } catch (IllegalStateException e) {
                            }
                        }
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            });
        }
    }


    public void EnemyMove(Player player){
        if (player.getExtension().equals("test_guy") || player.getExtension().equals("witch")) {


            int time = rand.nextInt(3000);
            if (System.currentTimeMillis() - lastDirectionChange >= time) {
                faceDir = rand.nextInt(4);
                lastDirectionChange = System.currentTimeMillis();
            }
            switch (faceDir) {
                case 0: //left
                    player.setX((float) (player.getX() - delta * 300));
                    if (player.getX() <= 0) {
                        faceDir = 1;
                    }
                    break;
                case 1: //right
                    player.setX((float) (player.getX() + delta * 300));
                    if (player.getX() >= maxX) {
                        faceDir = 0;
                    }
                    break;
                case 2:  //up
                    player.setY((float) (player.getY() - delta * 300));
                    if (player.getY() <= 0) {
                        faceDir = 3;
                    }
                    break;
                case 3: //down
                    player.setY((float) (player.getY() + delta * 300));
                    if (player.getY() >= maxY) {
                        faceDir = 2;
                    }
                    break;
            }
        }

        if (player.getExtension().equals("wisp")) {
            if (Math.abs(player.getX() - xPath.get(currentIndex)) > 25 || Math.abs(player.getY() - yPath.get(currentIndex)) > 25) {
                //FOR X MOVEMENT
                if (Math.abs(player.getX() - xPath.get(currentIndex)) > 25) {
                    if (player.getX() < xPath.get(currentIndex)) {
                        faceDir = 1;
                        player.setX((float) (player.getX() + delta * 300));
                    } else {
                        faceDir = 0;
                        player.setX((float) (player.getX() - delta * 300));
                    }
                }
                //FOR Y MOVEMENT
                if (Math.abs(player.getY() - yPath.get(currentIndex)) > 25) {
                    if (player.getY() < yPath.get(currentIndex)) {
                        faceDir = 3;
                        player.setY((float) (player.getY() + delta * 300));
                    } else {
                        faceDir = 2;
                        player.setY((float) (player.getY() - delta * 300));
                    }
                }
            } else {
                if (goingForward) {
                    if (currentIndex < xPath.size() - 1) {
                        currentIndex++;
                    } else {
                        goingForward = false;
                        currentIndex--;
                    }
                } else {
                    if (currentIndex > 0) {
                        currentIndex--;
                    } else {
                        goingForward = true;
                    }
                }

            }


        }


    }
}



