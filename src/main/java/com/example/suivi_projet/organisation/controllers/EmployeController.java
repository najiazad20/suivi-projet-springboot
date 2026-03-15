package com.example.suivi_projet.organisation.controllers;

import com.example.suivi_projet.organisation.dto.EmployeCreateDTO;
import com.example.suivi_projet.organisation.dto.EmployeResponseDTO;
import com.example.suivi_projet.organisation.services.EmployeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/employes")
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    // créer employé
    @PostMapping("/save")
    public ResponseEntity<EmployeResponseDTO> save(@Valid @RequestBody EmployeCreateDTO dto) {

        EmployeResponseDTO employe = employeService.save(dto);

        return new ResponseEntity<>(employe, HttpStatus.CREATED);
    }

    // modifier employé
    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeResponseDTO> update(
            @PathVariable int id,
            @Valid @RequestBody EmployeCreateDTO dto) {

        EmployeResponseDTO employe = employeService.update(id, dto);

        return new ResponseEntity<>(employe, HttpStatus.OK);
    }

    // trouver par id
    @GetMapping("/{id}")
    public ResponseEntity<EmployeResponseDTO> findById(@PathVariable int id) {

        EmployeResponseDTO employe = employeService.findById(id);

        return new ResponseEntity<>(employe, HttpStatus.OK);
    }

    // tous les employés
    @GetMapping("/all")
    public ResponseEntity<List<EmployeResponseDTO>> findAll() {

        List<EmployeResponseDTO> employes = employeService.findAll();

        return new ResponseEntity<>(employes, HttpStatus.OK);
    }

    // supprimer
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {

        boolean deleted = employeService.delete(id);

        if (deleted)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // recherche
    @GetMapping("/search")
    public ResponseEntity<List<EmployeResponseDTO>> search(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) String email) {

        List<EmployeResponseDTO> result = employeService.search(matricule, login, email);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // employés disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<EmployeResponseDTO>> getDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {

        List<EmployeResponseDTO> employes = employeService.getDisponibles(dateDebut, dateFin);

        return new ResponseEntity<>(employes, HttpStatus.OK);
    }
}