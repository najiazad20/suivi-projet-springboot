package com.example.suivi_projet.projet.services;

import com.example.suivi_projet.exceptions.BusinessException;
import com.example.suivi_projet.exceptions.ResourceNotFoundException;
import com.example.suivi_projet.projet.dto.ProjetRequestDTO;
import com.example.suivi_projet.projet.dto.ProjetResponseDTO;
import com.example.suivi_projet.projet.entities.Projet;
import com.example.suivi_projet.projet.mappers.ProjetMapper;
import com.example.suivi_projet.projet.repositories.ProjetRepository;
import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.organisation.entities.Organisme;
import com.example.suivi_projet.organisation.repositories.EmployeRepository;
import com.example.suivi_projet.organisation.repositories.OrganismeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetService {

    private final ProjetRepository projetRepository;
    private final OrganismeRepository organismeRepository;
    private final EmployeRepository employeRepository;
    private final ProjetMapper projetMapper;

    public ProjetService(ProjetRepository projetRepository,
                         OrganismeRepository organismeRepository,
                         EmployeRepository employeRepository,
                         ProjetMapper projetMapper) {

        this.projetRepository = projetRepository;
        this.organismeRepository = organismeRepository;
        this.employeRepository = employeRepository;
        this.projetMapper = projetMapper;
    }


    public ProjetResponseDTO addProjet(ProjetRequestDTO dto) {


        if (dto.dateDebut().after(dto.dateFin())) {
            throw new BusinessException("La date de début doit être avant la date de fin");
        }

        if (projetRepository.findByCode(dto.code()) != null) {
            throw new BusinessException("Le code projet existe déjà");
        }

        Organisme organisme = organismeRepository.findById(dto.organismeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Organisme introuvable avec id : " + dto.organismeId())
                );

        Employe chef = employeRepository.findById(dto.chefProjetId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Chef de projet introuvable avec id : " + dto.chefProjetId())
                );

        Projet projet = projetMapper.toEntity(dto, organisme, chef);

        return projetMapper.toResponseDTO(projetRepository.save(projet));
    }

    // GET ALL
    public List<ProjetResponseDTO> getAllProjets() {
        return projetRepository.findAll()
                .stream()
                .map(projetMapper::toResponseDTO)
                .toList();
    }

    // GET BY ID
    public ProjetResponseDTO getProjetById(int id) {

        Projet projet = projetRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Projet introuvable avec id : " + id)
                );

        return projetMapper.toResponseDTO(projet);
    }

    // UPDATE
    public ProjetResponseDTO updateProjet(int id, ProjetRequestDTO dto) {

        Projet projet = projetRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Projet introuvable avec id : " + id)
                );

        if (dto.dateDebut().after(dto.dateFin())) {
            throw new BusinessException("La date de début doit être avant la date de fin");
        }

        Organisme organisme = organismeRepository.findById(dto.organismeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Organisme introuvable avec id : " + dto.organismeId())
                );

        Employe chef = employeRepository.findById(dto.chefProjetId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Chef de projet introuvable avec id : " + dto.chefProjetId())
                );

        projetMapper.updateEntityFromDTO(dto, projet, organisme, chef);

        return projetMapper.toResponseDTO(projetRepository.save(projet));
    }

    // DELETE
    public void deleteProjet(int id) {

        Projet projet = projetRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Projet introuvable avec id : " + id)
                );

        projetRepository.delete(projet);
    }

    // FIND BY CODE
    public ProjetResponseDTO getByCode(String code) {

        Projet projet = projetRepository.findByCode(code);

        if (projet == null) {
            throw new ResourceNotFoundException("Projet introuvable avec code : " + code);
        }

        return projetMapper.toResponseDTO(projet);
    }

    // FIND BY MONTANT
    public List<ProjetResponseDTO> getByMontant(double montant) {
        return projetRepository.findByMontant(montant)
                .stream()
                .map(projetMapper::toResponseDTO)
                .toList();
    }
}