package com.medilabo.front_service.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.front_service.dto.LoginRequest;
import com.medilabo.front_service.security.JwtService;

/** Handles user login and returns an access token. */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /** Checks login details and creates a JWT.
     * @param loginRequest submitted username and password
     * @return signed access token
     */
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {

        // Check the login details before creating a JWT.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        return jwtService.generateToken(loginRequest.getUsername());
    }
}