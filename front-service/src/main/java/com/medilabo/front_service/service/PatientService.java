package com.medilabo.front_service.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.medilabo.front_service.model.Patient;
import com.medilabo.front_service.security.JwtService;

@Service
public class PatientService {

    private final RestClient restClient;
    private final JwtService jwtService;

    public PatientService(
            JwtService jwtService,
            @Value("${gateway.url:http://localhost:8080}") String gatewayUrl) {

        this.jwtService = jwtService;

        this.restClient = RestClient.builder()
                .baseUrl(gatewayUrl)
                .build();
    }

    public List<Patient> getAllPatients() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        String token = jwtService.generateToken(username);

        // Get all patients from the gateway using the user's JWT.
        Patient[] patients = restClient.get()
                .uri("/patients")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(Patient[].class);

        return Arrays.asList(patients);
    }

    public Patient createPatient(Patient patient) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        String token = jwtService.generateToken(username);

        // Send the new patient to patient-service through the gateway.
        return restClient.post()
                .uri("/patients")
                .header("Authorization", "Bearer " + token)
                .body(patient)
                .retrieve()
                .body(Patient.class);
    }

    public Patient getPatientById(Long id) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        String token = jwtService.generateToken(username);

        // Get one patient from patient-service through the gateway.
        return restClient.get()
                .uri("/patients/{id}", id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(Patient.class);
    }

    public Patient updatePatient(Long id, Patient patient) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        String token = jwtService.generateToken(username);

        // Send the updated patient to patient-service through the gateway.
        return restClient.put()
                .uri("/patients/{id}", id)
                .header("Authorization", "Bearer " + token)
                .body(patient)
                .retrieve()
                .body(Patient.class);
    }
}