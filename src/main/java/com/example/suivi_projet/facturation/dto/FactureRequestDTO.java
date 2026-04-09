package com.example.suivi_projet.facturation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;

public record FactureRequestDTO(

        @NotBlank(message = "Le code est obligatoire")
        String code,

        @NotNull(message = "La date est obligatoire")
        LocalDate dateFacture

) {
}