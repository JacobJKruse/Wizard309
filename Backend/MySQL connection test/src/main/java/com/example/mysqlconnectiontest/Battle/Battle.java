package com.example.mysqlconnectiontest.Battle;

import com.example.mysqlconnectiontest.Card.Card;
import com.example.mysqlconnectiontest.Card.CardRepository;
import com.example.mysqlconnectiontest.Element.Element;
import com.example.mysqlconnectiontest.Enemy.Enemy;
import com.example.mysqlconnectiontest.Enemy.EnemyRepository;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventory;
import com.example.mysqlconnectiontest.EnemyCardInventory.EnemyCardInventoryRepository;
import com.example.mysqlconnectiontest.Entities;
import com.example.mysqlconnectiontest.Wizard.Wizard;
import com.example.mysqlconnectiontest.Wizard.WizardRepository;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Session;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Thread.sleep;

@Setter
@Getter
@Component
public class Battle implements Runnable {


    final int FIRE = 0;
    final int ICE = 1;
    final int STORM = 2;
    final int EARTH = 3;
    final int LIFE = 4;
    final int DEATH = 5;

    public static Map<Element, Integer> type = new Hashtable<>();


    final double[][] EFFECTIVENESS = {{0.75, 1.5, 1, 1, 1, 1},           //FIRE
                                      {1, 0.75, 1.5, 1, 1, 1},             //ICE
                                      {1.5, 1, 0.75, 1, 1, 1},              //STORM
                                      {1, 1, 1, 0.75, 1, 1.5},           //EARTH
                                      {1, 1, 1, 1.5, 0.75, 1},           //LIFE
                                      {1, 1, 1, 1, 1.5, 0.75}            //DEATH
    };

    private static WizardRepository wizardRepository;
    private static EnemyRepository enemyRepository;
    private static CardRepository cardRepository;
    private static EnemyCardInventoryRepository enemyCardInventoryRepository;
    public static Map<Integer, Battle> battleSessionMap = new Hashtable<>();
    public int roomId;

    @Autowired
    public void setWizardRepository(WizardRepository repo) {
        wizardRepository = repo;

    }

    @Autowired
    public void setEnemyRepository(EnemyRepository repo) {
        enemyRepository = repo;
    }

    @Autowired
    public void setCardRepository(CardRepository repo) {
        cardRepository = repo;
    }

    @Autowired
    public void setEnemyCardInventoryRepository(EnemyCardInventoryRepository repo) {
        enemyCardInventoryRepository = repo;
    }


    ArrayList<Wizard> players = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();
    Queue<Entities> queue = new LinkedList<>();


    private Map<Wizard, Session> wizardSessionMap = new Hashtable<>();
    private Map<Session, Wizard> sessionWizardMap = new Hashtable<>();
    private int playerCount = 0;
    private int enemyCount = 0;
    private String turn;
    private int maxPlayers;
    private boolean isEnemyTurn;
    Thread gameThread;

    //logger
    private final Logger logger = LoggerFactory.getLogger(Battle.class);

    public Battle(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
    public Battle() {

    }


    public void addPlayer(Wizard w, Session s) throws IOException, EncodeException {
        if(playerCount >= maxPlayers) {
            s.getBasicRemote().sendText("Game is full");
            s.close();

        }
        else {
            players.add(w);
            wizardSessionMap.put(w, s);
            sessionWizardMap.put(s, w);
            playerCount = players.size();
            queue.add(w);
            sessionWizardMap.forEach((session, username) -> {
                try {
                    session.getBasicRemote().sendObject(sendGameState());
                } catch (EncodeException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void addEnemy(Enemy e) {
        enemies.add(e);
        queue.add(e);
        enemyCount = enemies.size();
        sessionWizardMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendObject(sendGameState());
            } catch (EncodeException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void removePlayer(Wizard w) throws IOException {
        players.remove(w);
        queue.remove(w);
        playerCount--;
        wizardSessionMap.get(w).close();
        sessionWizardMap.remove(wizardSessionMap.get(w));
        wizardSessionMap.remove(w);
    }



    public void removePlayer(Session s) throws IOException {
        Wizard w = sessionWizardMap.get(s);
        players.remove(w);
        queue.remove(w);
        playerCount--;
        wizardSessionMap.get(w).close();
        sessionWizardMap.remove(wizardSessionMap.get(w));
        wizardSessionMap.get(w).close();
        wizardSessionMap.remove(w);
        sessionWizardMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendObject(sendGameState());
            } catch (EncodeException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void removeEnemy(Enemy e) {
        enemies.remove(e);
        queue.remove(e);
        enemyCount = enemies.size();

    }

    public void attackEnemy(PlayerAttack a, Session s) throws IOException, InterruptedException {
        //skip is signified if cardId is -1
        if(a.getCardId() == -1) {
            sessionWizardMap.forEach((session, wizard) -> {
                try {
                    session.getBasicRemote().sendText("Player " + sessionWizardMap.get(s).getDisplayName() + " has skipped their turn");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            sessionWizardMap.get(s).setCurrMana(sessionWizardMap.get(s).getCurrMana() + 10);
            nextTurn();
            return;
        }

        //finds wizard from map
        Wizard attacker = sessionWizardMap.get(s);
        //finds wizard by id
        Enemy defender = null;
        for (Enemy enemy : enemies) {
            if (enemy.getId() == a.getEnemyId()) {
                defender = enemy;
            }
        }
        if(defender == null) {
            logger.info("enemy is null");
            return;
        }

        Card c = cardRepository.findById(a.getCardId());
        //attacks enemy
        attacker.setCurrMana(attacker.getCurrMana() - c.getManaCost());
        double stab;
        if(attacker.getElement().equals(c.getElement())) {
            stab = 1.5;
        }
        else {
            stab = 1;
        }
        double RAND = Math.random() * 0.4 + 0.8;
        int dmg = (int) ((((((2*(attacker.getLvl()) * EFFECTIVENESS[c.getElement().ordinal()][defender.getElement().ordinal()])/5 + 2) * c.getAttackPower())/50) + 2) * stab * RAND);
        defender.setCurrHP(defender.getCurrHP() - dmg);


        Enemy finalDefender1 = defender;
        sessionWizardMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText("Card " + c.getSpellName() + " was dropped on " + finalDefender1.getDisplayName());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        if(defender.getCurrHP() <= 0) {
            Enemy finalDefender = defender;
            sessionWizardMap.forEach((session, username) -> {
                try {
                    session.getBasicRemote().sendText("Enemy " + finalDefender.getDisplayName() + " has died");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            });
            matchReward(attacker, defender);
            removeEnemy(defender);
        }

        if(players.isEmpty()) {
            sessionWizardMap.forEach((session, username) -> {
                try {
                    session.getBasicRemote().sendObject(sendGameState());
                    session.getBasicRemote().sendText("Enemies has Won");

                } catch (EncodeException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            killGame();
            return;
        }

        if(enemies.isEmpty()) {
            sessionWizardMap.forEach((session, wizard) -> {
                try {
                    session.getBasicRemote().sendObject(sendGameState());
                    session.getBasicRemote().sendText("Wizards has Won");
                    //resets mana back to max
                    wizard.setCurrMana(wizard.getMaxMana());

                    //saves hp into the database
                    wizardRepository.save(sessionWizardMap.get(session));

                } catch (EncodeException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            killGame();
            return;
        }


        nextTurn();

    }

    public void nextTurn() {
        Entities e = queue.poll();
        queue.add(e);
        turn = queue.peek().getDisplayName();
        queue.peek().setCurrMana(queue.peek().getCurrMana() + 10);

        sessionWizardMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendObject(sendGameState());
            } catch (EncodeException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        if(queue.peek() instanceof Enemy) {
            isEnemyTurn = true;
        }
        else {
            isEnemyTurn = false;
        }
    }

    public void AttackPlayer(PlayerAttack a) {
        Enemy attacker = enemyRepository.findById(a.getEnemyId());
//        Wizard defender = wizardRepository.findById(a.getWizardId());
        Card c = cardRepository.findById(a.getCardId());

        Wizard defender = null;
        for (Wizard w : players) {
            if (w.getId() == a.getWizardId()) {
                defender = w;
            }
        }


        double stab;
        if(attacker.getElement().equals(c.getElement())) {
            stab = 1.5;
        }
        else {
            stab = 1;
        }
        double RAND = Math.random() * 0.4 + 0.8;
        int dmg = (int) ((((((2*(attacker.getLvl()) * EFFECTIVENESS[c.getElement().ordinal()][defender.getElement().ordinal()])/5 + 2) * c.getAttackPower())/50) + 2) * stab * RAND);
        defender.setCurrHP(defender.getCurrHP() - dmg);
//        wizardRepository.save(defender);

        Entities e = queue.poll();
        queue.add(e);
        turn = queue.peek().getDisplayName();
        queue.peek().setCurrMana(queue.peek().getCurrMana() + 10);

        sessionWizardMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendObject(a);
            } catch (EncodeException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        sessionWizardMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendObject(sendGameState());
            } catch (EncodeException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        if(queue.peek() instanceof  Enemy) {
            isEnemyTurn = true;
        }
        else {
            isEnemyTurn = false;
        }
    }

    public void attackPlayer(Enemy attacker) throws IOException, InterruptedException {
        sleep(3000);
        //picks which player to attack
        int p = (int) (Math.random() * players.size() - 1);
        Wizard defender = players.get(p);

        List<EnemyCardInventory> enemyCards = enemyCardInventoryRepository.getAllByEnemyId(roomId);
        int cIndex = (int) (Math.random() * enemyCards.size() - 1);
        Card c = enemyCards.get(cIndex).getCard();

        double stab;
        if(attacker.getElement().equals(c.getElement())) {
            stab = 1.5;
        }
        else {
            stab = 1;
        }
        double RAND = Math.random() * 0.4 + 0.8;
        int dmg = (int) ((((((2*(attacker.getLvl()) * EFFECTIVENESS[c.getElement().ordinal()][defender.getElement().ordinal()])/5 + 2) * c.getAttackPower())/50) + 2) * stab * RAND);
        defender.setCurrHP(defender.getCurrHP() - dmg);

        PlayerAttack a = new PlayerAttack(defender.getId(), attacker.getId(), c.getId());


        sessionWizardMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText("Card " + c.getSpellName() + " was dropped on " + defender.getDisplayName());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        if(defender.getCurrHP() <= 0) {
            Wizard finalDefender = defender;
            sessionWizardMap.forEach((session, wizard) -> {
                try {
                    session.getBasicRemote().sendText("Wizard " + finalDefender.getDisplayName() + " has died");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            defender.setCurrHP(defender.getMaxHP() / 2);
            defender.setCurrMana(defender.getMaxMana());
            wizardRepository.save(defender);
            removePlayer(defender);
        }

        if(players.isEmpty()) {
            sessionWizardMap.forEach((session, username) -> {
                try {
                    session.getBasicRemote().sendObject(sendGameState());
                    session.getBasicRemote().sendText("Enemies has Won");

                } catch (EncodeException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            killGame();
            return;
        }

        if(enemies.isEmpty()) {
            sessionWizardMap.forEach((session, username) -> {
                try {
                    session.getBasicRemote().sendObject(sendGameState());
                    session.getBasicRemote().sendText("Wizards has Won");

                } catch (EncodeException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            killGame();
            return;
        }

        nextTurn();


    }



    public GameState sendGameState() {

        return new GameState(turn, players, enemies);
    }




    public void startGameThread() {

        Entities e = queue.peek();
        turn = e.getDisplayName();
        if(queue.peek() instanceof  Enemy) {
            isEnemyTurn = true;
        }
        else {
            isEnemyTurn = false;
        }
//        queue.add(e);
        sessionWizardMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendObject(sendGameState());
            } catch (EncodeException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void gameLoop() {
//        if(players.size() < 2) {
//            logger.info("Need at least 2 players to start the game");
//        }

//        while(true) {
//
//            logger.info(String.valueOf(playerCount));
//        }


    }

    public void killGame() {
        sessionWizardMap.forEach(((session, wizard) -> {

            try {
                session.getBasicRemote().sendText("Game has ended");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            try {
                session.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }));
        battleSessionMap.remove(roomId);
    }

    public void run() {

        while(!players.isEmpty() || !enemies.isEmpty()) {

            try {
                sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            if(isEnemyTurn) {
                try {
                    attackPlayer((Enemy) queue.peek());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }

            }


        }


        


    }

    public void matchReward(Wizard w, Enemy e){
        int wizCurrXP = w.getCurrXp();
        int wizNextLvlXP = w.getNextLvlXp();
        int reward = e.getGoldDrop();
        int xpDrop = e.getXpDrop();
        int LVL = w.getLvl();

        int totalXP = wizCurrXP  + xpDrop;
        while (totalXP >=  wizNextLvlXP){
            totalXP = totalXP-wizNextLvlXP;
            LVL++;
            xpDrop += LVL*100;
            wizNextLvlXP = LvlUp(LVL);
        }
        w.setCurrXp(totalXP);
        w.setLvl(LVL);
        w.setNextLvlXp(wizNextLvlXP);
        w.setWallet(w.getWallet()+reward);
        wizardRepository.save(w);
    }

    public int LvlUp(int lvl){
        return (int) (100*Math.pow(1.25,lvl-1));
    }

}
