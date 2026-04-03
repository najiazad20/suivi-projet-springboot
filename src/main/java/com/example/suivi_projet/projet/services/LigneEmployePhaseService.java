package com.example.suivi_projet.projet.services;

import com.example.suivi_projet.projet.dto.*;
import com.example.suivi_projet.projet.entities.*;
import com.example.suivi_projet.projet.mappers.LigneEmployePhaseMapper;
import com.example.suivi_projet.projet.repositories.*;
import com.example.suivi_projet.organisation.repositories.EmployeRepository;
import com.example.suivi_projet.organisation.entities.Employe;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LigneEmployePhaseService {

    private final LigneEmployePhaseRepository repo;
    private final PhaseRepository phaseRepo;
    private final EmployeRepository employeRepo;
    private final LigneEmployePhaseMapper mapper;

    public LigneEmployePhaseService(LigneEmployePhaseRepository repo,
                                    PhaseRepository phaseRepo,
                                    EmployeRepository employeRepo,
                                    LigneEmployePhaseMapper mapper) {
        this.repo = repo;
        this.phaseRepo = phaseRepo;
        this.employeRepo = employeRepo;
        this.mapper = mapper;
    }

    // CREATE
    public LigneEmployePhaseResponseDTO create(int phaseId, int employeId,
                                               LigneEmployePhaseRequestDTO dto) {

        Phase phase = phaseRepo.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Phase introuvable"));

        Employe employe = employeRepo.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé introuvable"));

        // ❗ règles métier
        if (dto.dateDebut().before(phase.getDateDebut()) ||
                dto.dateFin().after(phase.getDateFin())) {
            throw new RuntimeException("Dates hors intervalle phase");
        }

        LigneEmployePhaseId id = new LigneEmployePhaseId(employeId, phaseId);

        if (repo.existsById(id)) {
            throw new RuntimeException("Affectation déjà existante");
        }

        LigneEmployePhase l = mapper.toEntity(dto, employe, phase);

        return mapper.toResponseDTO(repo.save(l));
    }

    // GET employes par phase
    public List<LigneEmployePhaseResponseDTO> getEmployesByPhase(int phaseId) {
        return repo.findByPhaseId(phaseId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    // GET ONE
    public LigneEmployePhaseResponseDTO get(int phaseId, int employeId) {
        LigneEmployePhase l = repo.findById(new LigneEmployePhaseId(employeId, phaseId))
                .orElseThrow(() -> new RuntimeException("Affectation introuvable"));

        return mapper.toResponseDTO(l);
    }

    // UPDATE
    public LigneEmployePhaseResponseDTO update(int phaseId, int employeId,
                                               LigneEmployePhaseRequestDTO dto) {

        LigneEmployePhase l = repo.findById(new LigneEmployePhaseId(employeId, phaseId))
                .orElseThrow(() -> new RuntimeException("Affectation introuvable"));

        mapper.updateEntityFromDTO(dto, l);

        return mapper.toResponseDTO(repo.save(l));
    }

    // DELETE
    public void delete(int phaseId, int employeId) {
        LigneEmployePhaseId id = new LigneEmployePhaseId(employeId, phaseId);

        if (!repo.existsById(id)) {
            throw new RuntimeException("Affectation introuvable");
        }

        repo.deleteById(id);
    }

    // phases par employe
    public List<LigneEmployePhaseResponseDTO> getPhasesByEmploye(int employeId) {
        return repo.findByEmployeId(employeId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }
}