package com.example.suivi_projet.projet.dto;

import jakarta.validation.constraints.*;
import java.util.Date;

public record PhaseRequestDTO(

        @NotBlank String code,
        @NotBlank String libelle,
        String description,

        @NotNull Date dateDebut,
        @NotNull Date dateFin,

        @Positive double montant

)
{
}