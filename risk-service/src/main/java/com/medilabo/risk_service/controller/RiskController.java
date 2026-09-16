package com.medilabo.risk_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.risk_service.model.Note;
import com.medilabo.risk_service.model.Patient;
import com.medilabo.risk_service.service.NoteService;
import com.medilabo.risk_service.service.PatientService;

@RestController
@RequestMapping("/risk")
public class RiskController {

    private final PatientService patientService;
    private final NoteService noteService;

    public RiskController(PatientService patientService, NoteService noteService) {
        this.patientService = patientService;
        this.noteService = noteService;
    }

    @GetMapping("/patient/{id}")
    public Patient getPatient(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @GetMapping("/notes/{id}")
    public List<Note> getNotes(@PathVariable Long id) {
        return noteService.getNotesByPatientId(id);
    }
}