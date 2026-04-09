package com.example.suivi_projet.facturation.dto;

import java.time.LocalDate;
import java.util.Date;

public record FactureResponseDTO(

        Integer id,
        String code,
        LocalDate dateFacture,
        Integer phaseId,
        double montantPhase

) {
}
