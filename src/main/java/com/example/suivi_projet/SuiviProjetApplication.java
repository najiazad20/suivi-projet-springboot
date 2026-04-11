package com.example.suivi_projet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.suivi_projet.organisation.repositories.EmployeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SuiviProjetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuiviProjetApplication.class, args);
	}

	@Bean
	CommandLineRunner start(EmployeRepository repo, PasswordEncoder encoder) {
		return args -> {
			repo.findAll().forEach(e -> {
				if (e.getPassword() != null && !e.getPassword().startsWith("$2a$")) {
					e.setPassword(encoder.encode(e.getPassword()));
					repo.save(e);
					System.out.println("Mot de passe haché pour l'utilisateur : " + e.getLogin());
				}
			});
		};
	}

}
