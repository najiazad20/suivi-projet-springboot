package com.example.suivi_projet.facturation.controllers;

import com.example.suivi_projet.facturation.dto.FactureDTO;
import com.example.suivi_projet.facturation.entities.Facture;
import com.example.suivi_projet.facturation.mappers.FactureMapper;
import com.example.suivi_projet.facturation.services.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FactureController {

    @Autowired
    private FactureService factureService;

    @PostMapping("/phases/{phaseId}/facture")
    public ResponseEntity<FactureDTO> createFacture(@PathVariable int phaseId,
                                                    @RequestBody FactureDTO dto) {
        Facture facture = FactureMapper.toEntity(dto, null); // Phase sera attachée dans service
        Facture created = factureService.createFacture(phaseId, facture);
        return new ResponseEntity<>(FactureMapper.toDTO(created), HttpStatus.CREATED);
    }

    @GetMapping("/factures")
    public ResponseEntity<List<FactureDTO>> getAllFactures() {
        List<FactureDTO> list = factureService.getAllFactures()
                .stream()
                .map(FactureMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/factures/{id}")
    public ResponseEntity<FactureDTO> getFacture(@PathVariable int id) {
        Facture facture = factureService.getFactureById(id);
        return new ResponseEntity<>(FactureMapper.toDTO(facture), HttpStatus.OK);
    }

    @PutMapping("/factures/{id}")
    public ResponseEntity<FactureDTO> updateFacture(@PathVariable int id,
                                                    @RequestBody FactureDTO dto) {
        Facture factureDetails = FactureMapper.toEntity(dto, null); // Phase non modifiable
        Facture updated = factureService.updateFacture(id, factureDetails);
        return new ResponseEntity<>(FactureMapper.toDTO(updated), HttpStatus.OK);
    }

    @DeleteMapping("/factures/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable int id) {
        factureService.deleteFacture(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/factures/byDate")
    public ResponseEntity<List<FactureDTO>> getFacturesByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        List<FactureDTO> list = factureService.getFacturesByDate(date)
                .stream()
                .map(FactureMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}