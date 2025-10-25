package org.example.repository;

import org.apache.catalina.User;
import org.example.entity.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {
    public Optional<UserModel> findByEmail(String email);
}
