package com.medilabo.patient_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.medilabo.patient_service.model.Patient;
import com.medilabo.patient_service.repository.PatientRepository;


/** Stores and retrieves patient records. */
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /** Saves a patient record.
     * @param patient patient to save
     * @return saved patient
     */
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    /** Returns all patient records.
     * @return list of patients
     */
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /** Finds a patient by ID.
     * @param id patient ID
     * @return matching patient, if found
     */
    public Optional<Patient> getPatientById(Long id) {
    return patientRepository.findById(id);
}

/** Saves the updated patient record.
 * @param patient patient to update
 * @return updated patient
 */
public Patient updatePatient(Patient patient) {
    return patientRepository.save(patient);
}
}