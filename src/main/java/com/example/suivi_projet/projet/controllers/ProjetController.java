package com.example.suivi_projet.projet.controllers;

import jakarta.validation.Valid;
import com.example.suivi_projet.projet.dto.ProjetRequestDTO;
import com.example.suivi_projet.projet.dto.ProjetResponseDTO;
import com.example.suivi_projet.projet.services.ProjetService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
public class ProjetController {

    private final ProjetService projetService;

    public ProjetController(ProjetService projetService) {
        this.projetService = projetService;
    }

    // CREATE
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'DIRECTEUR')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjetResponseDTO addProjet(@Valid @RequestBody ProjetRequestDTO dto) {
        return projetService.addProjet(dto);
    }

    // GET ALL
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'DIRECTEUR', 'CHEF_PROJET', 'COMPTABLE')")
    @GetMapping
    public List<ProjetResponseDTO> getAllProjets() {
        return projetService.getAllProjets();
    }

    // GET BY ID
    @PreAuthorize("authenticated()")
    @GetMapping("/{id}")
    public ProjetResponseDTO getProjetById(@PathVariable int id) {
        return projetService.getProjetById(id);
    }

    // UPDATE
    @PreAuthorize("hasAnyRole( 'DIRECTEUR')")
    @PutMapping("/{id}")
    public ProjetResponseDTO updateProjet(@PathVariable int id,
                                          @Valid @RequestBody ProjetRequestDTO dto) {
        return projetService.updateProjet(id, dto);
    }
    @PreAuthorize("hasRole('DIRECTEUR')")
    @PatchMapping("/{id}/montant")
    public ProjetResponseDTO updateMontant(@PathVariable int id, @RequestParam double montant) {
        return projetService.updateMontant(id, montant);
    }
    // DELETE
    @PreAuthorize("hasRole('DIRECTEUR')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjet(@PathVariable int id) {
        projetService.deleteProjet(id);
    }

    // FIND BY CODE
    @PreAuthorize("authenticated()")
    @GetMapping("/code/{code}")
    public ProjetResponseDTO getByCode(@PathVariable String code) {
        return projetService.getByCode(code);
    }

    // FIND BY MONTANT
    @PreAuthorize("authenticated()")
    @GetMapping("/montant/{montant}")
    public List<ProjetResponseDTO> getByMontant(@PathVariable double montant) {
        return projetService.getByMontant(montant);
    }
    @PreAuthorize("hasRole('DIRECTEUR')")
    @PatchMapping("/{id}/affecter-chef")
    public ProjetResponseDTO affecterChef(@PathVariable int id, @RequestParam int chefId) {
        return projetService.affecterChef(id, chefId);
    }
}