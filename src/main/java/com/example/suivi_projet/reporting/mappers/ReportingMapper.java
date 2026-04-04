package com.example.suivi_projet.reporting.mappers;

import com.example.suivi_projet.projet.entities.Phase;
import com.example.suivi_projet.projet.entities.Projet;
import com.example.suivi_projet.reporting.dto.*;
import org.springframework.stereotype.Component;

@Component
public class ReportingMapper {

    public PhaseReportingDTO toPhaseDTO(Phase phase) {
        return new PhaseReportingDTO(
                phase.getId(),
                phase.getCode(),
                phase.getLibelle(),
                phase.getDateDebut(),
                phase.getDateFin(),
                phase.getMontant()
        );
    }

    public ProjetReportingDTO toProjetDTO(Projet projet) {
        return new ProjetReportingDTO(
                projet.getId(),
                projet.getCode(),
                projet.getNom(),
                projet.getDateDebut(),
                projet.getDateFin(),
                projet.getMontant()
        );
    }
}