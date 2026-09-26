package com.medilabo.risk_service.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.medilabo.risk_service.model.Note;

/** Gets patient notes from notes-service. */
@Service
public class NoteService {

    private final RestClient restClient;

    public NoteService(
            @Value("${note.service.url:http://localhost:8083}") String noteServiceUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(noteServiceUrl)
                .build();
    }

    /** Finds all notes for a patient.
     * @param patientId ID of the patient
     * @return patient's notes
     */
    public List<Note> getNotesByPatientId(Long patientId) {

        // Get the patient's notes from notes-service.
        Note[] notes = restClient.get()
                .uri("/notes/patient/{patId}", patientId)
                .retrieve()
                .body(Note[].class);

        return Arrays.asList(notes);
    }
}