package com.medilabo.front_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.medilabo.front_service.model.Note;
import com.medilabo.front_service.model.Patient;
import com.medilabo.front_service.service.NoteService;
import com.medilabo.front_service.service.PatientService;

@Controller
public class HomeController {

    private final PatientService patientService;
    private final NoteService noteService;

    public HomeController(PatientService patientService, NoteService noteService) {
        this.patientService = patientService;
        this.noteService = noteService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        return "home";
    }

    @GetMapping("/patients/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient-form";
    }

    @PostMapping("/patients")
    public String createPatient(@ModelAttribute Patient patient) {
        patientService.createPatient(patient);
        return "redirect:/";
    }

    @GetMapping("/patients/edit/{id}")
    public String showEditPatientForm(@PathVariable Long id, Model model) {

        Patient patient = patientService.getPatientById(id);

        model.addAttribute("patient", patient);
        model.addAttribute("notes", noteService.getNotesByPatientId(id));

        return "patient-form";
    }

    @PostMapping("/patients/{id}")
    public String updatePatient(@PathVariable Long id, @ModelAttribute Patient patient) {
        patientService.updatePatient(id, patient);
        return "redirect:/";
    }

    @PostMapping("/patients/{id}/notes")
    public String addNote(@PathVariable Long id, @RequestParam String noteText) {

        Patient patient = patientService.getPatientById(id);

        Note note = new Note(
                id,
                patient.getLastName(),
                noteText
        );

        noteService.createNote(note);

        return "redirect:/patients/edit/" + id;
    }
}