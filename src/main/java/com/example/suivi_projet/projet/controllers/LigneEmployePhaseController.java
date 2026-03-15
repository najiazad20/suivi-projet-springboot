package com.example.suivi_projet.projet.controllers;

import com.example.suivi_projet.projet.dto.LigneEmployePhaseCreateDTO;
import com.example.suivi_projet.projet.dto.LigneEmployePhaseResponseDTO;
import com.example.suivi_projet.projet.services.LigneEmployePhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LigneEmployePhaseController {

    @Autowired
    private LigneEmployePhaseService service;

    @PostMapping("/phases/{phaseId}/employes/{employeId}")
    public ResponseEntity<LigneEmployePhaseResponseDTO> create(
            @PathVariable int phaseId,
            @PathVariable int employeId,
            @RequestBody LigneEmployePhaseCreateDTO dto) {
        return new ResponseEntity<>(service.create(phaseId, employeId, dto), HttpStatus.CREATED);
    }

    @GetMapping("/phases/{phaseId}/employes")
    public ResponseEntity<List<LigneEmployePhaseResponseDTO>> getEmployesByPhase(@PathVariable int phaseId) {
        return new ResponseEntity<>(service.getEmployesByPhase(phaseId), HttpStatus.OK);
    }

    @GetMapping("/phases/{phaseId}/employes/{employeId}")
    public ResponseEntity<LigneEmployePhaseResponseDTO> get(
            @PathVariable int phaseId,
            @PathVariable int employeId) {
        return new ResponseEntity<>(service.get(phaseId, employeId), HttpStatus.OK);
    }

    @PutMapping("/phases/{phaseId}/employes/{employeId}")
    public ResponseEntity<LigneEmployePhaseResponseDTO> update(
            @PathVariable int phaseId,
            @PathVariable int employeId,
            @RequestBody LigneEmployePhaseCreateDTO dto) {
        return new ResponseEntity<>(service.update(phaseId, employeId, dto), HttpStatus.OK);
    }

    @DeleteMapping("/phases/{phaseId}/employes/{employeId}")
    public ResponseEntity<Void> delete(
            @PathVariable int phaseId,
            @PathVariable int employeId) {
        boolean deleted = service.delete(phaseId, employeId);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/employes/{employeId}/phases")
    public ResponseEntity<List<LigneEmployePhaseResponseDTO>> getPhasesByEmploye(@PathVariable int employeId) {
        return new ResponseEntity<>(service.getPhasesByEmploye(employeId), HttpStatus.OK);
    }
}
