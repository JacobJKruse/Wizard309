package com.example.mysqlconnectiontest;

import com.example.mysqlconnectiontest.Card.AttackType;
import com.example.mysqlconnectiontest.Card.Card;
import com.example.mysqlconnectiontest.Card.CardRepository;
import com.example.mysqlconnectiontest.Card.Element;
import com.example.mysqlconnectiontest.Characters.CharacterController;
import com.example.mysqlconnectiontest.Characters.CharacterRepository;
import com.example.mysqlconnectiontest.Characters.Characters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class MySqlConnectionTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySqlConnectionTestApplication.class, args);
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
