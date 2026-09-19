package com.medilabo.front_service.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.medilabo.front_service.security.JwtService;

@Service
public class RiskService {

    private final RestClient restClient;
    private final JwtService jwtService;

    public RiskService(JwtService jwtService) {
        this.jwtService = jwtService;

        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public String getRiskByPatientId(Long patientId) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        String token = jwtService.generateToken(username);

        return restClient.get()
                .uri("/risk/{id}", patientId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);
    }
}