package com.medilabo.risk_service.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.medilabo.risk_service.model.Note;

@Service
public class NoteService {

    private final RestClient restClient;

    public NoteService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
    }

    public List<Note> getNotesByPatientId(Long patientId) {

        Note[] notes = restClient.get()
                .uri("/notes/patient/{patId}", patientId)
                .retrieve()
                .body(Note[].class);

        return Arrays.asList(notes);
    }
}