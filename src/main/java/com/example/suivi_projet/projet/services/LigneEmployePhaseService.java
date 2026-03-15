package com.example.suivi_projet.projet.services;

import com.example.suivi_projet.projet.dto.LigneEmployePhaseCreateDTO;
import com.example.suivi_projet.projet.dto.LigneEmployePhaseResponseDTO;
import com.example.suivi_projet.projet.entities.LigneEmployePhase;
import com.example.suivi_projet.projet.entities.LigneEmployePhaseId;
import com.example.suivi_projet.projet.entities.Phase;
import com.example.suivi_projet.projet.repositories.LigneEmployePhaseRepository;
import com.example.suivi_projet.projet.repositories.PhaseRepository;
import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.organisation.repositories.EmployeRepository;
import com.example.suivi_projet.projet.mappers.LigneEmployePhaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LigneEmployePhaseService {

    @Autowired
    private LigneEmployePhaseRepository repo;

    @Autowired
    private PhaseRepository phaseRepo;

    @Autowired
    private EmployeRepository employeRepo;

    public LigneEmployePhaseResponseDTO create(int phaseId, int employeId, LigneEmployePhaseCreateDTO dto) {
        Phase phase = phaseRepo.findById(phaseId).get();
        Employe employe = employeRepo.findById(employeId).get();

        if(dto.getDateDebut().before(phase.getDateDebut()) || dto.getDateFin().after(phase.getDateFin())) {
            throw new RuntimeException("Dates doivent être dans l'intervalle de la phase");
        }

        LigneEmployePhaseId id = new LigneEmployePhaseId(employeId, phaseId);
        if(repo.findById(id).isPresent()) {
            throw new RuntimeException("Affectation déjà existante");
        }

        LigneEmployePhase l = LigneEmployePhaseMapper.toEntity(dto, employe, phase);
        repo.save(l);
        return LigneEmployePhaseMapper.toDTO(l);
    }

    public List<LigneEmployePhaseResponseDTO> getEmployesByPhase(int phaseId) {
        List<LigneEmployePhase> list = repo.findByPhaseId(phaseId);
        List<LigneEmployePhaseResponseDTO> result = new ArrayList<>();
        for(LigneEmployePhase l : list) {
            result.add(LigneEmployePhaseMapper.toDTO(l));
        }
        return result;
    }

    public LigneEmployePhaseResponseDTO get(int phaseId, int employeId) {
        LigneEmployePhase l = repo.findById(new LigneEmployePhaseId(employeId, phaseId)).get();
        return LigneEmployePhaseMapper.toDTO(l);
    }

    public LigneEmployePhaseResponseDTO update(int phaseId, int employeId, LigneEmployePhaseCreateDTO dto) {
        LigneEmployePhase l = repo.findById(new LigneEmployePhaseId(employeId, phaseId)).get();
        LigneEmployePhaseMapper.updateEntity(l, dto);
        repo.save(l);
        return LigneEmployePhaseMapper.toDTO(l);
    }

    public boolean delete(int phaseId, int employeId) {
        LigneEmployePhaseId id = new LigneEmployePhaseId(employeId, phaseId);
        if(repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<LigneEmployePhaseResponseDTO> getPhasesByEmploye(int employeId) {
        List<LigneEmployePhase> list = repo.findByEmployeId(employeId);
        List<LigneEmployePhaseResponseDTO> result = new ArrayList<>();
        for(LigneEmployePhase l : list) {
            result.add(LigneEmployePhaseMapper.toDTO(l));
        }
        return result;
    }
}