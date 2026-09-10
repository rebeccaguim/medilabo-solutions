package com.medilabo.notes_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.notes_service.model.Note;
import com.medilabo.notes_service.service.NoteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/patient/{patId}")
    public List<Note> getNotesByPatientId(@PathVariable Long patId) {
        return noteService.getNotesByPatientId(patId);
    }

    @PostMapping
    public Note createNote(@Valid @RequestBody Note note) {
        return noteService.saveNote(note);
    }
}