package com.example.suivi_projet.projet.controllers;

import com.example.suivi_projet.projet.dto.LivrableRequestDTO;
import com.example.suivi_projet.projet.dto.LivrableResponseDTO;
import com.example.suivi_projet.projet.services.LivrableService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LivrableController {

    private final LivrableService livrableService;

    public LivrableController(LivrableService livrableService) {
        this.livrableService = livrableService;
    }

    // POST /api/phases/{phaseId}/livrables
    @PostMapping("/phases/{phaseId}/livrables")
    @ResponseStatus(HttpStatus.CREATED)
    public LivrableResponseDTO createLivrable(
            @PathVariable int phaseId,
            @Valid @RequestBody LivrableRequestDTO dto) {

        return livrableService.createLivrable(phaseId, dto);
    }

    // GET /api/phases/{phaseId}/livrables
    @GetMapping("/phases/{phaseId}/livrables")
    public List<LivrableResponseDTO> getByPhase(@PathVariable int phaseId) {
        return livrableService.getLivrablesByPhase(phaseId);
    }

    // GET /api/livrables/{id}
    @GetMapping("/livrables/{id}")
    public LivrableResponseDTO getById(@PathVariable int id) {
        return livrableService.getLivrableById(id);
    }

    // PUT /api/livrables/{id}
    @PutMapping("/livrables/{id}")
    public LivrableResponseDTO update(
            @PathVariable int id,
            @Valid @RequestBody LivrableRequestDTO dto) {

        return livrableService.updateLivrable(id, dto);
    }

    // DELETE /api/livrables/{id}
    @DeleteMapping("/livrables/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        livrableService.deleteLivrable(id);
    }
}