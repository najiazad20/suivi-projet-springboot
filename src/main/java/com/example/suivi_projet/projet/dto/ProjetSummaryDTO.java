package com.example.suivi_projet.projet.dto;

public record ProjetSummaryDTO(
        String nomProjet,
        String chefProjet,
        double montantGlobal,
        long nombrePhases,
        double montantFacture,
        double progressionPercentage // (Phases terminées / Total) * 100
) {}
