package com.example.suivi_projet.organisation.controllers;

import com.example.suivi_projet.organisation.dto.*;
import com.example.suivi_projet.organisation.services.EmployeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employes")
@CrossOrigin("*")
public class EmployeController {

    private final EmployeService service;

    public EmployeController(EmployeService service) {
        this.service = service;
    }
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PostMapping
    public EmployeResponseDTO save(@Valid @RequestBody EmployeRequestDTO dto) {
        return service.save(dto);
    }
    @PreAuthorize("hasAnyRole('ADMINISTRATEUR', 'DIRECTEUR', 'CHEF_PROJET')")
    @GetMapping
    public List<EmployeResponseDTO> findAll(@RequestParam(required = false) String nom) {
        return service.findAll(nom);
    }
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @GetMapping("/{id}")
    public EmployeResponseDTO findById(@PathVariable int id) {
        return service.findById(id);
    }
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @PutMapping("/{id}")
    public EmployeResponseDTO update(@PathVariable int id,
                                     @Valid @RequestBody EmployeRequestDTO dto) {
        return service.update(id, dto);
    }
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.delete(id);
    }
    @PreAuthorize("hasRole('CHEF_PROJET')")
    @GetMapping("/disponibles")
    public List<EmployeResponseDTO> disponibles(
            @RequestParam LocalDate dateDebut,
            @RequestParam LocalDate dateFin) {

        return service.getDisponibles(dateDebut, dateFin);
    }
}