package com.medilabo.patient_service.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.medilabo.patient_service.model.Patient;
import com.medilabo.patient_service.service.PatientService;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

    @Test
    void shouldGetAllPatients() throws Exception {
        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetPatientById() throws Exception {
        Patient patient = new Patient(
                "Test",
                "TestInDanger",
                LocalDate.of(2004, 6, 18),
                "M",
                "3 Club Road",
                "300-444-5555");

        when(patientService.getPatientById(1L))
                .thenReturn(Optional.of(patient));

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("TestInDanger"));
    }

    @Test
    void shouldCreatePatient() throws Exception {
        Patient savedPatient = new Patient(
                "Test",
                "TestNone",
                LocalDate.of(1966, 12, 31),
                "F",
                "1 Brookside St",
                "100-222-3333");

        when(patientService.savePatient(any(Patient.class)))
                .thenReturn(savedPatient);

        String patientJson = """
                {
                  "firstName": "Test",
                  "lastName": "TestNone",
                  "birthDate": "1966-12-31",
                  "gender": "F",
                  "address": "1 Brookside St",
                  "phone": "100-222-3333"
                }
                """;

        mockMvc.perform(post("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(patientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("TestNone"));
    }
    @Test
void shouldUpdatePatient() throws Exception {
    Patient updatedPatient = new Patient(
            "Test",
            "TestNone",
            LocalDate.of(1966, 12, 31),
            "F",
            "10 New Brookside St",
            "999-888-7777");

    when(patientService.updatePatient(any(Patient.class)))
            .thenReturn(updatedPatient);

    String patientJson = """
            {
              "firstName": "Test",
              "lastName": "TestNone",
              "birthDate": "1966-12-31",
              "gender": "F",
              "address": "10 New Brookside St",
              "phone": "999-888-7777"
            }
            """;

    mockMvc.perform(put("/patients/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(patientJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.address").value("10 New Brookside St"))
            .andExpect(jsonPath("$.phone").value("999-888-7777"));
}

@Test
void shouldReturnNotFoundWhenPatientDoesNotExist() throws Exception {
    // Simulate a patient that does not exist in the database
    when(patientService.getPatientById(999999L))
            .thenReturn(Optional.empty());

    mockMvc.perform(get("/patients/999999"))
            .andExpect(status().isNotFound());
}
}