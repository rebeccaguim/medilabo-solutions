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
import com.medilabo.front_service.service.RiskService;

/** Shows patient pages and handles patient form actions. */
@Controller
public class HomeController {

    private final PatientService patientService;
    private final NoteService noteService;
    private final RiskService riskService;

    public HomeController(
            PatientService patientService,
            NoteService noteService,
            RiskService riskService) {

        this.patientService = patientService;
        this.noteService = noteService;
        this.riskService = riskService;
    }

    /** Shows the patient list.
     * @param model page data
     * @return name of the patient list page
     */
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
        // Save the submitted patient and return to the patient list.
        patientService.createPatient(patient);
        return "redirect:/";
    }

    /** Shows patient details, notes, and risk level.
     * @param id patient ID
     * @param model page data
     * @return name of the patient details page
     */
    @GetMapping("/patients/view/{id}")
    public String showPatient(@PathVariable Long id, Model model) {

        // Load the patient's details, notes, and risk level for the page.
        Patient patient = patientService.getPatientById(id);

        model.addAttribute("patient", patient);
        model.addAttribute("notes", noteService.getNotesByPatientId(id));
        model.addAttribute("riskLevel", riskService.getRiskByPatientId(id));

        return "patient-view";
    }

    @GetMapping("/patients/edit/{id}")
    public String showEditPatientForm(@PathVariable Long id, Model model) {

        Patient patient = patientService.getPatientById(id);

        model.addAttribute("patient", patient);

        return "patient-form";
    }

    @PostMapping("/patients/{id}")
    public String updatePatient(
            @PathVariable Long id,
            @ModelAttribute Patient patient) {

        // Save the edited patient details.
        patientService.updatePatient(id, patient);
        return "redirect:/";
    }

    /** Adds a note to a patient.
     * @param id patient ID
     * @param noteText note text
     * @return redirect to the patient details page
     */
    @PostMapping("/patients/{id}/notes")
    public String addNote(
            @PathVariable Long id,
            @RequestParam String noteText) {

        // Link the new note to the selected patient.
        Patient patient = patientService.getPatientById(id);

        Note note = new Note(
                id,
                patient.getLastName(),
                noteText
        );

        noteService.createNote(note);

        return "redirect:/patients/view/" + id;
    }
}