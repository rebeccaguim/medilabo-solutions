package com.medilabo.risk_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.medilabo.risk_service.model.Patient;

@Service
public class PatientService {

    private final RestClient restClient;

    public PatientService(
            @Value("${patient.service.url:http://localhost:8081}") String patientServiceUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(patientServiceUrl)
                .build();
    }

    public Patient getPatientById(Long patientId) {

        // Get patient information from patient-service.
        return restClient.get()
                .uri("/patients/{id}", patientId)
                .retrieve()
                .body(Patient.class);
    }
}