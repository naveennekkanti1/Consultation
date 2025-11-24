package org.example.repository;

import org.example.entity.JobModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<JobModel,String> {

}
