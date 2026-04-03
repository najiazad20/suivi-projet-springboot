package com.example.suivi_projet.projet.dto;

public record DocumentResponseDTO(

        int id,
        String code,
        String libelle,
        String description,
        String chemin,
        int projetId

) {
}