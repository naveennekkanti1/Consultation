package org.example.repository;

import org.example.entity.model;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface consultationRepository extends MongoRepository<model,String> {

}
