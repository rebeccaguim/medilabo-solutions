package com.medilabo.notes_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.medilabo.notes_service.model.Note;
import com.medilabo.notes_service.repository.NoteRepository;

/** Stores and retrieves patient notes. */
@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /** Finds all notes linked to a patient.
     * @param patId patient ID
     * @return patient's notes
     */
    public List<Note> getNotesByPatientId(Long patId) {
        return noteRepository.findByPatId(patId);
    }

    /** Saves a note.
     * @param note note to save
     * @return saved note
     */
    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }
}