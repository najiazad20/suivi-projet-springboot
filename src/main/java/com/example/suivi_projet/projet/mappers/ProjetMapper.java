package com.example.suivi_projet.projet.mappers;

import com.example.suivi_projet.projet.dto.ProjetCreateDTO;
import com.example.suivi_projet.projet.dto.ProjetResponseDTO;
import com.example.suivi_projet.projet.entities.Projet;
import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.organisation.entities.Organisme;

public class ProjetMapper {

    public static ProjetResponseDTO toDTO(Projet projet) {
        ProjetResponseDTO dto = new ProjetResponseDTO();
        dto.setId(projet.getId());
        dto.setCode(projet.getCode());
        dto.setNom(projet.getNom());
        dto.setDescription(projet.getDescription());
        dto.setDateDebut(projet.getDateDebut());
        dto.setDateFin(projet.getDateFin());
        dto.setMontant(projet.getMontant());
        dto.setOrganismeNom(projet.getOrganisme() != null ? projet.getOrganisme().getNom() : "");
        dto.setChefProjetNom(projet.getChefProjet() != null ? projet.getChefProjet().getNom() : "");
        return dto;
    }

    public static Projet toEntity(ProjetCreateDTO dto, Organisme organisme, Employe chefProjet) {
        Projet projet = new Projet();
        projet.setCode(dto.getCode());
        projet.setNom(dto.getNom());
        projet.setDescription(dto.getDescription());
        projet.setDateDebut(dto.getDateDebut());
        projet.setDateFin(dto.getDateFin());
        projet.setMontant(dto.getMontant());
        projet.setOrganisme(organisme);
        projet.setChefProjet(chefProjet);
        return projet;
    }

    public static void updateEntity(Projet projet, ProjetCreateDTO dto, Organisme organisme, Employe chefProjet) {
        projet.setCode(dto.getCode());
        projet.setNom(dto.getNom());
        projet.setDescription(dto.getDescription());
        projet.setDateDebut(dto.getDateDebut());
        projet.setDateFin(dto.getDateFin());
        projet.setMontant(dto.getMontant());
        projet.setOrganisme(organisme);
        projet.setChefProjet(chefProjet);
    }
}