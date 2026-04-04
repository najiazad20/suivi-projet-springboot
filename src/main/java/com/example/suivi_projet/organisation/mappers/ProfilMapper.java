package com.example.suivi_projet.organisation.mappers;

import com.example.suivi_projet.organisation.dto.ProfilRequestDTO;
import com.example.suivi_projet.organisation.dto.ProfilResponseDTO;
import com.example.suivi_projet.organisation.entities.Profil;
import org.springframework.stereotype.Component;

@Component
public class ProfilMapper {

    public Profil toEntity(ProfilRequestDTO dto) {

        Profil profil = new Profil();

        profil.setCode(dto.code());
        profil.setLibelle(dto.libelle());

        return profil;
    }

    public ProfilResponseDTO toDTO(Profil profil) {

        return new ProfilResponseDTO(
                profil.getId(),
                profil.getCode(),
                profil.getLibelle()
        );
    }

    public void updateEntity(Profil profil, ProfilRequestDTO dto) {

        profil.setCode(dto.code());
        profil.setLibelle(dto.libelle());
    }
}