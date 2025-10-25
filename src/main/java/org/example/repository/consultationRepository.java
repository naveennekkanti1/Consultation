package org.example.repository;

import org.example.entity.model;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface consultationRepository extends MongoRepository<model,String> {
    Optional<model> findById(String id);

}
