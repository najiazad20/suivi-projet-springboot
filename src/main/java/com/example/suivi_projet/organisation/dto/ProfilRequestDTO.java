package com.example.suivi_projet.organisation.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfilRequestDTO(

        @NotBlank(message = "Le code est obligatoire")
        String code,

        @NotBlank(message = "Le libellé est obligatoire")
        String libelle

) {
}