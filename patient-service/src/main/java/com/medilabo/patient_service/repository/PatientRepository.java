package com.medilabo.patient_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medilabo.patient_service.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}