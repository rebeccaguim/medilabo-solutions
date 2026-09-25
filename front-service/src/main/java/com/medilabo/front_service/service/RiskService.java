package com.medilabo.front_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.medilabo.front_service.security.JwtService;

@Service
public class RiskService {

    private final RestClient restClient;
    private final JwtService jwtService;

    public RiskService(
            JwtService jwtService,
            @Value("${gateway.url:http://localhost:8080}") String gatewayUrl) {

        this.jwtService = jwtService;

        this.restClient = RestClient.builder()
                .baseUrl(gatewayUrl)
                .build();
    }

    public String getRiskByPatientId(Long patientId) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        String token = jwtService.generateToken(username);

        // Get the patient's risk level from risk-service through the gateway.
        return restClient.get()
                .uri("/risk/{id}", patientId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);
    }
}