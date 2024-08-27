package com.example.talent_api.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "applicants")
public class Applicant {

    // Getters and Setters
    @Setter
    @Getter
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private long phone;
    private String resume;
    private String status = "pending"; // Default status
    private String role;

    public Applicant() {}

}
