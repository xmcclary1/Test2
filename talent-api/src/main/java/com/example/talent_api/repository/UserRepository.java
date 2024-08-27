//package com.example.talent_api.repository;
//
//import java.util.Optional;
//
//import org.springframework.data.mongodb.repository.MongoRepository;
//
//import com.example.talent_api.domain.User;
//
//public interface UserRepository extends MongoRepository<User, String>{
//    User findByUsername(String username);
//    Optional<User> findByUsernameOrEmail(String username, String email);
//    Boolean existsByUsername(String username);
//    Boolean existsByEmail(String email);
//}


package com.example.talent_api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.talent_api.domain.User;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);

    @Query("{'$or': [{'username': {$regex: ?0, $options: 'i'}}, {'email': {$regex: ?1, $options: 'i'}}]}")
    Optional<User> findByUsernameOrEmail(String username, String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
