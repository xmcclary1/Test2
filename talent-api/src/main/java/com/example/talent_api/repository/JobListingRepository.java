package com.example.talent_api.repository;

import com.example.talent_api.domain.JobListing;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface JobListingRepository extends MongoRepository<JobListing, String> {
    List<JobListing> findByPostedBy(String postedBy);
}
