package com.example.suivi_projet.projet.mappers;

import com.example.suivi_projet.projet.dto.PhaseDTO;
import com.example.suivi_projet.projet.entities.Phase;

public class PhaseMapper {

    public static PhaseDTO toDTO(Phase phase) {

        PhaseDTO dto = new PhaseDTO();

        dto.setId(phase.getId());
        dto.setCode(phase.getCode());
        dto.setLibelle(phase.getLibelle());
        dto.setDescription(phase.getDescription());
        dto.setDateDebut(phase.getDateDebut());
        dto.setDateFin(phase.getDateFin());
        dto.setMontant(phase.getMontant());

        dto.setEtatRealisation(phase.isEtatRealisation());
        dto.setEtatFacturation(phase.isEtatFacturation());
        dto.setEtatPaiement(phase.isEtatPaiement());

        if (phase.getProjet() != null) {
            dto.setProjetId(phase.getProjet().getId());
        }

        return dto;
    }

    public static Phase toEntity(PhaseDTO dto) {

        Phase phase = new Phase();

        phase.setId(dto.getId());
        phase.setCode(dto.getCode());
        phase.setLibelle(dto.getLibelle());
        phase.setDescription(dto.getDescription());
        phase.setDateDebut(dto.getDateDebut());
        phase.setDateFin(dto.getDateFin());
        phase.setMontant(dto.getMontant());

        phase.setEtatRealisation(dto.isEtatRealisation());
        phase.setEtatFacturation(dto.isEtatFacturation());
        phase.setEtatPaiement(dto.isEtatPaiement());

        return phase;
    }
}