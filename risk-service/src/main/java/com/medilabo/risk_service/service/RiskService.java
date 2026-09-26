package com.medilabo.risk_service.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.stereotype.Service;

import com.medilabo.risk_service.model.Note;
import com.medilabo.risk_service.model.Patient;
import com.medilabo.risk_service.model.RiskLevel;

/** Calculates a patient's diabetes risk from notes, age, and gender. */
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

    /** Counts the trigger terms found in the patient's notes.
     * @param notes notes to check
     * @return number of matching terms
     */
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

    /** Calculates the patient's age in years.
     * @param patient patient whose age is calculated
     * @return age in complete years
     */
    public int calculateAge(Patient patient) {

        LocalDate birthDate = patient.getBirthDate();
        LocalDate currentDate = LocalDate.now();

        // Calculate the patient's age in complete years.
        return Period.between(birthDate, currentDate).getYears();
    }

    /** Applies the risk thresholds for patients aged 30 or older.
     * @param triggerCount number of matching terms
     * @return assessed risk level
     */
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

    /** Applies the risk thresholds for males under 30.
     * @param triggerCount number of matching terms
     * @return assessed risk level
     */
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

    /** Applies the risk thresholds for females under 30.
     * @param triggerCount number of matching terms
     * @return assessed risk level
     */
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

    /** Selects a risk level using the patient's age, gender, and notes.
     * @param patient patient being assessed
     * @param notes patient's notes
     * @return assessed risk level
     */
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

    /** Loads patient data and calculates the final risk level.
     * @param patientId ID of the patient to assess
     * @return assessed risk level
     */
    public RiskLevel assessRisk(Long patientId) {

        // Load the patient's data before calculating the final risk level.
        Patient patient = patientService.getPatientById(patientId);
        List<Note> notes = noteService.getNotesByPatientId(patientId);

        return determineRisk(patient, notes);
    }
}