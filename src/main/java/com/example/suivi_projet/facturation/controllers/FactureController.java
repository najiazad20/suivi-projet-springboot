package com.example.suivi_projet.facturation.controllers;

import jakarta.validation.Valid;
import com.example.suivi_projet.facturation.dto.FactureRequestDTO;
import com.example.suivi_projet.facturation.dto.FactureResponseDTO;
import com.example.suivi_projet.facturation.services.FactureService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@PreAuthorize("hasRole('COMPTABLE')")
@RestController
@RequestMapping("/api")
public class FactureController {

    private final FactureService factureService;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }

    // CREATE
    @PostMapping("/phases/{phaseId}/facture")
    @ResponseStatus(HttpStatus.CREATED)
    public FactureResponseDTO createFacture(@PathVariable int phaseId,
                                            @Valid @RequestBody FactureRequestDTO dto) {
        return factureService.createFacture(phaseId, dto);
    }

    // GET ALL
    @GetMapping("/factures")
    public List<FactureResponseDTO> getAllFactures() {
        return factureService.getAllFactures();
    }

    // GET BY ID
    @GetMapping("/factures/{id}")
    public FactureResponseDTO getFactureById(@PathVariable int id) {
        return factureService.getFactureById(id);
    }

    // UPDATE
    @PutMapping("/factures/{id}")
    public FactureResponseDTO updateFacture(@PathVariable int id,
                                            @Valid @RequestBody FactureRequestDTO dto) {
        return factureService.updateFacture(id, dto);
    }

    // DELETE
    @DeleteMapping("/factures/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFacture(@PathVariable int id) {
        factureService.deleteFacture(id);
    }

    // SEARCH BY DATE
    @GetMapping("/factures/byDate")
    public List<FactureResponseDTO> getFacturesByDate(@RequestParam("date") LocalDate date) {
        return factureService.getFacturesByDate(date);
    }
    @GetMapping("/factures/byCode")
    public List<FactureResponseDTO> getFacturesByCode(@RequestParam String code) {
        return factureService.getFacturesByCode(code);
    }
}