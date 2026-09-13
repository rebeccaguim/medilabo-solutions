package com.medilabo.front_service.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.medilabo.front_service.model.Note;
import com.medilabo.front_service.security.JwtService;

@Service
public class NoteService {

    private final RestClient restClient;
    private final JwtService jwtService;

    public NoteService(JwtService jwtService) {
        this.jwtService = jwtService;

        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public List<Note> getNotesByPatientId(Long patId) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        String token = jwtService.generateToken(username);

        Note[] notes = restClient.get()
                .uri("/notes/patient/{patId}", patId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(Note[].class);

        return Arrays.asList(notes);
    }

    public Note createNote(Note note) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        String token = jwtService.generateToken(username);

        return restClient.post()
                .uri("/notes")
                .header("Authorization", "Bearer " + token)
                .body(note)
                .retrieve()
                .body(Note.class);
    }
}