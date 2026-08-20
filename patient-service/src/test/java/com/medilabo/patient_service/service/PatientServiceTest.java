package com.medilabo.patient_service.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.medilabo.patient_service.model.Patient;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Test
    void shouldSavePatient() {
        Patient patient = new Patient(
                "Test",
                "TestBorderline",
                LocalDate.of(1945, 6, 24),
                "M",
                "2 High St",
                "200-333-4444");

        Patient savedPatient = patientService.savePatient(patient);

        assertThat(savedPatient.getId()).isNotNull();
        assertThat(savedPatient.getFirstName()).isEqualTo("Test");
        assertThat(savedPatient.getLastName()).isEqualTo("TestBorderline");
    }

    @Test
void shouldGetAllPatients() {
    Patient patient = new Patient(
            "Test",
            "TestNone",
            LocalDate.of(1966, 12, 31),
            "F",
            "1 Brookside St",
            "100-222-3333");

    patientService.savePatient(patient);

    List<Patient> patients = patientService.getAllPatients();

    assertThat(patients).isNotEmpty();
}

@Test
void shouldGetPatientById() {
    Patient patient = new Patient(
            "Test",
            "TestInDanger",
            LocalDate.of(2004, 6, 18),
            "M",
            "3 Club Road",
            "300-444-5555");

    Patient savedPatient = patientService.savePatient(patient);

    Optional<Patient> foundPatient = patientService.getPatientById(savedPatient.getId());

    assertThat(foundPatient).isPresent();
    assertThat(foundPatient.get().getLastName()).isEqualTo("TestInDanger");
}

@Test
void shouldUpdatePatient() {
    Patient patient = new Patient(
            "Test",
            "TestEarlyOnset",
            LocalDate.of(2002, 6, 28),
            "F",
            "4 Valley Dr",
            "400-555-6666");

    Patient savedPatient = patientService.savePatient(patient);

    savedPatient.setAddress("5 New Valley Dr");
    savedPatient.setPhone("500-666-7777");

    Patient updatedPatient = patientService.updatePatient(savedPatient);

    assertThat(updatedPatient.getAddress()).isEqualTo("5 New Valley Dr");
    assertThat(updatedPatient.getPhone()).isEqualTo("500-666-7777");
}
}