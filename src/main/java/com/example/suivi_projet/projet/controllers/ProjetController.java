package com.example.suivi_projet.projet.controllers;

import jakarta.validation.Valid;
import com.example.suivi_projet.projet.dto.ProjetRequestDTO;
import com.example.suivi_projet.projet.dto.ProjetResponseDTO;
import com.example.suivi_projet.projet.services.ProjetService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@PreAuthorize("hasAnyRole('SECRETAIRE','DIRECTEUR')")
@RestController
@RequestMapping("/api/projets")
public class ProjetController {

    private final ProjetService projetService;

    public ProjetController(ProjetService projetService) {
        this.projetService = projetService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjetResponseDTO addProjet(@Valid @RequestBody ProjetRequestDTO dto) {
        return projetService.addProjet(dto);
    }

    // GET ALL
    @GetMapping
    public List<ProjetResponseDTO> getAllProjets() {
        return projetService.getAllProjets();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ProjetResponseDTO getProjetById(@PathVariable int id) {
        return projetService.getProjetById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ProjetResponseDTO updateProjet(@PathVariable int id,
                                          @Valid @RequestBody ProjetRequestDTO dto) {
        return projetService.updateProjet(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjet(@PathVariable int id) {
        projetService.deleteProjet(id);
    }

    // FIND BY CODE
    @GetMapping("/code/{code}")
    public ProjetResponseDTO getByCode(@PathVariable String code) {
        return projetService.getByCode(code);
    }

    // FIND BY MONTANT
    @GetMapping("/montant/{montant}")
    public List<ProjetResponseDTO> getByMontant(@PathVariable double montant) {
        return projetService.getByMontant(montant);
    }
}