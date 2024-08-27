package com.example.talent_api.service;

import com.example.talent_api.dto.JwtAuthResponse;
import com.example.talent_api.dto.LoginDto;
import com.example.talent_api.dto.RegisterDto;

public interface AuthService {

    String register(RegisterDto registerDto);

    JwtAuthResponse login(LoginDto loginDto);
}