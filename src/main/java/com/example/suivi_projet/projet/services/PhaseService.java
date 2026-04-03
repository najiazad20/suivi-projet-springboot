package com.example.suivi_projet.projet.services;

import com.example.suivi_projet.projet.dto.PhaseRequestDTO;
import com.example.suivi_projet.projet.dto.PhaseResponseDTO;
import com.example.suivi_projet.projet.entities.Phase;
import com.example.suivi_projet.projet.entities.Projet;
import com.example.suivi_projet.projet.mappers.PhaseMapper;
import com.example.suivi_projet.projet.repositories.PhaseRepository;
import com.example.suivi_projet.projet.repositories.ProjetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhaseService {

    private final PhaseRepository phaseRepository;
    private final ProjetRepository projetRepository;
    private final PhaseMapper phaseMapper;

    public PhaseService(PhaseRepository phaseRepository,
                        ProjetRepository projetRepository,
                        PhaseMapper phaseMapper) {
        this.phaseRepository = phaseRepository;
        this.projetRepository = projetRepository;
        this.phaseMapper = phaseMapper;
    }

    // CREATE
    public PhaseResponseDTO addPhase(int projetId, PhaseRequestDTO dto) {

        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        if (dto.dateDebut().before(projet.getDateDebut()) ||
                dto.dateFin().after(projet.getDateFin())) {
            throw new RuntimeException("Dates invalides");
        }

        double somme = phaseRepository.findByProjetId(projetId)
                .stream()
                .mapToDouble(Phase::getMontant)
                .sum();

        if (somme + dto.montant() > projet.getMontant()) {
            throw new RuntimeException("Montant dépassé");
        }

        Phase phase = phaseMapper.toEntity(dto, projet);

        return phaseMapper.toResponseDTO(phaseRepository.save(phase));
    }

    // GET phases d'un projet
    public List<PhaseResponseDTO> getPhasesByProjet(int projetId) {
        return phaseRepository.findByProjetId(projetId)
                .stream()
                .map(phaseMapper::toResponseDTO)
                .toList();
    }

    // GET by id
    public PhaseResponseDTO getPhaseById(int id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phase introuvable"));

        return phaseMapper.toResponseDTO(phase);
    }

    // UPDATE
    public PhaseResponseDTO updatePhase(int id, PhaseRequestDTO dto) {

        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phase introuvable"));

        Projet projet = phase.getProjet();

        if (dto.dateDebut().before(projet.getDateDebut()) ||
                dto.dateFin().after(projet.getDateFin())) {
            throw new RuntimeException("Dates invalides");
        }

        phaseMapper.updateEntityFromDTO(dto, phase);

        return phaseMapper.toResponseDTO(phaseRepository.save(phase));
    }

    // DELETE
    public void deletePhase(int id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phase introuvable"));

        phaseRepository.delete(phase);
    }

    // REALISATION
    public PhaseResponseDTO setRealisation(int id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phase introuvable"));

        phase.setEtatRealisation(true);

        return phaseMapper.toResponseDTO(phaseRepository.save(phase));
    }

    // FACTURATION
    public PhaseResponseDTO setFacturation(int id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phase introuvable"));

        phase.setEtatFacturation(true);

        return phaseMapper.toResponseDTO(phaseRepository.save(phase));
    }

    // PAIEMENT
    public PhaseResponseDTO setPaiement(int id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phase introuvable"));

        phase.setEtatPaiement(true);

        return phaseMapper.toResponseDTO(phaseRepository.save(phase));
    }

    // REPORTING 1
    public List<PhaseResponseDTO> getTermineesNonFacturees() {
        return phaseRepository.findByEtatRealisationTrueAndEtatFacturationFalse()
                .stream()
                .map(phaseMapper::toResponseDTO)
                .toList();
    }

    // REPORTING 2
    public List<PhaseResponseDTO> getFactureesNonPayees() {
        return phaseRepository.findByEtatFacturationTrueAndEtatPaiementFalse()
                .stream()
                .map(phaseMapper::toResponseDTO)
                .toList();
    }
}