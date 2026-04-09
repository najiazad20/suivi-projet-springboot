package com.example.suivi_projet.projet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public record LigneEmployePhaseResponseDTO(
        int employeId,
        int phaseId,

        @Schema(type = "string", example = "2026-04-09")
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date dateDebut,


        @Schema(type = "string", example = "2026-04-09")
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date dateFin
) {}