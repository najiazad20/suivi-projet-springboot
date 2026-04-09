package com.example.suivi_projet.projet.services;

import com.example.suivi_projet.exceptions.BusinessException;
import com.example.suivi_projet.exceptions.ResourceNotFoundException;
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
    public PhaseResponseDTO addPhase(PhaseRequestDTO dto) {
        // 1. On récupère le projetId depuis le DTO
        int projetId = dto.projetId();

        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Projet introuvable avec id : " + projetId)
                );

        // règle métier dates (Inchangée, c'est parfait)
        if (dto.dateDebut().before(projet.getDateDebut()) ||
                dto.dateFin().after(projet.getDateFin())) {
            throw new BusinessException("Les dates de la phase doivent être incluses dans celles du projet");
        }

        // règle métier montant (Inchangée, très bien joué)
        double somme = phaseRepository.findByProjetId(projetId)
                .stream()
                .mapToDouble(Phase::getMontant)
                .sum();

        if (somme + dto.montant() > projet.getMontant()) {
            throw new BusinessException("Le montant total des phases dépasse celui du projet");
        }

        // Mapping et sauvegarde
        Phase phase = phaseMapper.toEntity(dto, projet);
        return phaseMapper.toResponseDTO(phaseRepository.save(phase));
    }

    // GET phases d'un projet
    public List<PhaseResponseDTO> getPhasesByProjet(int projetId) {


        if (!projetRepository.existsById(projetId)) {
            throw new ResourceNotFoundException("Projet introuvable avec id : " + projetId);
        }

        return phaseRepository.findByProjetId(projetId)
                .stream()
                .map(phaseMapper::toResponseDTO)
                .toList();
    }

    // GET by id
    public PhaseResponseDTO getPhaseById(int id) {

        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Phase introuvable avec id : " + id)
                );

        return phaseMapper.toResponseDTO(phase);
    }

    // UPDATE
    public PhaseResponseDTO updatePhase(int id, PhaseRequestDTO dto) {

        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Phase introuvable avec id : " + id)
                );

        Projet projet = phase.getProjet();


        if (dto.dateDebut().before(projet.getDateDebut()) ||
                dto.dateFin().after(projet.getDateFin())) {
            throw new BusinessException("Les dates de la phase doivent être incluses dans celles du projet");
        }


        double somme = phaseRepository.findByProjetId(projet.getId())
                .stream()
                .filter(p -> p.getId() != id)
                .mapToDouble(Phase::getMontant)
                .sum();

        if (somme + dto.montant() > projet.getMontant()) {
            throw new BusinessException("Le montant total des phases dépasse celui du projet");
        }

        phaseMapper.updateEntityFromDTO(dto, phase);

        return phaseMapper.toResponseDTO(phaseRepository.save(phase));
    }

    // DELETE
    public void deletePhase(int id) {

        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Phase introuvable avec id : " + id)
                );

        phaseRepository.delete(phase);
    }

    // REALISATION
    public PhaseResponseDTO setRealisation(int id) {

        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Phase introuvable avec id : " + id)
                );

        phase.setEtatRealisation(true);

        return phaseMapper.toResponseDTO(phaseRepository.save(phase));
    }

    // FACTURATION
    public PhaseResponseDTO setFacturation(int id) {

        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Phase introuvable avec id : " + id)
                );

        phase.setEtatFacturation(true);

        return phaseMapper.toResponseDTO(phaseRepository.save(phase));
    }

    // PAIEMENT
    public PhaseResponseDTO setPaiement(int id) {

        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Phase introuvable avec id : " + id)
                );

        phase.setEtatPaiement(true);

        return phaseMapper.toResponseDTO(phaseRepository.save(phase));
    }


    public List<PhaseResponseDTO> getTermineesNonFacturees() {
        return phaseRepository.findByEtatRealisationTrueAndEtatFacturationFalse()
                .stream()
                .map(phaseMapper::toResponseDTO)
                .toList();
    }


    public List<PhaseResponseDTO> getFactureesNonPayees() {
        return phaseRepository.findByEtatFacturationTrueAndEtatPaiementFalse()
                .stream()
                .map(phaseMapper::toResponseDTO)
                .toList();
    }
}