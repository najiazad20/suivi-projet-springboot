package com.example.suivi_projet.projet.mappers;

import com.example.suivi_projet.projet.dto.*;
import com.example.suivi_projet.projet.entities.*;
import com.example.suivi_projet.organisation.entities.Employe;
import org.springframework.stereotype.Component;

@Component
public class LigneEmployePhaseMapper {

    public LigneEmployePhase toEntity(LigneEmployePhaseRequestDTO dto,
                                      Employe employe,
                                      Phase phase) {
        return new LigneEmployePhase(
                dto.dateDebut(),
                dto.dateFin(),
                employe,
                phase
        );
    }

    public LigneEmployePhaseResponseDTO toResponseDTO(LigneEmployePhase l) {
        return new LigneEmployePhaseResponseDTO(
                l.getEmploye().getId(),
                l.getPhase().getId(),
                l.getDateDebut(),
                l.getDateFin()
        );
    }

    public void updateEntityFromDTO(LigneEmployePhaseRequestDTO dto,
                                    LigneEmployePhase l) {
        l.setDateDebut(dto.dateDebut());
        l.setDateFin(dto.dateFin());
    }
}