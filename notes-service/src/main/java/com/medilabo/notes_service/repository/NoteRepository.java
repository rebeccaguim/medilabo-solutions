package com.medilabo.notes_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.medilabo.notes_service.model.Note;

public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findByPatId(Long patId);
}