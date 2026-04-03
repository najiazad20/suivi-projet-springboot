package com.example.suivi_projet.organisation.mappers;

import com.example.suivi_projet.organisation.dto.OrganismeRequestDTO;
import com.example.suivi_projet.organisation.dto.OrganismeResponseDTO;
import com.example.suivi_projet.organisation.entities.Organisme;
import org.springframework.stereotype.Component;

@Component
public class OrganismeMapper {

    public Organisme toEntity(OrganismeRequestDTO dto) {

        Organisme org = new Organisme();

        org.setCode(dto.code());
        org.setNom(dto.nom());
        org.setAdresse(dto.adresse());
        org.setTelephone(dto.telephone());
        org.setNomContact(dto.nomContact());
        org.setEmailContact(dto.emailContact());
        org.setSiteWeb(dto.siteWeb());

        return org;
    }

    public OrganismeResponseDTO toDTO(Organisme org) {

        return new OrganismeResponseDTO(
                org.getId(),
                org.getCode(),
                org.getNom(),
                org.getAdresse(),
                org.getTelephone(),
                org.getNomContact(),
                org.getEmailContact(),
                org.getSiteWeb()
        );
    }

    public void updateEntityFromDTO(OrganismeRequestDTO dto, Organisme org) {

        org.setCode(dto.code());
        org.setNom(dto.nom());
        org.setAdresse(dto.adresse());
        org.setTelephone(dto.telephone());
        org.setNomContact(dto.nomContact());
        org.setEmailContact(dto.emailContact());
        org.setSiteWeb(dto.siteWeb());
    }
}