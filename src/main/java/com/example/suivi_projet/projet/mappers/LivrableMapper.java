package com.example.suivi_projet.projet.mappers;

import com.example.suivi_projet.projet.dto.LivrableDTO;
import com.example.suivi_projet.projet.entities.Livrable;

public class LivrableMapper {

    public static LivrableDTO toDTO(Livrable livrable) {

        LivrableDTO dto = new LivrableDTO();

        dto.setId(livrable.getId());
        dto.setCode(livrable.getCode());
        dto.setLibelle(livrable.getLibelle());
        dto.setDescription(livrable.getDescription());
        dto.setChemin(livrable.getChemin());

        if (livrable.getPhase() != null) {
            dto.setPhaseId(livrable.getPhase().getId());
        }

        return dto;
    }

    public static Livrable toEntity(LivrableDTO dto) {

        Livrable livrable = new Livrable();

        livrable.setId(dto.getId());
        livrable.setCode(dto.getCode());
        livrable.setLibelle(dto.getLibelle());
        livrable.setDescription(dto.getDescription());
        livrable.setChemin(dto.getChemin());

        return livrable;
    }
}