package com.app.trainerattendence.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.trainerattendence.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
