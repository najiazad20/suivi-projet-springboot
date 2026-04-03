package com.example.suivi_projet.organisation.dto;

public record OrganismeResponseDTO(

        Integer id,
        String code,
        String nom,
        String adresse,
        String telephone,
        String nomContact,
        String emailContact,
        String siteWeb

) {
}