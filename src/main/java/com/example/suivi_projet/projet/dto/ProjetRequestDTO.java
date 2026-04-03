package com.example.suivi_projet.projet.dto;

import jakarta.validation.constraints.*;
import java.util.Date;

public record ProjetRequestDTO(

        @NotBlank(message = "Le code est obligatoire")
        String code,

        @NotBlank(message = "Le nom est obligatoire")
        String nom,

        String description,

        @NotNull(message = "La date début est obligatoire")
        Date dateDebut,

        @NotNull(message = "La date fin est obligatoire")
        Date dateFin,

        @Positive(message = "Le montant doit être positif")
        double montant,

        @NotNull(message = "Organisme obligatoire")
        Integer organismeId,

        @NotNull(message = "Chef projet obligatoire")
        Integer chefProjetId

) {
}