package com.example.talent_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.example.talent_api.domain.User;
import com.example.talent_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableMongoRepositories
public class TalentApiApplication implements CommandLineRunner {

	@Autowired
	UserRepository CustRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(TalentApiApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("-------------CREATE Users-----------------------------------\n");
		createUsers();
		System.out.println("-------------Creation Complete-----------------------------------\n");
	}

	void createUsers() {
		System.out.println("User creation started...");

		try {
			createUserIfNotExists("Connor", "Eastman", "connor123", "connor@gmail.com", "connor@123", "manager");
			createUserIfNotExists("Xavion", "Mcclaryfagan", "xavion123", "xavion@gmail.com", "xavion@123", "candidate");
			createUserIfNotExists("Sakshi", "Ghadigaonkar", "sakshi123", "sakshi@gmail.com", "sakshi@123", "manager");
			createUserIfNotExists("Harsh", "Kumar", "harsh123", "harsh@gmail.com", "harsh@123", "candidate");

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("User creation complete...");
	}


	void createUserIfNotExists(String firstname, String lastname, String username, String email, String password, String role) {
		if (CustRepo.existsByUsername(username)) {
			System.out.println("User with username '" + username + "' already exists. Skipping creation.");
		} else {
			User user = new User();
			user.setFirstname(firstname);
			user.setLastname(lastname);
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(passwordEncoder.encode(password));
			user.setPhone(908389392L);
			user.setRole(role);
			CustRepo.save(user);
			System.out.println("User with username '" + username + "' created successfully.");
		}
	}
}