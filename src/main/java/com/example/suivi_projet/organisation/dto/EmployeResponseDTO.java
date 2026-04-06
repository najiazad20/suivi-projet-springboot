package com.example.suivi_projet.organisation.dto;

public record EmployeResponseDTO(
        int id,
        String matricule,
        String nom,
        String prenom,
        String telephone,
        String email,
        String login,
        String password,
        String profilId
) {}