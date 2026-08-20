package com.medilabo.patient_service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.medilabo.patient_service.model.Patient;

@SpringBootTest
@Transactional
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void shouldSavePatient() {
        Patient patient = new Patient(
                "Test",
                "TestNone",
                LocalDate.of(1966, 12, 31),
                "F",
                "1 Brookside St",
                "100-222-3333");

        Patient savedPatient = patientRepository.save(patient);

        assertThat(savedPatient.getId()).isNotNull();
        assertThat(savedPatient.getFirstName()).isEqualTo("Test");
        assertThat(savedPatient.getLastName()).isEqualTo("TestNone");
    }
}