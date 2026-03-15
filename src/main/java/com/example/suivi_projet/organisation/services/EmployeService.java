package com.example.suivi_projet.organisation.services;







import com.example.suivi_projet.organisation.dto.EmployeCreateDTO;
import com.example.suivi_projet.organisation.dto.EmployeResponseDTO;
import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.organisation.entities.Profil;
import com.example.suivi_projet.organisation.mappers.EmployeMapper;
import com.example.suivi_projet.organisation.repositories.EmployeRepository;
import com.example.suivi_projet.organisation.repositories.ProfilRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeService {

    private final EmployeRepository employeRepository;
    private final ProfilRepository profilRepository;

    public EmployeService(EmployeRepository employeRepository, ProfilRepository profilRepository) {
        this.employeRepository = employeRepository;
        this.profilRepository = profilRepository;
    }


    public EmployeResponseDTO createEmploye(EmployeCreateDTO dto) {
        if (employeRepository.findByMatricule(dto.getMatricule()) != null)
            throw new RuntimeException("Matricule déjà utilisé");
        if (employeRepository.findByLogin(dto.getLogin()) != null)
            throw new RuntimeException("Login déjà utilisé");

        Profil profil = profilRepository.findById(dto.getProfilId())
                .orElseThrow(() -> new RuntimeException("Profil non trouvé"));

        Employe employe = EmployeMapper.toEntity(dto, profil);
        return EmployeMapper.toDTO(employeRepository.save(employe));
    }


    public EmployeResponseDTO updateEmploye(int id, EmployeCreateDTO dto) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));

        Profil profil = profilRepository.findById(dto.getProfilId())
                .orElseThrow(() -> new RuntimeException("Profil non trouvé"));

        EmployeMapper.updateEntity(employe, dto, profil);
        return EmployeMapper.toDTO(employeRepository.save(employe));
    }


    public EmployeResponseDTO getById(int id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        return EmployeMapper.toDTO(employe);
    }


    public List<EmployeResponseDTO> getAll() {
        return employeRepository.findAll()
                .stream()
                .map(EmployeMapper::toDTO)
                .collect(Collectors.toList());
    }


    public void delete(int id) {
        employeRepository.deleteById(id);
    }


    public List<EmployeResponseDTO> search(String matricule, String login, String email) {
        return employeRepository.findAll().stream()
                .filter(e -> (matricule == null || e.getMatricule().equalsIgnoreCase(matricule)) &&
                        (login == null || e.getLogin().equalsIgnoreCase(login)) &&
                        (email == null || e.getEmail().equalsIgnoreCase(email)))
                .map(EmployeMapper::toDTO)
                .collect(Collectors.toList());
    }


    public List<EmployeResponseDTO> getDisponibles(LocalDate dateDebut, LocalDate dateFin) {
        return getAll();
    }
}