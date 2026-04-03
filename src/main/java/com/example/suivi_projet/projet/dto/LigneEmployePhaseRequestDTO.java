package com.example.suivi_projet.projet.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Date;

public record LigneEmployePhaseRequestDTO(
        @NotNull Date dateDebut,
        @NotNull Date dateFin
) {}