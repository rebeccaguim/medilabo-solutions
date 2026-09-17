package com.medilabo.risk_service.service;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.medilabo.risk_service.model.Note;
import com.medilabo.risk_service.model.Patient;
import com.medilabo.risk_service.model.RiskLevel;

class RiskServiceTest {

    private final PatientService patientService = mock(PatientService.class);
    private final NoteService noteService = mock(NoteService.class);

    private final RiskService riskService =
            new RiskService(patientService, noteService);

    @Test
    void shouldCountTriggerTermsInPatientNotes() {

        Note firstNote = new Note();
        firstNote.setNote(
                "Le patient est Fumeur et son Cholestérol est élevé"
        );

        Note secondNote = new Note();
        secondNote.setNote(
                "Le patient présente des Vertiges"
        );

        List<Note> notes = List.of(firstNote, secondNote);

        int result = riskService.countTriggerTerms(notes);

        assertEquals(3, result);
    }

    @Test
    void shouldReturnZeroWhenNotesContainNoTriggerTerms() {

        Note note = new Note();
        note.setNote(
                "Le patient déclare qu'il se sent très bien"
        );

        List<Note> notes = List.of(note);

        int result = riskService.countTriggerTerms(notes);

        assertEquals(0, result);
    }

    @Test
    void shouldIgnoreCaseWhenCountingTriggerTerms() {

        Note note = new Note();
        note.setNote(
                "Le patient est FUMEUR et son cholestérol est élevé"
        );

        List<Note> notes = List.of(note);

        int result = riskService.countTriggerTerms(notes);

        assertEquals(2, result);
    }

    @Test
    void shouldCalculatePatientAge() {

        Patient patient = new Patient();
        patient.setBirthDate(LocalDate.now().minusYears(25));

        int result = riskService.calculateAge(patient);

        assertEquals(25, result);
    }

    @Test
    void shouldReturnNoneForPatientOver30WithOneTrigger() {

        RiskLevel result = riskService.determineRiskForPatientOver30(1);

        assertEquals(RiskLevel.NONE, result);
    }

    @Test
    void shouldReturnBorderlineForPatientOver30WithTwoTriggers() {

        RiskLevel result = riskService.determineRiskForPatientOver30(2);

        assertEquals(RiskLevel.BORDERLINE, result);
    }

    @Test
    void shouldReturnInDangerForPatientOver30WithSixTriggers() {

        RiskLevel result = riskService.determineRiskForPatientOver30(6);

        assertEquals(RiskLevel.IN_DANGER, result);
    }

    @Test
    void shouldReturnEarlyOnsetForPatientOver30WithEightTriggers() {

        RiskLevel result = riskService.determineRiskForPatientOver30(8);

        assertEquals(RiskLevel.EARLY_ONSET, result);
    }

    @Test
    void shouldReturnNoneForMaleUnder30WithTwoTriggers() {

        RiskLevel result = riskService.determineRiskForMaleUnder30(2);

        assertEquals(RiskLevel.NONE, result);
    }

    @Test
    void shouldReturnInDangerForMaleUnder30WithThreeTriggers() {

        RiskLevel result = riskService.determineRiskForMaleUnder30(3);

        assertEquals(RiskLevel.IN_DANGER, result);
    }

    @Test
    void shouldReturnEarlyOnsetForMaleUnder30WithFiveTriggers() {

        RiskLevel result = riskService.determineRiskForMaleUnder30(5);

        assertEquals(RiskLevel.EARLY_ONSET, result);
    }

    @Test
    void shouldReturnNoneForFemaleUnder30WithThreeTriggers() {

        RiskLevel result = riskService.determineRiskForFemaleUnder30(3);

        assertEquals(RiskLevel.NONE, result);
    }

    @Test
    void shouldReturnInDangerForFemaleUnder30WithFourTriggers() {

        RiskLevel result = riskService.determineRiskForFemaleUnder30(4);

        assertEquals(RiskLevel.IN_DANGER, result);
    }

    @Test
    void shouldReturnEarlyOnsetForFemaleUnder30WithSevenTriggers() {

        RiskLevel result = riskService.determineRiskForFemaleUnder30(7);

        assertEquals(RiskLevel.EARLY_ONSET, result);
    }

    @Test
    void shouldDetermineInDangerRiskForMaleUnder30() {

        Patient patient = new Patient();
        patient.setBirthDate(LocalDate.now().minusYears(25));
        patient.setGender("M");

        Note note = new Note();
        note.setNote(
                "Le patient est Fumeur avec un Cholestérol anormal"
        );

        List<Note> notes = List.of(note);

        RiskLevel result = riskService.determineRisk(patient, notes);

        assertEquals(RiskLevel.IN_DANGER, result);
    }

    @Test
    void shouldReturnNoneForOfficialTestNonePatient() {

        Patient patient = createPatient(
                LocalDate.of(1966, 12, 31),
                "F"
        );

        List<Note> notes = List.of(
                createNote(
                        "Le patient déclare qu'il 'se sent très bien' "
                        + "Poids égal ou inférieur au poids recommandé"
                )
        );

        RiskLevel result = riskService.determineRisk(patient, notes);

        assertEquals(RiskLevel.NONE, result);
    }

    @Test
    void shouldReturnBorderlineForOfficialTestBorderlinePatient() {

        Patient patient = createPatient(
                LocalDate.of(1945, 6, 24),
                "M"
        );

        List<Note> notes = List.of(
                createNote(
                        "Le patient déclare qu'il ressent beaucoup de stress au travail "
                        + "Il se plaint également que son audition est anormale dernièrement"
                ),
                createNote(
                        "Le patient déclare avoir fait une réaction aux médicaments "
                        + "au cours des 3 derniers mois "
                        + "Il remarque également que son audition continue d'être anormale"
                )
        );

        RiskLevel result = riskService.determineRisk(patient, notes);

        assertEquals(RiskLevel.BORDERLINE, result);
    }

    @Test
    void shouldReturnInDangerForOfficialTestInDangerPatient() {

        Patient patient = createPatient(
                LocalDate.of(2004, 6, 18),
                "M"
        );

        List<Note> notes = List.of(
                createNote(
                        "Le patient déclare qu'il fume depuis peu"
                ),
                createNote(
                        "Le patient déclare qu'il est fumeur et qu'il a cessé "
                        + "de fumer l'année dernière "
                        + "Il se plaint également de crises d’apnée respiratoire anormales "
                        + "Tests de laboratoire indiquant un taux de cholestérol LDL élevé"
                )
        );

        RiskLevel result = riskService.determineRisk(patient, notes);

        assertEquals(RiskLevel.IN_DANGER, result);
    }

    @Test
    void shouldReturnEarlyOnsetForOfficialTestEarlyOnsetPatient() {

        Patient patient = createPatient(
                LocalDate.of(2002, 6, 28),
                "F"
        );

        List<Note> notes = List.of(
                createNote(
                        "Le patient déclare qu'il lui est devenu difficile de monter les escaliers "
                        + "Il se plaint également d’être essoufflé "
                        + "Tests de laboratoire indiquant que les anticorps sont élevés "
                        + "Réaction aux médicaments"
                ),
                createNote(
                        "Le patient déclare qu'il a mal au dos lorsqu'il reste assis "
                        + "pendant longtemps"
                ),
                createNote(
                        "Le patient déclare avoir commencé à fumer depuis peu "
                        + "Hémoglobine A1C supérieure au niveau recommandé"
                ),
                createNote(
                        "Taille, Poids, Cholestérol, Vertige et Réaction"
                )
        );

        RiskLevel result = riskService.determineRisk(patient, notes);

        assertEquals(RiskLevel.EARLY_ONSET, result);
    }

    @Test
    void shouldAssessRiskUsingPatientAndNotesServices() {

        Patient patient = createPatient(
                LocalDate.now().minusYears(25),
                "M"
        );

        List<Note> notes = List.of(
                createNote(
                        "Le patient est Fumeur avec un Cholestérol anormal"
                )
        );

        when(patientService.getPatientById(3L))
                .thenReturn(patient);

        when(noteService.getNotesByPatientId(3L))
                .thenReturn(notes);

        RiskLevel result = riskService.assessRisk(3L);

        assertEquals(RiskLevel.IN_DANGER, result);
    }

    private Patient createPatient(LocalDate birthDate, String gender) {

        Patient patient = new Patient();
        patient.setBirthDate(birthDate);
        patient.setGender(gender);

        return patient;
    }

    private Note createNote(String text) {

        Note note = new Note();
        note.setNote(text);

        return note;
    }
}