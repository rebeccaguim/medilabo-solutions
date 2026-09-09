package com.medilabo.front_service.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.medilabo.front_service.model.Patient;
import com.medilabo.front_service.security.JwtService;

@Service
public class PatientService {

    private final RestClient restClient;
    private final JwtService jwtService;

    public PatientService(JwtService jwtService) {
        this.jwtService = jwtService;

        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public List<Patient> getAllPatients() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        String token = jwtService.generateToken(username);

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

        return restClient.put()
                .uri("/patients/{id}", id)
                .header("Authorization", "Bearer " + token)
                .body(patient)
                .retrieve()
                .body(Patient.class);
    }
}