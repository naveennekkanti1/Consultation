package org.example.repository;

import org.example.entity.FormResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FormResponseRepository
        extends MongoRepository<FormResponse, String> {

    List<FormResponse> findByFormId(
            String formId
    );

    boolean existsByFormIdAndRespondentEmail(
            String formId,
            String respondentEmail
    );
}