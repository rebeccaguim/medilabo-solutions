package com.medilabo.notes_service.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.medilabo.notes_service.model.Note;
import com.medilabo.notes_service.repository.NoteRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final NoteRepository noteRepository;

    public DataInitializer(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public void run(String... args) {

        // Insert the test data only when the collection is empty
        if (noteRepository.count() == 0) {

            List<Note> notes = List.of(

                    new Note(
                            1L,
                            "TestNone",
                            "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé"
                    ),

                    new Note(
                            2L,
                            "TestBorderline",
                            "Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement"
                    ),

                    new Note(
                            2L,
                            "TestBorderline",
                            "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale"
                    ),

                    new Note(
                            3L,
                            "TestInDanger",
                            "Le patient déclare qu'il fume depuis peu"
                    ),

                    new Note(
                            3L,
                            "TestInDanger",
                            "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé"
                    ),

                    new Note(
                            4L,
                            "TestEarlyOnset",
                            "Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments"
                    ),

                    new Note(
                            4L,
                            "TestEarlyOnset",
                            "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps"
                    ),

                    new Note(
                            4L,
                            "TestEarlyOnset",
                            "Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé"
                    ),

                    new Note(
                            4L,
                            "TestEarlyOnset",
                            "Taille, Poids, Cholestérol, Vertige et Réaction"
                    )
            );

            noteRepository.saveAll(notes);
        }
    }
}