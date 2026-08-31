package com.medilabo.front_service.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.medilabo.front_service.model.Patient;

@Service
public class PatientService {

    private final RestClient restClient;

    public PatientService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public List<Patient> getAllPatients() {
        Patient[] patients = restClient.get()
                .uri("/patients")
                .retrieve()
                .body(Patient[].class);

        return Arrays.asList(patients);
    }
}