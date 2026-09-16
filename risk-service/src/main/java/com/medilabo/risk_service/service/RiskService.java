package com.medilabo.risk_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.medilabo.risk_service.model.Note;

@Service
public class RiskService {

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

    public int countTriggerTerms(List<Note> notes) {

        int triggerCount = 0;

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
}