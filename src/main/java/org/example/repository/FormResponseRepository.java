package org.example.repository;

import org.example.entity.FormResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FormResponseRepository extends MongoRepository<FormResponse, String> {

    List<FormResponse> findByFormId(String formId);

    @Query(value = "{ 'formId': ?0, 'answers.email': ?1 }", exists = true)
    Boolean existsByEmail(String formId, String email);

}
