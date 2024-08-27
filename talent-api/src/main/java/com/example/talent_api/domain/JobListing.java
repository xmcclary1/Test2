package com.example.talent_api.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Document(collection = "job_listings")
public class JobListing {

    @Id
    private String id;

    private String title;

    private String description;

    private String location;
    private String companyName;
    private String employmentType; // e.g., Full-time, Part-time
    private String postedBy; // The ID of the hiring manager
    private String postedByRole; // Role of the hiring manager
    private double salary;

    private boolean deleted = false;

    @Setter
    @Getter
    private List<Applicant> applicants = new ArrayList<>();

    public JobListing() {}

    // Getters and Setters

}
