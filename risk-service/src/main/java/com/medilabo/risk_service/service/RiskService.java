package com.medilabo.risk_service.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.stereotype.Service;

import com.medilabo.risk_service.model.Note;
import com.medilabo.risk_service.model.Patient;
import com.medilabo.risk_service.model.RiskLevel;

@Service
public class RiskService {

    // These terms are used to find possible diabetes symptoms in patient notes.
    private static final List<String> TRIGGER_TERMS = List.of(
            "Hémoglobine A1C",
            "Microalbumine",
            "Taille",
            "Poids",
            "Fumeur",
            "Fumeuse",
            "Anormal",
            "Cholestérol",
            "Vertiges",
            "Rechute",
            "Réaction",
            "Anticorps"
    );

    private final PatientService patientService;
    private final NoteService noteService;

    public RiskService(PatientService patientService, NoteService noteService) {
        this.patientService = patientService;
        this.noteService = noteService;
    }

    public int countTriggerTerms(List<Note> notes) {

        int triggerCount = 0;

        // Count each trigger term found in each note.
        for (Note note : notes) {

            String noteText = note.getNote();

            for (String triggerTerm : TRIGGER_TERMS) {

                if (noteText.toLowerCase().contains(triggerTerm.toLowerCase())) {
                    triggerCount++;
                }
            }
        }

        return triggerCount;
    }

    public int calculateAge(Patient patient) {

        LocalDate birthDate = patient.getBirthDate();
        LocalDate currentDate = LocalDate.now();

        // Calculate the patient's age in complete years.
        return Period.between(birthDate, currentDate).getYears();
    }

    public RiskLevel determineRiskForPatientOver30(int triggerCount) {

        // For age 30+, 2-5 terms is borderline, 6-7 is danger, and 8+ is early onset.
        if (triggerCount >= 8) {
            return RiskLevel.EARLY_ONSET;
        }

        if (triggerCount >= 6) {
            return RiskLevel.IN_DANGER;
        }

        if (triggerCount >= 2) {
            return RiskLevel.BORDERLINE;
        }

        return RiskLevel.NONE;
    }

    public RiskLevel determineRiskForMaleUnder30(int triggerCount) {

        // For males under 30, 3-4 terms is danger and 5+ is early onset.
        if (triggerCount >= 5) {
            return RiskLevel.EARLY_ONSET;
        }

        if (triggerCount >= 3) {
            return RiskLevel.IN_DANGER;
        }

        return RiskLevel.NONE;
    }

    public RiskLevel determineRiskForFemaleUnder30(int triggerCount) {

        // For females under 30, 4-6 terms is danger and 7+ is early onset.
        if (triggerCount >= 7) {
            return RiskLevel.EARLY_ONSET;
        }

        if (triggerCount >= 4) {
            return RiskLevel.IN_DANGER;
        }

        return RiskLevel.NONE;
    }

    public RiskLevel determineRisk(Patient patient, List<Note> notes) {

        int age = calculateAge(patient);
        int triggerCount = countTriggerTerms(notes);

        // Choose the thresholds based on age and gender.
        if (age < 30) {

            if ("M".equalsIgnoreCase(patient.getGender())) {
                return determineRiskForMaleUnder30(triggerCount);
            }

            if ("F".equalsIgnoreCase(patient.getGender())) {
                return determineRiskForFemaleUnder30(triggerCount);
            }

            return RiskLevel.NONE;
        }

        return determineRiskForPatientOver30(triggerCount);
    }

    public RiskLevel assessRisk(Long patientId) {

        // Load the patient's data before calculating the final risk level.
        Patient patient = patientService.getPatientById(patientId);
        List<Note> notes = noteService.getNotesByPatientId(patientId);

        return determineRisk(patient, notes);
    }
}