package com.example.mysqlconnectiontest;

import com.example.mysqlconnectiontest.Enemy.Enemy;
import com.example.mysqlconnectiontest.Enemy.EnemyRepository;
import com.example.mysqlconnectiontest.EnemyMovement.MoveLoop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

@EnableJpaRepositories
@SpringBootApplication
public class MySqlConnectionTestApplication {

	private static EnemyRepository enemyRepository;
	@Autowired
	public void setEnemyRepository(EnemyRepository repo) {
		enemyRepository = repo;
	}

	public static void main(String[] args) throws InterruptedException {

		SpringApplication.run(MySqlConnectionTestApplication.class, args);

		ArrayList<MoveLoop> moveLoopArrayList = new ArrayList<MoveLoop>();

		int i = 0;
		for (Enemy e:enemyRepository.findAll()
		) {
			System.out.println(e.getDisplayName());

			moveLoopArrayList.add(new MoveLoop(e));

		}


		for(i=0;i<moveLoopArrayList.size();i++){
			moveLoopArrayList.get(i).startMoveThread();
		}


///		Enemy guy = new Enemy("test", 100, 100, 100, 100, 100, 100, 100, Element.Fire, "test_guy",3000,3000);
//		Enemy guy1 = enemyRepository.findById(8);
//		Enemy guy2 = enemyRepository.findById(5);
//		Enemy guy3 = enemyRepository.findById(6);
//		Enemy wisp = enemyRepository.findById(7);
//
//
////		Enemy wisp = new Enemy("Wisp", 100, 100, 100, 100, 100, 100, 100, Element.Ice, "wisp", 2000,2000);
//		MoveLoop m = new MoveLoop(guy1);
//		m.startMoveThread();
//		MoveLoop m2 = new MoveLoop(guy2);
//		m2.startMoveThread();
//		MoveLoop m3 = new MoveLoop(guy3);
//		m3.startMoveThread();
//		MoveLoop m4 = new MoveLoop(wisp);
//		m4.startMoveThread();
//
//		System.out.println(guy2.toString());


//		Battle b = new Battle(5);
//		b.startGameThread();
//		b.addPlayer();
//		sleep(5000);
//		b.addPlayer();
	}

//	@Bean
//	CommandLineRunner initCharacter(CharacterRepository characterRepository, CardRepository cardRepository){
//		return args -> {
//			Characters Oscar = new Characters("Oscar",5);
//			Characters Abhi = new Characters("Abhi",4);
//			Characters Jacob = new Characters("Jacob",4);
//			Characters Philip =new Characters("Philip",6);
//			Card card1 = new Card("Fireball", Element.Fire, 5, 2, AttackType.Single);
//			Card card2 = new Card("Ice Ball", Element.Ice, 4, 2, AttackType.Single);
//			Card card3 = new Card("Lighting Strike", Element.Storm, 6, 4, AttackType.Single);
//			Card card4 = new Card("Bone Shard",Element.Death,3,1,AttackType.Single );
//			Oscar.setCard(card1);
//			Abhi.setCard(card2);
//			Jacob.setCard(card3);
//			Philip.setCard(card4);
//			characterRepository.save(Oscar);
//			characterRepository.save(Abhi);
//			characterRepository.save(Jacob);
//			characterRepository.save(Philip);
//
//
//		};
//	}



}
