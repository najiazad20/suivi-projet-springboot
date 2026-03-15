package com.example.suivi_projet.organisation.controllers;

import com.example.suivi_projet.organisation.dto.OrganismeCreateDTO;
import com.example.suivi_projet.organisation.dto.OrganismeResponseDTO;
import com.example.suivi_projet.organisation.entities.Organisme;
import com.example.suivi_projet.organisation.mappers.OrganismeMapper;
import com.example.suivi_projet.organisation.services.OrganismeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/organismes")
public class OrganismeController {

    @Autowired
    private OrganismeService service;

    @PostMapping
    public ResponseEntity<OrganismeResponseDTO> create(@Valid @RequestBody OrganismeCreateDTO dto) {
        Organisme org = OrganismeMapper.toEntity(dto);
        Organisme saved = service.save(org);
        return ResponseEntity.ok(OrganismeMapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganismeResponseDTO> update(@PathVariable int id,
                                                       @Valid @RequestBody OrganismeCreateDTO dto) {
        return service.findById(id)
                .map(existing -> {
                    existing.setCode(dto.getCode());
                    existing.setNom(dto.getNom());
                    existing.setAdresse(dto.getAdresse());
                    existing.setTelephone(dto.getTelephone());
                    existing.setNomContact(dto.getNomContact());
                    existing.setEmailContact(dto.getEmailContact());
                    existing.setSiteWeb(dto.getSiteWeb());
                    Organisme updated = service.save(existing);
                    return ResponseEntity.ok(OrganismeMapper.toDTO(updated));
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganismeResponseDTO> getById(@PathVariable int id) {
        return service.findById(id)
                .map(org -> ResponseEntity.ok(OrganismeMapper.toDTO(org)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<OrganismeResponseDTO> getAll() {
        return service.findAll().stream()
                .map(OrganismeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public List<OrganismeResponseDTO> search(@RequestParam(required = false) String nom,
                                             @RequestParam(required = false) String code,
                                             @RequestParam(required = false) String contact) {
        if(nom != null) return service.findByNom(nom).stream().map(OrganismeMapper::toDTO).collect(Collectors.toList());
        if(code != null) return service.findByCode(code).stream().map(OrganismeMapper::toDTO).collect(Collectors.toList());
        if(contact != null) return service.findByNomContact(contact).stream().map(OrganismeMapper::toDTO).collect(Collectors.toList());
        return service.findAll().stream().map(OrganismeMapper::toDTO).collect(Collectors.toList());
    }
}