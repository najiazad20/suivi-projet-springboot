package com.example.suivi_projet.projet.dto;

import java.util.Date;

public record ProjetResponseDTO(

        int id,
        String code,
        String nom,
        String description,
        Date dateDebut,
        Date dateFin,
        double montant,
        String organismeId,
        String chefProjetNom

) {
}