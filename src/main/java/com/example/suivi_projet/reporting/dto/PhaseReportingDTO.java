package com.example.suivi_projet.reporting.dto;

import java.util.Date;

public record PhaseReportingDTO(
        int id,
        String code,
        String libelle,
        Date dateDebut,
        Date dateFin,
        double montant
) {}