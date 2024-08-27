package com.example.talent_api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.talent_api.domain.User;
import com.example.talent_api.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserAPI {
	@Autowired
	UserRepository repo;

	@GetMapping
	public Iterable<User> getAll() {
		return repo.findAll();
	}

	@GetMapping("/{userId}")
	public Optional<User> getCustomerById(@PathVariable("userId") String id) {
		return repo.findById(id);
	}

    @GetMapping("/username/{username}")
	public User getUserByUsername(@PathVariable("username") String username) {
		return repo.findByUsername(username);
	}


    @PostMapping
	public ResponseEntity<User> createUser(@RequestBody User newUser) {
		try {
			User savedUser = repo.save(newUser);
			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}	

    @DeleteMapping("/userId")
    public void deleteUserById(@PathVariable("userId") String id) {
        repo.deleteById(id);
    }
}