package com.example.talent_api.service.Impl;

import com.example.talent_api.domain.User;
import com.example.talent_api.dto.JwtAuthResponse;
import com.example.talent_api.exception.ResourceNotFoundException;
import com.example.talent_api.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import com.example.talent_api.dto.LoginDto;
import com.example.talent_api.dto.RegisterDto;
import com.example.talent_api.repository.UserRepository;
import com.example.talent_api.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String register(RegisterDto registerDto) {

        // Check if the username already exists in the database
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        // Check if the email already exists in the database
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }

        User user = new User();
        user.setFirstname(registerDto.getFirstname());
        user.setLastname(registerDto.getLastname());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setPhone(registerDto.getPhone());

        // Set the role from the RegisterDto, assuming it is passed from a dropdown
        String role = registerDto.getRole();
        if (!role.equalsIgnoreCase("manager") && !role.equalsIgnoreCase("candidate")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role specified!");
        }
        user.setRole(role);

        userRepository.save(user);

        return "User Registered Successfully!";
    }


//    @Override
//    public JwtAuthResponse login(LoginDto loginDto) {
//
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                loginDto.getUsernameOrEmail(),
//                loginDto.getPassword()
//        ));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String token = jwtTokenProvider.generateToken(authentication);
//
//        Optional<User> userOptional = userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(),
//                loginDto.getUsernameOrEmail());
//
//        if (userOptional.isPresent()) {
//            User loggedInUser = userOptional.get();
//            String role = loggedInUser.getRole();
//            logger.info("User found: " + loggedInUser.getUsername() + " with role: " + role);
//            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
//            jwtAuthResponse.setRole(role);
//            jwtAuthResponse.setAccessToken(token);
//            return jwtAuthResponse;
//        } else {
//            logger.warn("User not found for username or email: " + loginDto.getUsernameOrEmail());
//            throw new ResourceNotFoundException("User not found");
//        }
//    }
//

    @Override
    public JwtAuthResponse login(LoginDto loginDto) {

        logger.info("Attempting to authenticate user with username or email: " + loginDto.getUsernameOrEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsernameOrEmail(),
                    loginDto.getPassword()
            ));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(authentication);

            Optional<User> userOptional = userRepository.findByUsernameOrEmail(loginDto.getUsernameOrEmail(),
                    loginDto.getUsernameOrEmail());

            if (userOptional.isPresent()) {
                User loggedInUser = userOptional.get();
                String role = loggedInUser.getRole();
                logger.info("User found: " + loggedInUser.getUsername() + " with role: " + role);
                JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
                jwtAuthResponse.setRole(role);
                jwtAuthResponse.setAccessToken(token);
                return jwtAuthResponse;
            } else {
                logger.warn("User not found for username or email: " + loginDto.getUsernameOrEmail());
                throw new ResourceNotFoundException("User not found");
            }
        } catch (Exception e) {
            logger.error("Authentication failed for username or email: " + loginDto.getUsernameOrEmail(), e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

}
