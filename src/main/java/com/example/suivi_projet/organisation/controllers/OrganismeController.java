package com.example.suivi_projet.organisation.controllers;

import com.example.suivi_projet.organisation.dto.OrganismeRequestDTO;
import com.example.suivi_projet.organisation.dto.OrganismeResponseDTO;
import com.example.suivi_projet.organisation.services.OrganismeService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organismes")
public class OrganismeController {

    private final OrganismeService organismeService;

    public OrganismeController(OrganismeService organismeService) {
        this.organismeService = organismeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrganismeResponseDTO addOrganisme(@Valid @RequestBody OrganismeRequestDTO dto) {
        return organismeService.addOrganisme(dto);
    }

    @GetMapping
    public List<OrganismeResponseDTO> getAllOrganismes() {
        return organismeService.getAllOrganismes();
    }

    @GetMapping("/{id}")
    public OrganismeResponseDTO getOrganismeById(@PathVariable Integer id) {
        return organismeService.getOrganismeById(id);
    }

    @PutMapping("/{id}")
    public OrganismeResponseDTO updateOrganisme(@PathVariable Integer id,
                                                @Valid @RequestBody OrganismeRequestDTO dto) {
        return organismeService.updateOrganisme(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganisme(@PathVariable Integer id) {
        organismeService.deleteOrganisme(id);
    }

    // 🔥 recherche
    @GetMapping("/search")
    public List<OrganismeResponseDTO> search(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String contact) {

        return organismeService.search(nom, code, contact);
    }
}