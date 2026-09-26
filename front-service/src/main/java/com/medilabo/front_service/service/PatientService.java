package com.medilabo.front_service.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.medilabo.front_service.model.Patient;
import com.medilabo.front_service.security.JwtService;

/** Sends patient requests to patient-service through the gateway. */
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

        /** Gets all patients.
         * @return list of patients
         */
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

        /** Creates a patient.
         * @param patient patient details
         * @return saved patient
         */
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

        /** Gets a patient by ID.
         * @param id patient ID
         * @return patient details
         */
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

        /** Updates a patient.
         * @param id patient ID
         * @param patient updated patient details
         * @return updated patient
         */
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