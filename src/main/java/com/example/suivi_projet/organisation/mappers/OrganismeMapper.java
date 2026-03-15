package com.example.suivi_projet.organisation.mappers;

import com.example.suivi_projet.organisation.entities.Organisme;
import com.example.suivi_projet.organisation.dto.OrganismeCreateDTO;
import com.example.suivi_projet.organisation.dto.OrganismeResponseDTO;

public class OrganismeMapper {

    public static Organisme toEntity(OrganismeCreateDTO dto) {
        Organisme org = new Organisme();
        org.setCode(dto.getCode());
        org.setNom(dto.getNom());
        org.setAdresse(dto.getAdresse());
        org.setTelephone(dto.getTelephone());
        org.setNomContact(dto.getNomContact());
        org.setEmailContact(dto.getEmailContact());
        org.setSiteWeb(dto.getSiteWeb());
        return org;
    }

    public static OrganismeResponseDTO toDTO(Organisme org) {
        OrganismeResponseDTO dto = new OrganismeResponseDTO();
        dto.setId(org.getId());
        dto.setCode(org.getCode());
        dto.setNom(org.getNom());
        dto.setAdresse(org.getAdresse());
        dto.setTelephone(org.getTelephone());
        dto.setNomContact(org.getNomContact());
        dto.setEmailContact(org.getEmailContact());
        dto.setSiteWeb(org.getSiteWeb());
        return dto;
    }
}