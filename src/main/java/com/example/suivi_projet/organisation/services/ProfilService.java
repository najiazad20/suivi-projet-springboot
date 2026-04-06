package com.example.suivi_projet.organisation.services;

import com.example.suivi_projet.organisation.dto.ProfilRequestDTO;
import com.example.suivi_projet.organisation.dto.ProfilResponseDTO;
import com.example.suivi_projet.organisation.entities.Profil;
import com.example.suivi_projet.organisation.mappers.ProfilMapper;
import com.example.suivi_projet.organisation.repositories.ProfilRepository;
import com.example.suivi_projet.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfilService {

    private final ProfilRepository profilRepository;
    private final ProfilMapper mapper;

    public ProfilService(ProfilRepository profilRepository, ProfilMapper mapper) {
        this.profilRepository = profilRepository;
        this.mapper = mapper;
    }

    public ProfilResponseDTO save(ProfilRequestDTO dto) {

        Profil profil = mapper.toEntity(dto);

        return mapper.toDTO(profilRepository.save(profil));
    }


    // GET BY ID
    public ProfilResponseDTO findById(int id) {

        Profil profil = profilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profil introuvable"));

        return mapper.toDTO(profil);
    }
    // GET ALL
    public List<ProfilResponseDTO> findAll() {

        return profilRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    // UPDATE
    public ProfilResponseDTO update(int id, ProfilRequestDTO dto) {

        Profil profil = profilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profil introuvable"));

        mapper.updateEntity(profil, dto);

        return mapper.toDTO(profilRepository.save(profil));
    }

    // DELETE
    public void delete(int id) {

        Profil profil = profilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profil introuvable"));

        profilRepository.delete(profil);
    }

    // FIND BY CODE
    public ProfilResponseDTO findByCode(String code) {

        Profil profil = profilRepository.findByCode(code);

        if (profil == null) {
            throw new ResourceNotFoundException("Profil introuvable");
        }

        return mapper.toDTO(profil);
    }
}