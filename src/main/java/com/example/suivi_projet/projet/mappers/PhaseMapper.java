package com.example.suivi_projet.projet.mappers;

import com.example.suivi_projet.projet.dto.PhaseRequestDTO;
import com.example.suivi_projet.projet.dto.PhaseResponseDTO;
import com.example.suivi_projet.projet.entities.Phase;
import com.example.suivi_projet.projet.entities.Projet;
import org.springframework.stereotype.Component;

@Component
public class PhaseMapper {

    public Phase toEntity(PhaseRequestDTO dto, Projet projet) {

        Phase phase = new Phase();

        phase.setCode(dto.code());
        phase.setLibelle(dto.libelle());
        phase.setDescription(dto.description());
        phase.setDateDebut(dto.dateDebut());
        phase.setDateFin(dto.dateFin());
        phase.setMontant(dto.montant());

        phase.setEtatRealisation(false);
        phase.setEtatFacturation(false);
        phase.setEtatPaiement(false);

        phase.setProjet(projet);

        return phase;
    }

    public PhaseResponseDTO toResponseDTO(Phase phase) {
        return new PhaseResponseDTO(
                phase.getId(),
                phase.getCode(),
                phase.getLibelle(),
                phase.getDescription(),
                phase.getDateDebut(),
                phase.getDateFin(),
                phase.getMontant(),
                phase.isEtatRealisation(),
                phase.isEtatFacturation(),
                phase.isEtatPaiement(),
                phase.getProjet().getId()
        );
    }

    public void updateEntityFromDTO(PhaseRequestDTO dto, Phase phase) {

        phase.setCode(dto.code());
        phase.setLibelle(dto.libelle());
        phase.setDescription(dto.description());
        phase.setDateDebut(dto.dateDebut());
        phase.setDateFin(dto.dateFin());
        phase.setMontant(dto.montant());
    }
}