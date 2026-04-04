package com.example.suivi_projet.reporting.dto;

import java.util.Date;

public record ProjetReportingDTO(
        int id,
        String code,
        String nom,
        Date dateDebut,
        Date dateFin,
        double montant
) {}