package com.example.suivi_projet.projet.dto;

import java.util.Date;

public record PhaseResponseDTO(

        int id,
        String code,
        String libelle,
        String description,
        Date dateDebut,
        Date dateFin,
        double montant,
        boolean etatRealisation,
        boolean etatFacturation,
        boolean etatPaiement,
        int projetId

) {
}