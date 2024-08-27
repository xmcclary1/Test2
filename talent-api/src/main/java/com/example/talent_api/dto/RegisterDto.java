package com.example.talent_api.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private long phone;
    private String role;

    private List<String> jobListingIds = new ArrayList<>();

}
