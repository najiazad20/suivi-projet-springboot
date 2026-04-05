package com.example.suivi_projet.organisation.mappers;

import com.example.suivi_projet.organisation.dto.*;
import com.example.suivi_projet.organisation.entities.*;
import org.springframework.stereotype.Component;

@Component
public class EmployeMapper {

    public Employe toEntity(EmployeRequestDTO dto, Profil profil) {

        Employe e = new Employe();

        e.setMatricule(dto.matricule());
        e.setNom(dto.nom());
        e.setPrenom(dto.prenom());
        e.setTelephone(dto.telephone());
        e.setEmail(dto.email());
        e.setLogin(dto.login());
        e.setPassword(dto.password());
        e.setProfil(profil);

        return e;
    }

    public EmployeResponseDTO toDTO(Employe e) {

        return new EmployeResponseDTO(
                e.getId(),
                e.getMatricule(),
                e.getNom(),
                e.getPrenom(),
                e.getTelephone(),
                e.getEmail(),
                e.getLogin(),
                e.getProfil().getLibelle(),
                e.getPassword()
        );
    }

    public void update(EmployeRequestDTO dto, Employe e, Profil profil) {

        e.setMatricule(dto.matricule());
        e.setNom(dto.nom());
        e.setPrenom(dto.prenom());
        e.setTelephone(dto.telephone());
        e.setEmail(dto.email());
        e.setLogin(dto.login());
        e.setPassword(dto.password());
        e.setProfil(profil);
    }
}