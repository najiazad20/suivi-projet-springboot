package com.example.suivi_projet.organisation.controllers;





import com.example.suivi_projet.organisation.dto.EmployeCreateDTO;
import com.example.suivi_projet.organisation.dto.EmployeResponseDTO;
import com.example.suivi_projet.organisation.services.EmployeService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employes")
public class EmployeController {

    private final EmployeService employeService;

    public EmployeController(EmployeService employeService) {
        this.employeService = employeService;
    }

    @PostMapping
    public ResponseEntity<EmployeResponseDTO> create(@Valid @RequestBody EmployeCreateDTO dto) {
        return ResponseEntity.ok(employeService.createEmploye(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeResponseDTO> update(@PathVariable int id,
                                                     @Valid @RequestBody EmployeCreateDTO dto) {
        return ResponseEntity.ok(employeService.updateEmploye(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeResponseDTO> getById(@PathVariable int id) {
        return ResponseEntity.ok(employeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<EmployeResponseDTO>> getAll(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) String email) {

        if (matricule != null || login != null || email != null) {
            return ResponseEntity.ok(employeService.search(matricule, login, email));
        }

        return ResponseEntity.ok(employeService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        employeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<EmployeResponseDTO>> getDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(employeService.getDisponibles(dateDebut, dateFin));
    }
}