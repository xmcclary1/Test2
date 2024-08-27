package com.example.talent_api.domain;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document("users")
@Getter
@Setter
public class User {
    @Id
	private String id = new ObjectId().toString();

    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private long phone;
    private String role;
    private String resumeId;
    private List<String> jobListingIds = new ArrayList<>();

}
