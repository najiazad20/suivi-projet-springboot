package com.example.suivi_projet.organisation.controllers;

import com.example.suivi_projet.organisation.dto.ProfilRequestDTO;
import com.example.suivi_projet.organisation.dto.ProfilResponseDTO;
import com.example.suivi_projet.organisation.services.ProfilService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@PreAuthorize("hasRole('ADMINISTRATEUR')")
@RestController
@RequestMapping("/api/profils")
public class ProfilController {

    private final ProfilService profilService;

    public ProfilController(ProfilService profilService) {
        this.profilService = profilService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfilResponseDTO save(@Valid @RequestBody ProfilRequestDTO dto) {

        return profilService.save(dto);
    }

    // GET ALL
    @GetMapping
    public List<ProfilResponseDTO> findAll() {

        return profilService.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ProfilResponseDTO findById(@PathVariable int id) {

        return profilService.findById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ProfilResponseDTO update(@PathVariable int id,
                                    @Valid @RequestBody ProfilRequestDTO dto) {

        return profilService.update(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {

        profilService.delete(id);
    }

    // FIND BY CODE
    @GetMapping("/search")
    public ProfilResponseDTO findByCode(@RequestParam String code) {

        return profilService.findByCode(code);
    }
}