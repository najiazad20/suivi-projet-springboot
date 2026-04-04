package com.example.suivi_projet.facturation.services;
import com.example.suivi_projet.exceptions.ResourceNotFoundException;
import com.example.suivi_projet.exceptions.BusinessException;
import com.example.suivi_projet.facturation.dto.FactureRequestDTO;
import com.example.suivi_projet.facturation.dto.FactureResponseDTO;
import com.example.suivi_projet.facturation.entities.Facture;
import com.example.suivi_projet.facturation.mappers.FactureMapper;
import com.example.suivi_projet.facturation.repositories.FactureRepository;
import com.example.suivi_projet.projet.entities.Phase;
import com.example.suivi_projet.projet.repositories.PhaseRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactureService {

    private final FactureRepository factureRepository;
    private final PhaseRepository phaseRepository;
    private final FactureMapper factureMapper;

    public FactureService(FactureRepository factureRepository,
                          PhaseRepository phaseRepository,
                          FactureMapper factureMapper) {
        this.factureRepository = factureRepository;
        this.phaseRepository = phaseRepository;
        this.factureMapper = factureMapper;
    }

    public FactureResponseDTO createFacture(Integer phaseId, FactureRequestDTO dto) {

        Phase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable"));

        //  règle 1 : phase terminée
        if (!phase.isEtatRealisation()) {
            throw new BusinessException("La phase doit être terminée");
        }

        //  règle 2 : pas double facturation
        if (phase.isEtatFacturation()) {
            throw new BusinessException("Phase déjà facturée");
        }

        Facture facture = factureMapper.toEntity(dto, phase);

        phase.setEtatFacturation(true);
        phaseRepository.save(phase);

        Facture saved = factureRepository.save(facture);

        return factureMapper.toDTO(saved);
    }
    public List<FactureResponseDTO> getAllFactures() {

        return factureRepository.findAll()
                .stream()
                .map(factureMapper::toDTO)
                .toList();
    }

    public FactureResponseDTO getFactureById(Integer id) {

        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture introuvable avec id : " + id));

        return factureMapper.toDTO(facture);
    }

    public FactureResponseDTO updateFacture(Integer id, FactureRequestDTO dto) {

        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture introuvable avec id : " + id));

        factureMapper.updateEntityFromDTO(dto, facture);

        Facture updated = factureRepository.save(facture);

        return factureMapper.toDTO(updated);
    }

    public void deleteFacture(Integer id) {

        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture introuvable avec id : " + id));

        Phase phase = facture.getPhase();

        // remise à false si suppression
        phase.setEtatFacturation(false);
        phaseRepository.save(phase);

        factureRepository.delete(facture);
    }
    public List<FactureResponseDTO> getFacturesByCode(String code) {

        return factureRepository.findByCode(code)
                .stream()
                .map(factureMapper::toDTO)
                .toList();
    }
    //  recherche comptable
    public List<FactureResponseDTO> getFacturesByDate(java.util.Date date) {

        return factureRepository.findByDateFacture(date)
                .stream()
                .map(factureMapper::toDTO)
                .toList();
    }
}