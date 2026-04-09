package com.example.suivi_projet.projet.mappers;

import com.example.suivi_projet.projet.dto.LigneEmployePhaseRequestDTO;
import com.example.suivi_projet.projet.dto.LigneEmployePhaseResponseDTO;
import com.example.suivi_projet.projet.entities.LigneEmployePhase;
import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.projet.entities.LigneEmployePhaseId;
import com.example.suivi_projet.projet.entities.Phase;
import org.springframework.stereotype.Component;

@Component
public class LigneEmployePhaseMapper {

    public LigneEmployePhase toEntity(LigneEmployePhaseRequestDTO dto,
                                      Employe employe,
                                      Phase phase) {

        LigneEmployePhase ligne = new LigneEmployePhase();
        ligne.setId(new LigneEmployePhaseId(
                employe.getId(),
                phase.getId()
        ));
        ligne.setDateDebut(dto.dateDebut());
        ligne.setDateFin(dto.dateFin());
        ligne.setEmploye(employe);
        ligne.setPhase(phase);

        return ligne;
    }

    public LigneEmployePhaseResponseDTO toResponseDTO(LigneEmployePhase ligne) {
        return new LigneEmployePhaseResponseDTO(
                ligne.getEmploye().getId(),
                ligne.getPhase().getId(),
                ligne.getDateDebut(),
                ligne.getDateFin()
        );
    }

    public void updateEntityFromDTO(LigneEmployePhaseRequestDTO dto,
                                    LigneEmployePhase ligne) {

        ligne.setDateDebut(dto.dateDebut());
        ligne.setDateFin(dto.dateFin());
    }
}