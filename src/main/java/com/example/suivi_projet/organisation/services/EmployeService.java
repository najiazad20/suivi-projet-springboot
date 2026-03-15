package com.example.suivi_projet.organisation.services;

import com.example.suivi_projet.organisation.dto.EmployeCreateDTO;
import com.example.suivi_projet.organisation.dto.EmployeResponseDTO;
import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.organisation.entities.Profil;
import com.example.suivi_projet.organisation.mappers.EmployeMapper;
import com.example.suivi_projet.organisation.repositories.EmployeRepository;
import com.example.suivi_projet.organisation.repositories.ProfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeService {

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private ProfilRepository profilRepository;


    // créer employé
    public EmployeResponseDTO save(EmployeCreateDTO dto) {

        if (employeRepository.findByMatricule(dto.getMatricule()) != null)
            return null;

        if (employeRepository.findByLogin(dto.getLogin()) != null)
            return null;

        Optional<Profil> profilOptional = profilRepository.findById(dto.getProfilId());

        if (!profilOptional.isPresent())
            return null;

        Profil profil = profilOptional.get();

        Employe employe = EmployeMapper.toEntity(dto, profil);

        Employe saved = employeRepository.save(employe);

        return EmployeMapper.toDTO(saved);
    }


    // modifier employé
    public EmployeResponseDTO update(int id, EmployeCreateDTO dto) {

        Optional<Employe> employeOptional = employeRepository.findById(id);

        if (!employeOptional.isPresent())
            return null;

        Optional<Profil> profilOptional = profilRepository.findById(dto.getProfilId());

        if (!profilOptional.isPresent())
            return null;

        Employe employe = employeOptional.get();
        Profil profil = profilOptional.get();

        EmployeMapper.updateEntity(employe, dto, profil);

        Employe updated = employeRepository.save(employe);

        return EmployeMapper.toDTO(updated);
    }


    // trouver par id
    public EmployeResponseDTO findById(int id) {

        Optional<Employe> employeOptional = employeRepository.findById(id);

        if (!employeOptional.isPresent())
            return null;

        return EmployeMapper.toDTO(employeOptional.get());
    }


    // tous les employés
    public List<EmployeResponseDTO> findAll() {

        return employeRepository.findAll()
                .stream()
                .map(EmployeMapper::toDTO)
                .collect(Collectors.toList());
    }


    // supprimer
    public boolean delete(int id) {

        Optional<Employe> employeOptional = employeRepository.findById(id);

        if (employeOptional.isPresent()) {
            employeRepository.delete(employeOptional.get());
            return true;
        }

        return false;
    }


    // recherche
    public List<EmployeResponseDTO> search(String matricule, String login, String email) {

        return employeRepository.findAll().stream()
                .filter(e ->
                        (matricule == null || e.getMatricule().equalsIgnoreCase(matricule)) &&
                                (login == null || e.getLogin().equalsIgnoreCase(login)) &&
                                (email == null || e.getEmail().equalsIgnoreCase(email))
                )
                .map(EmployeMapper::toDTO)
                .collect(Collectors.toList());
    }


    // employés disponibles (exemple simple)
    public List<EmployeResponseDTO> getDisponibles(LocalDate dateDebut, LocalDate dateFin) {

        return findAll();
    }
}