package com.medilabo.risk_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.medilabo.risk_service.model.Patient;

@Service
public class PatientService {

    private final RestClient restClient;

    public PatientService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }

    public Patient getPatientById(Long patientId) {

        return restClient.get()
                .uri("/patients/{id}", patientId)
                .retrieve()
                .body(Patient.class);
    }
}