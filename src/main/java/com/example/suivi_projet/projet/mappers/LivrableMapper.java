package com.example.suivi_projet.projet.mappers;

import com.example.suivi_projet.projet.dto.LivrableRequestDTO;
import com.example.suivi_projet.projet.dto.LivrableResponseDTO;
import com.example.suivi_projet.projet.entities.Livrable;
import com.example.suivi_projet.projet.entities.Phase;
import org.springframework.stereotype.Component;

@Component
public class LivrableMapper {

    public Livrable toEntity(LivrableRequestDTO dto, Phase phase) {
        Livrable livrable = new Livrable();
        livrable.setCode(dto.code());
        livrable.setLibelle(dto.libelle());
        livrable.setDescription(dto.description());
        livrable.setChemin(dto.chemin());
        livrable.setPhase(phase);
        return livrable;
    }

    public LivrableResponseDTO toResponseDTO(Livrable livrable) {
        return new LivrableResponseDTO(
                livrable.getId(),
                livrable.getCode(),
                livrable.getLibelle(),
                livrable.getDescription(),
                livrable.getChemin(),
                livrable.getPhase().getId()
        );
    }

    public void updateEntityFromDTO(LivrableRequestDTO dto, Livrable livrable, Phase phase) {
        livrable.setCode(dto.code());
        livrable.setLibelle(dto.libelle());
        livrable.setDescription(dto.description());
        livrable.setChemin(dto.chemin());
        livrable.setPhase(phase);
    }
}