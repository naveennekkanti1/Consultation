package org.example.repository;

import org.example.entity.MeetingModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends MongoRepository<MeetingModel, String> {
}
