package com.example.suivi_projet.reporting.services;

import com.example.suivi_projet.projet.repositories.PhaseRepository;
import com.example.suivi_projet.projet.repositories.ProjetRepository;
import com.example.suivi_projet.reporting.dto.*;
import com.example.suivi_projet.reporting.mappers.ReportingMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReportingService {

    private final PhaseRepository phaseRepository;
    private final ProjetRepository projetRepository;
    private final ReportingMapper reportingMapper;

    public ReportingService(PhaseRepository phaseRepository,
                            ProjetRepository projetRepository,
                            ReportingMapper reportingMapper) {
        this.phaseRepository = phaseRepository;
        this.projetRepository = projetRepository;
        this.reportingMapper = reportingMapper;
    }

    public List<PhaseReportingDTO> getPhasesTermineesNonFacturees() {
        return phaseRepository.findByEtatRealisationTrueAndEtatFacturationFalse()
                .stream()
                .map(reportingMapper::toPhaseDTO)
                .toList();
    }

    public List<PhaseReportingDTO> getPhasesFactureesNonPayees() {
        return phaseRepository.findByEtatFacturationTrueAndEtatPaiementFalse()
                .stream()
                .map(reportingMapper::toPhaseDTO)
                .toList();
    }

    public List<PhaseReportingDTO> getPhasesPayees() {
        return phaseRepository.findByEtatPaiementTrue()
                .stream()
                .map(reportingMapper::toPhaseDTO)
                .toList();
    }

    public List<ProjetReportingDTO> getProjetsEnCours() {
        return projetRepository.findByDateFinAfter(new Date())
                .stream()
                .map(reportingMapper::toProjetDTO)
                .toList();
    }

    public List<ProjetReportingDTO> getProjetsClotures() {
        return projetRepository.findByDateFinBefore(new Date())
                .stream()
                .map(reportingMapper::toProjetDTO)
                .toList();
    }

    // Dashboard
    public String getDashboard() {
        long totalPhases = phaseRepository.count();
        long phasesFacturees = phaseRepository.findByEtatFacturationTrueAndEtatPaiementFalse().size();

        return "Total phases: " + totalPhases +
                " | Phases facturées non payées: " + phasesFacturees;
    }
}