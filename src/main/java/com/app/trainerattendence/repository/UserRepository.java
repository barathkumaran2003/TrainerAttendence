package com.app.trainerattendence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.app.trainerattendence.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    User findByUserId(String userId);
}
