package org.example.repository;

import org.example.entity.NewsLetterModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsLetterRepository extends MongoRepository<NewsLetterModel,String> {
    Optional<NewsLetterModel> findByEmail(String email);
    List<NewsLetterModel> findByIsSubscribed(boolean isSubscribed);
}
