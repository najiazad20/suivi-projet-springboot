package com.example.suivi_projet.projet.controllers;

import jakarta.validation.Valid;
import com.example.suivi_projet.projet.dto.*;
import com.example.suivi_projet.projet.services.LigneEmployePhaseService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@PreAuthorize("hasRole('CHEF_PROJET')")
@RestController
@RequestMapping("/api")
public class LigneEmployePhaseController {

    private final LigneEmployePhaseService service;

    public LigneEmployePhaseController(LigneEmployePhaseService service) {
        this.service = service;
    }

    @PostMapping("/phases/{phaseId}/employes/{employeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public LigneEmployePhaseResponseDTO create(
            @PathVariable int phaseId,
            @PathVariable int employeId,
            @Valid @RequestBody LigneEmployePhaseRequestDTO dto) {

        return service.create(phaseId, employeId, dto);
    }

    @GetMapping("/phases/{phaseId}/employes")
    public List<LigneEmployePhaseResponseDTO> getEmployes(@PathVariable int phaseId) {
        return service.getEmployesByPhase(phaseId);
    }

    @GetMapping("/phases/{phaseId}/employes/{employeId}")
    public LigneEmployePhaseResponseDTO get(
            @PathVariable int phaseId,
            @PathVariable int employeId) {

        return service.get(phaseId, employeId);
    }



    @DeleteMapping("/phases/{phaseId}/employes/{employeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable int phaseId,
            @PathVariable int employeId) {

        service.delete(phaseId, employeId);
    }
    @PutMapping("/phases/{phaseId}/employes/{employeId}")
    public LigneEmployePhaseResponseDTO update(
            @PathVariable int phaseId,
            @PathVariable int employeId,
            @Valid @RequestBody LigneEmployePhaseRequestDTO dto) {

        return service.update(phaseId, employeId, dto);
    }
    @GetMapping("/employes/{employeId}/phases")
    public List<LigneEmployePhaseResponseDTO> getPhases(@PathVariable int employeId) {
        return service.getPhasesByEmploye(employeId);
    }
}