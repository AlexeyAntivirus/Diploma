package com.rx;

import com.rx.dao.repositories.DisciplineRepository;
import com.rx.dao.repositories.DocumentRepository;
import com.rx.dao.repositories.UserRepository;
import com.rx.helpers.DevDataAccessObjectCommandLineRunner;
import com.rx.helpers.ProdDataAccessObjectCommandLineRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class DiplomaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiplomaApplication.class, args);
	}

	@Bean
    @Profile("dev")
	public CommandLineRunner runner(UserRepository userRepository,
									DocumentRepository documentRepository,
									DisciplineRepository disciplineRepository,
									BCryptPasswordEncoder bCryptPasswordEncoder) {
		return new DevDataAccessObjectCommandLineRunner(userRepository, disciplineRepository, documentRepository, bCryptPasswordEncoder);
	}

    @Bean
    @Profile("prod")
    public CommandLineRunner prodRunner(UserRepository userRepository,
                                        BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new ProdDataAccessObjectCommandLineRunner(userRepository, bCryptPasswordEncoder);
    }
}
