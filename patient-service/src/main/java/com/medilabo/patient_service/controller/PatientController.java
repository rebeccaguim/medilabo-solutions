package com.medilabo.patient_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.patient_service.model.Patient;
import com.medilabo.patient_service.service.PatientService;

import jakarta.validation.Valid;

/** Handles requests to read and update patient records. */
@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /** Returns all patient records.
     * @return list of patients
     */
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    /** Finds a patient by ID.
     * @param id patient ID
     * @return patient details, or a not-found response
     */
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        // Return 404 when the patient does not exist.
        Optional<Patient> patient = patientService.getPatientById(id);

        if (patient.isPresent()) {
            return ResponseEntity.ok(patient.get());
        }

        return ResponseEntity.notFound().build();
    }

    /** Validates and saves a patient.
     * @param patient patient details
     * @return saved patient
     */
    @PostMapping
    public Patient createPatient(@Valid @RequestBody Patient patient) {
        // Validate and save the new patient.
        return patientService.savePatient(patient);
    }

    /** Updates the patient with the given ID.
     * @param id patient ID
     * @param patient updated patient details
     * @return updated patient
     */
    @PutMapping("/{id}")
    public Patient updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody Patient patient) {

        // Keep the URL ID as the ID of the patient being updated.
        patient.setId(id);
        return patientService.updatePatient(patient);
    }
}