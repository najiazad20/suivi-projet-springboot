package com.example.suivi_projet.reporting.controllers;

import com.example.suivi_projet.reporting.dto.*;
import com.example.suivi_projet.reporting.services.ReportingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@PreAuthorize("hasAnyRole('DIRECTEUR', 'CHEF_PROJET', 'COMPTABLE')")
@RestController
@RequestMapping("/api/reporting")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/phases/terminees-non-facturees")
    public List<PhaseReportingDTO> getPhasesTermineesNonFacturees() {
        return reportingService.getPhasesTermineesNonFacturees();
    }

    @GetMapping("/phases/facturees-non-payees")
    public List<PhaseReportingDTO> getPhasesFactureesNonPayees() {
        return reportingService.getPhasesFactureesNonPayees();
    }
    @GetMapping("/phases/payees")
    public List<PhaseReportingDTO> getPhasesPayees() {
        return reportingService.getPhasesPayees();
    }

    @GetMapping("/projets/en-cours")
    public List<ProjetReportingDTO> getProjetsEnCours() {
        return reportingService.getProjetsEnCours();
    }

    @GetMapping("/projets/clotures")
    public List<ProjetReportingDTO> getProjetsClotures() {
        return reportingService.getProjetsClotures();
    }

    @GetMapping("/tableau-de-bord")
    public String getDashboard() {
        return reportingService.getDashboard();
    }
}