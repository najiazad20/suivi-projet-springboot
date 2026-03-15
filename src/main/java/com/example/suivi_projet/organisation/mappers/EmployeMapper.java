package com.example.suivi_projet.organisation.mappers;

import com.example.suivi_projet.organisation.dto.EmployeCreateDTO;
import com.example.suivi_projet.organisation.dto.EmployeResponseDTO;
import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.organisation.entities.Profil;

public class EmployeMapper {

    public static EmployeResponseDTO toDTO(Employe employe) {
        EmployeResponseDTO dto = new EmployeResponseDTO();

        dto.setId(employe.getId());
        dto.setMatricule(employe.getMatricule());
        dto.setNom(employe.getNom());
        dto.setPrenom(employe.getPrenom());
        dto.setTelephone(employe.getTelephone());
        dto.setEmail(employe.getEmail());
        dto.setLogin(employe.getLogin());
        dto.setPassword(employe.getPassword());


        Profil profil = employe.getProfil();
        dto.setProfilNom(profil != null ? (profil.getLibelle() != null ? profil.getLibelle() : "") : "");

        return dto;
    }

    public static Employe toEntity(EmployeCreateDTO dto, Profil profil) {
        Employe employe = new Employe();

        employe.setMatricule(dto.getMatricule());
        employe.setNom(dto.getNom());
        employe.setPrenom(dto.getPrenom());
        employe.setTelephone(dto.getTelephone());
        employe.setEmail(dto.getEmail());
        employe.setLogin(dto.getLogin());
        employe.setPassword(dto.getPassword());
        employe.setProfil(profil);

        return employe;
    }

    public static void updateEntity(Employe employe, EmployeCreateDTO dto, Profil profil) {
        employe.setMatricule(dto.getMatricule());
        employe.setNom(dto.getNom());
        employe.setPrenom(dto.getPrenom());
        employe.setTelephone(dto.getTelephone());
        employe.setEmail(dto.getEmail());
        employe.setLogin(dto.getLogin());
        employe.setPassword(dto.getPassword());
        employe.setProfil(profil);
    }
}