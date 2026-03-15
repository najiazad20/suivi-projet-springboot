package com.example.suivi_projet.facturation.mappers;

import com.example.suivi_projet.facturation.dto.FactureDTO;
import com.example.suivi_projet.facturation.entities.Facture;
import com.example.suivi_projet.projet.entities.Phase;

public class FactureMapper {

    public static FactureDTO toDTO(Facture facture) {
        return new FactureDTO(
                facture.getId(),
                facture.getCode(),
                facture.getDateFacture(),
                facture.getPhase().getId(),
                facture.getPhase().getMontant() // Montant de la phase
        );
    }

    public static Facture toEntity(FactureDTO dto, Phase phase) {
        Facture facture = new Facture();
        facture.setCode(dto.getCode());
        facture.setDateFacture(dto.getDateFacture());
        facture.setPhase(phase);
        return facture;
    }

    public static void updateEntity(Facture facture, FactureDTO dto, Phase phase) {
        facture.setCode(dto.getCode());
        facture.setDateFacture(dto.getDateFacture());
        facture.setPhase(phase);
    }
}