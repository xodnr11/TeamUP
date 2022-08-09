package com.example.TeamUP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing//Jpa 활성화
@SpringBootApplication
public class TeamUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamUpApplication.class, args);
	}

}
