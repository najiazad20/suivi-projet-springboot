package com.example.suivi_projet.projet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivrableRequestDTO(

        @NotBlank(message = "Le code est obligatoire")
        String code,

        @NotBlank(message = "Le libellé est obligatoire")
        String libelle,

        String description,

        String chemin,

        @NotNull(message = "La phase est obligatoire")
        Integer phaseId
) {
}