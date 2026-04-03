package com.example.suivi_projet.organisation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OrganismeRequestDTO(

        @NotBlank(message = "Le code est obligatoire")
        String code,

        @NotBlank(message = "Le nom est obligatoire")
        String nom,

        String adresse,
        String telephone,

        @NotBlank(message = "Le contact est obligatoire")
        String nomContact,

        @Email(message = "Email invalide")
        String emailContact,

        String siteWeb
) {
}