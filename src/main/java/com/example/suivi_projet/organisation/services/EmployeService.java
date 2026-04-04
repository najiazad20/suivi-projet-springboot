package com.example.suivi_projet.organisation.services;

import com.example.suivi_projet.organisation.dto.*;
import com.example.suivi_projet.organisation.entities.*;
import com.example.suivi_projet.organisation.mappers.EmployeMapper;
import com.example.suivi_projet.organisation.repositories.*;
import com.example.suivi_projet.exceptions.BusinessException;
import com.example.suivi_projet.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeService {

    private final EmployeRepository employeRepository;
    private final ProfilRepository profilRepository;
    private final EmployeMapper mapper;

    public EmployeService(EmployeRepository employeRepository,
                          ProfilRepository profilRepository,
                          EmployeMapper mapper) {
        this.employeRepository = employeRepository;
        this.profilRepository = profilRepository;
        this.mapper = mapper;
    }

    // CREATE
    public EmployeResponseDTO save(EmployeRequestDTO dto) {

        if (employeRepository.findByMatricule(dto.matricule()) != null)
            throw new BusinessException("Matricule déjà utilisé");

        if (employeRepository.findByLogin(dto.login()) != null)
            throw new BusinessException("Login déjà utilisé");

        Profil profil = profilRepository.findById(dto.profilId())
                .orElseThrow(() -> new ResourceNotFoundException("Profil introuvable"));

        Employe e = mapper.toEntity(dto, profil);

        return mapper.toDTO(employeRepository.save(e));
    }

    // GET ALL
    public List<EmployeResponseDTO> findAll(String nom) {

        return employeRepository.findAll()
                .stream()
                .filter(e -> nom == null || e.getNom().toLowerCase().contains(nom.toLowerCase()))
                .map(mapper::toDTO)
                .toList();
    }

    // GET BY ID
    public EmployeResponseDTO findById(int id) {

        Employe e = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable"));

        return mapper.toDTO(e);
    }

    // UPDATE
    public EmployeResponseDTO update(int id, EmployeRequestDTO dto) {

        Employe e = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable"));

        Profil profil = profilRepository.findById(dto.profilId())
                .orElseThrow(() -> new ResourceNotFoundException("Profil introuvable"));

        mapper.update(dto, e, profil);

        return mapper.toDTO(employeRepository.save(e));
    }

    // DELETE
    public void delete(int id) {

        Employe e = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable"));

        employeRepository.delete(e);
    }

    // DISPONIBILITÉ
    public List<EmployeResponseDTO> getDisponibles(LocalDate d1, LocalDate d2) {

        return employeRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}