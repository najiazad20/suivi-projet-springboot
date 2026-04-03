package com.example.suivi_projet.projet.services;

import com.example.suivi_projet.projet.dto.LivrableRequestDTO;
import com.example.suivi_projet.projet.dto.LivrableResponseDTO;
import com.example.suivi_projet.projet.entities.Livrable;
import com.example.suivi_projet.projet.entities.Phase;
import com.example.suivi_projet.projet.mappers.LivrableMapper;
import com.example.suivi_projet.projet.repositories.LivrableRepository;
import com.example.suivi_projet.projet.repositories.PhaseRepository;
import com.example.suivi_projet.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivrableService {

    private final LivrableRepository livrableRepository;
    private final PhaseRepository phaseRepository;
    private final LivrableMapper livrableMapper;

    public LivrableService(LivrableRepository livrableRepository,
                           PhaseRepository phaseRepository,
                           LivrableMapper livrableMapper) {
        this.livrableRepository = livrableRepository;
        this.phaseRepository = phaseRepository;
        this.livrableMapper = livrableMapper;
    }

    // CREATE
    public LivrableResponseDTO createLivrable(int phaseId, LivrableRequestDTO dto) {

        Phase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable"));

        Livrable livrable = livrableMapper.toEntity(dto, phase);

        Livrable saved = livrableRepository.save(livrable);

        return livrableMapper.toResponseDTO(saved);
    }

    // GET BY PHASE
    public List<LivrableResponseDTO> getLivrablesByPhase(int phaseId) {

        return livrableRepository.findByPhaseId(phaseId)
                .stream()
                .map(livrableMapper::toResponseDTO)
                .toList();
    }

    // GET BY ID
    public LivrableResponseDTO getLivrableById(int id) {

        Livrable livrable = livrableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livrable introuvable"));

        return livrableMapper.toResponseDTO(livrable);
    }

    // UPDATE
    public LivrableResponseDTO updateLivrable(int id, LivrableRequestDTO dto) {

        Livrable livrable = livrableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livrable introuvable"));

        Phase phase = phaseRepository.findById(dto.phaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable"));

        livrableMapper.updateEntityFromDTO(dto, livrable, phase);

        Livrable updated = livrableRepository.save(livrable);

        return livrableMapper.toResponseDTO(updated);
    }

    // DELETE
    public void deleteLivrable(int id) {

        Livrable livrable = livrableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livrable introuvable"));

        livrableRepository.delete(livrable);
    }
}