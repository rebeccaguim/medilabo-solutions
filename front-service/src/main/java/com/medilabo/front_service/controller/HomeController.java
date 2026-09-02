package com.medilabo.front_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.medilabo.front_service.model.Patient;
import com.medilabo.front_service.service.PatientService;

@Controller
public class HomeController {

    private final PatientService patientService;

    public HomeController(PatientService patientService) {
        this.patientService = patientService;
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
}