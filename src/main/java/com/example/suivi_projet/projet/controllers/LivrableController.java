package com.example.suivi_projet.projet.controllers;

import com.example.suivi_projet.projet.dto.LivrableRequestDTO;
import com.example.suivi_projet.projet.dto.LivrableResponseDTO;
import com.example.suivi_projet.projet.services.LivrableService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('CHEF_PROJET', 'DIRECTEUR')")
@RequestMapping("/api")
public class LivrableController {

    private final LivrableService livrableService;

    public LivrableController(LivrableService livrableService) {
        this.livrableService = livrableService;
    }




    @GetMapping("/phases/{phaseId}/livrables")
    public List<LivrableResponseDTO> getByPhase(@PathVariable int phaseId) {
        return livrableService.getLivrablesByPhase(phaseId);
    }


    @PostMapping("/phases/{phaseId}/livrables")
    @ResponseStatus(HttpStatus.CREATED)
    public LivrableResponseDTO createLivrable(
            @PathVariable int phaseId,
            @Valid @RequestBody LivrableRequestDTO dto) {

        return livrableService.createLivrable(phaseId, dto);
    }




    @GetMapping("/livrables/{id}")
    public LivrableResponseDTO getById(@PathVariable int id) {
        return livrableService.getLivrableById(id);
    }


    @PutMapping("/livrables/{id}")
    public LivrableResponseDTO update(
            @PathVariable int id,
            @Valid @RequestBody LivrableRequestDTO dto) {

        return livrableService.updateLivrable(id, dto);
    }


    @DeleteMapping("/livrables/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        livrableService.deleteLivrable(id);
    }
}