package com.example.suivi_projet.projet.mappers;

import com.example.suivi_projet.projet.dto.ProjetRequestDTO;
import com.example.suivi_projet.projet.dto.ProjetResponseDTO;
import com.example.suivi_projet.projet.entities.Projet;
import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.organisation.entities.Organisme;
import org.springframework.stereotype.Component;

@Component
public class ProjetMapper {

    public Projet toEntity(ProjetRequestDTO dto, Organisme organisme, Employe chefProjet) {
        Projet projet = new Projet();

        projet.setCode(dto.code());
        projet.setNom(dto.nom());
        projet.setDescription(dto.description());
        projet.setDateDebut(dto.dateDebut());
        projet.setDateFin(dto.dateFin());
        projet.setMontant(dto.montant());
        projet.setOrganisme(organisme);
        projet.setChefProjet(chefProjet);

        return projet;
    }

    public ProjetResponseDTO toResponseDTO(Projet projet) {
        return new ProjetResponseDTO(
                projet.getId(),
                projet.getCode(),
                projet.getNom(),
                projet.getDescription(),
                projet.getDateDebut(),
                projet.getDateFin(),
                projet.getMontant(),
                projet.getOrganisme().getNom(),
                projet.getChefProjet().getNom()
        );
    }

    public void updateEntityFromDTO(ProjetRequestDTO dto, Projet projet,
                                    Organisme organisme, Employe chefProjet) {

        projet.setCode(dto.code());
        projet.setNom(dto.nom());
        projet.setDescription(dto.description());
        projet.setDateDebut(dto.dateDebut());
        projet.setDateFin(dto.dateFin());
        projet.setMontant(dto.montant());
        projet.setOrganisme(organisme);
        projet.setChefProjet(chefProjet);
    }
}