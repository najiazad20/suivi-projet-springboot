package com.example.suivi_projet.projet.dto;

import jakarta.validation.constraints.*;
import java.util.Date;

public record PhaseRequestDTO(

        @NotBlank String code,
        @NotBlank String libelle,
        String description,

        @NotNull Date dateDebut,
        @NotNull Date dateFin,

        @Positive double montant,


        boolean etatRealisation,
        boolean etatFacturation,
        boolean etatPaiement,

        @NotNull(message = "Le projet est obligatoire")
        Integer projetId

) {
}