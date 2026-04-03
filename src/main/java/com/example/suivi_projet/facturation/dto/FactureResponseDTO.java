package com.example.suivi_projet.facturation.dto;

import java.util.Date;

public record FactureResponseDTO(

        Integer id,
        String code,
        Date dateFacture,
        Integer phaseId,
        double montantPhase

) {
}
