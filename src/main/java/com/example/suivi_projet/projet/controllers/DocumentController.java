package com.example.suivi_projet.projet.controllers;

import com.example.suivi_projet.projet.dto.DocumentRequestDTO;
import com.example.suivi_projet.projet.dto.DocumentResponseDTO;
import com.example.suivi_projet.projet.services.DocumentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/projets/{projetId}/documents")
    public ResponseEntity<DocumentResponseDTO> save(
            @PathVariable int projetId,
            @Valid @RequestBody DocumentRequestDTO dto) {

        DocumentRequestDTO dtoWithProjetId = new DocumentRequestDTO(
                dto.code(), dto.libelle(), dto.description(), dto.chemin(), projetId
        );

        return new ResponseEntity<>(documentService.save(dtoWithProjetId), HttpStatus.CREATED);
    }

    @GetMapping("/projets/{projetId}/documents")
    public ResponseEntity<List<DocumentResponseDTO>> findByProjet(@PathVariable int projetId) {
        return new ResponseEntity<>(documentService.findByProjet(projetId), HttpStatus.OK);
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<DocumentResponseDTO> findById(@PathVariable int id) {
        return ResponseEntity.of(java.util.Optional.ofNullable(documentService.findById(id)));
    }

    @PutMapping("/documents/{id}")
    public ResponseEntity<DocumentResponseDTO> update(
            @PathVariable int id,
            @Valid @RequestBody DocumentRequestDTO dto) {

        return ResponseEntity.of(java.util.Optional.ofNullable(documentService.update(id, dto)));
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return documentService.delete(id)
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/documents/search")
    public ResponseEntity<List<DocumentResponseDTO>> findByLibelle(@RequestParam String libelle) {
        return new ResponseEntity<>(documentService.findByLibelle(libelle), HttpStatus.OK);
    }
    @GetMapping("/documents/{id}/download")
    public ResponseEntity<String> download(@PathVariable int id) {
        return ResponseEntity.of(
                java.util.Optional.ofNullable(documentService.findById(id))
                        .map(DocumentResponseDTO::chemin)
        );
    }


}