package com.example.mysqlconnectiontest.Enemy;

public class EnemyFactory {
    public Enemy createEnemy(String request) {
        Enemy enemy = null;
        if("Wisp".equals(request)) {
            enemy = new Wisp();
        }
        else if("BasicEnemy".equals(request)) {
            enemy = new BasicEnemy();
        }
        else if("Mimic".equals(request)){
            enemy = new Mimic();
        }
        else if("Witch".equals(request)){
            enemy = new Witch();
        }
        else if("Boss".equals(request)){
            enemy = new Boss();
        }

        return enemy;
    }
}
