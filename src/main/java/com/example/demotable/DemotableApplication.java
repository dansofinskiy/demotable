package com.example.demotable;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class DemotableApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemotableApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(PlayerRepository repository) {
		return (args -> {
			repository.save(new Player("guy", 12));
			repository.save(new Player("femme", 11));
		});
	}

}
