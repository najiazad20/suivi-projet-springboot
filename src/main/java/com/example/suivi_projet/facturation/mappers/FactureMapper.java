package com.example.suivi_projet.facturation.mappers;

import com.example.suivi_projet.facturation.dto.FactureRequestDTO;
import com.example.suivi_projet.facturation.dto.FactureResponseDTO;
import com.example.suivi_projet.facturation.entities.Facture;
import com.example.suivi_projet.projet.entities.Phase;
import org.springframework.stereotype.Component;

@Component
public class FactureMapper {

    public Facture toEntity(FactureRequestDTO dto, Phase phase) {

        Facture facture = new Facture();

        facture.setCode(dto.code());
        facture.setDateFacture(dto.dateFacture());
        facture.setPhase(phase);

        return facture;
    }

    public FactureResponseDTO toDTO(Facture facture) {

        return new FactureResponseDTO(
                facture.getId(),
                facture.getCode(),
                facture.getDateFacture(),
                facture.getPhase().getId(),
                facture.getPhase().getMontant()
        );
    }

    public void updateEntityFromDTO(FactureRequestDTO dto, Facture facture) {

        facture.setCode(dto.code());
        facture.setDateFacture(dto.dateFacture());
    }
}