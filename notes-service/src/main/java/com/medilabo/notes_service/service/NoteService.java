package com.medilabo.notes_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.medilabo.notes_service.model.Note;
import com.medilabo.notes_service.repository.NoteRepository;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getNotesByPatientId(Long patId) {
        return noteRepository.findByPatId(patId);
    }

    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }
}