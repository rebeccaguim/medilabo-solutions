package com.medilabo.risk_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.risk_service.model.RiskLevel;
import com.medilabo.risk_service.service.RiskService;

/** Provides the risk assessment endpoint. */
@RestController
@RequestMapping("/risk")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    /** Returns the risk level for a patient.
     * @param id patient ID
     * @return patient's risk level
     */
    @GetMapping("/{id}")
    public RiskLevel getRisk(@PathVariable Long id) {
        // Return the assessed risk level for this patient.
        return riskService.assessRisk(id);
    }
}