package com.example.suivi_projet.organisation.services;

import com.example.suivi_projet.organisation.dto.OrganismeRequestDTO;
import com.example.suivi_projet.organisation.dto.OrganismeResponseDTO;
import com.example.suivi_projet.organisation.entities.Organisme;
import com.example.suivi_projet.organisation.mappers.OrganismeMapper;
import com.example.suivi_projet.organisation.repositories.OrganismeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganismeService {

    private final OrganismeRepository organismeRepository;
    private final OrganismeMapper organismeMapper;

    public OrganismeService(OrganismeRepository organismeRepository,
                            OrganismeMapper organismeMapper) {
        this.organismeRepository = organismeRepository;
        this.organismeMapper = organismeMapper;
    }

    public OrganismeResponseDTO addOrganisme(OrganismeRequestDTO dto) {

        Organisme organisme = organismeMapper.toEntity(dto);
        Organisme saved = organismeRepository.save(organisme);

        return organismeMapper.toDTO(saved);
    }

    public List<OrganismeResponseDTO> getAllOrganismes() {

        return organismeRepository.findAll()
                .stream()
                .map(organismeMapper::toDTO)
                .toList();
    }

    public OrganismeResponseDTO getOrganismeById(Integer id) {

        Organisme organisme = organismeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organisme introuvable avec id : " + id));

        return organismeMapper.toDTO(organisme);
    }

    public OrganismeResponseDTO updateOrganisme(Integer id, OrganismeRequestDTO dto) {

        Organisme organisme = organismeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organisme introuvable avec id : " + id));

        organismeMapper.updateEntityFromDTO(dto, organisme);

        Organisme updated = organismeRepository.save(organisme);

        return organismeMapper.toDTO(updated);
    }

    public void deleteOrganisme(Integer id) {

        Organisme organisme = organismeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organisme introuvable avec id : " + id));

        organismeRepository.delete(organisme);
    }

    // 🔥 Recherche demandée (nom / code / contact)
    public List<OrganismeResponseDTO> search(String nom, String code, String contact) {

        if (nom != null) {
            return organismeRepository.findByNom(nom)
                    .stream()
                    .map(organismeMapper::toDTO)
                    .toList();
        }

        if (code != null) {
            return organismeRepository.findByCode(code)
                    .stream()
                    .map(organismeMapper::toDTO)
                    .toList();
        }

        if (contact != null) {
            return organismeRepository.findByNomContact(contact)
                    .stream()
                    .map(organismeMapper::toDTO)
                    .toList();
        }

        return getAllOrganismes();
    }
}