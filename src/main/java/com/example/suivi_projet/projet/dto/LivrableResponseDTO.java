package com.example.suivi_projet.projet.dto;

public record LivrableResponseDTO(
        Integer id,
        String code,
        String libelle,
        String description,
        String chemin,
        Integer phaseId
) {
}