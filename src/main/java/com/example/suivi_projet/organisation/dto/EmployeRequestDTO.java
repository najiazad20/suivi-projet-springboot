package com.example.suivi_projet.organisation.dto;

import jakarta.validation.constraints.*;

public record EmployeRequestDTO(

        @NotBlank
        String matricule,

        @NotBlank
        String nom,

        @NotBlank
        String prenom,

        @NotBlank
        String telephone,

        @Email
        @NotBlank
        String email,

        @NotBlank
        String login,

        @NotBlank
        String password,

        int profilId
) {}