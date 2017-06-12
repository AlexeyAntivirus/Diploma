package com.rx;

import com.rx.dao.repositories.DisciplineRepository;
import com.rx.dao.repositories.DocumentRepository;
import com.rx.dao.repositories.UserRepository;
import com.rx.helpers.DataAccessObjectCommandLineRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;


@SpringBootApplication
public class DiplomaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiplomaApplication.class, args);
	}

	@Bean
    @Profile("dev")
	public CommandLineRunner runner(UserRepository userRepository, DocumentRepository documentRepository, DisciplineRepository disciplineRepository) {
		return new DataAccessObjectCommandLineRunner(userRepository, disciplineRepository, documentRepository);
	}
}
