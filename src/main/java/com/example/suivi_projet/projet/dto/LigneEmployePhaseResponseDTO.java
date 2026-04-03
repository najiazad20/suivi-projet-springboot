package com.example.suivi_projet.projet.dto;

import java.util.Date;

public record LigneEmployePhaseResponseDTO(
        int employeId,
        int phaseId,
        Date dateDebut,
        Date dateFin
) {}