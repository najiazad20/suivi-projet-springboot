package com.example.suivi_projet.projet.controllers;

import jakarta.validation.Valid;
import com.example.suivi_projet.projet.dto.PhaseRequestDTO;
import com.example.suivi_projet.projet.dto.PhaseResponseDTO;
import com.example.suivi_projet.projet.services.PhaseService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PhaseController {

    private final PhaseService phaseService;

    public PhaseController(PhaseService phaseService) {
        this.phaseService = phaseService;
    }



    // GET phases projet
    @PreAuthorize("hasRole('CHEF_PROJET')")
    @GetMapping("/projets/{projetId}/phases")
    public List<PhaseResponseDTO> getPhases(@PathVariable int projetId) {
        return phaseService.getPhasesByProjet(projetId);
    }
    // CREATE
    @PreAuthorize("hasRole('CHEF_PROJET')")
    @PostMapping("/projets/{projetId}/phases")
    @ResponseStatus(HttpStatus.CREATED)
    public PhaseResponseDTO addPhase(@PathVariable int projetId,
                                     @Valid @RequestBody PhaseRequestDTO dto) {
        return phaseService.addPhase(projetId, dto);
    }
    // GET by id
    @PreAuthorize("hasRole('CHEF_PROJET')")
    @GetMapping("/phases/{id}")
    public PhaseResponseDTO getPhase(@PathVariable int id) {
        return phaseService.getPhaseById(id);
    }

    // UPDATE
    @PreAuthorize("hasRole('CHEF_PROJET')")
    @PutMapping("/phases/{id}")
    public PhaseResponseDTO updatePhase(@PathVariable int id,
                                        @Valid @RequestBody PhaseRequestDTO dto) {
        return phaseService.updatePhase(id, dto);
    }

    // DELETE
    @PreAuthorize("hasRole('CHEF_PROJET')")
    @DeleteMapping("/phases/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePhase(@PathVariable int id) {
        phaseService.deletePhase(id);
    }

    // REALISATION
    @PreAuthorize("hasRole('CHEF_PROJET')")
    @PatchMapping("/phases/{id}/realisation")
    public PhaseResponseDTO realisation(@PathVariable int id) {
        return phaseService.setRealisation(id);
    }

    // FACTURATION
    @PreAuthorize("hasRole('COMPTABLE')")
    @PatchMapping("/phases/{id}/facturation")
    public PhaseResponseDTO facturation(@PathVariable int id) {
        return phaseService.setFacturation(id);
    }

    // PAIEMENT
    @PreAuthorize("hasRole('COMPTABLE')")
    @PatchMapping("/phases/{id}/paiement")
    public PhaseResponseDTO paiement(@PathVariable int id) {
        return phaseService.setPaiement(id);
    }

    // REPORTING
    @PreAuthorize("hasRole('COMPTABLE')")
    @GetMapping("/phases/terminees-non-facturees")
    public List<PhaseResponseDTO> termineesNonFacturees() {
        return phaseService.getTermineesNonFacturees();
    }
    @PreAuthorize("hasRole('COMPTABLE')")
    @GetMapping("/phases/facturees-non-payees")
    public List<PhaseResponseDTO> factureesNonPayees() {
        return phaseService.getFactureesNonPayees();
    }
}