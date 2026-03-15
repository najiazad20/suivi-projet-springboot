package com.example.suivi_projet.projet.mappers;

import com.example.suivi_projet.projet.dto.LigneEmployePhaseCreateDTO;
import com.example.suivi_projet.projet.dto.LigneEmployePhaseResponseDTO;
import com.example.suivi_projet.projet.entities.LigneEmployePhase;
import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.projet.entities.Phase;

public class LigneEmployePhaseMapper {

    public static LigneEmployePhase toEntity(LigneEmployePhaseCreateDTO dto, Employe employe, Phase phase) {
        return new LigneEmployePhase(dto.getDateDebut(), dto.getDateFin(), employe, phase);
    }

    public static LigneEmployePhaseResponseDTO toDTO(LigneEmployePhase l) {
        return new LigneEmployePhaseResponseDTO(
                l.getEmploye().getId(),
                l.getPhase().getId(),
                l.getDateDebut(),
                l.getDateFin()
        );
    }

    public static void updateEntity(LigneEmployePhase l, LigneEmployePhaseCreateDTO dto) {
        l.setDateDebut(dto.getDateDebut());
        l.setDateFin(dto.getDateFin());
    }
}