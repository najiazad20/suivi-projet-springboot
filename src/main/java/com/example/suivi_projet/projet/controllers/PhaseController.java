package com.example.suivi_projet.projet.controllers;

import com.example.suivi_projet.projet.entities.Phase;
import com.example.suivi_projet.projet.services.PhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PhaseController {

    @Autowired
    private PhaseService phaseService;

    // créer phase
    @PostMapping("/projets/{projetId}/phases")
    public ResponseEntity<Phase> createPhase(@PathVariable int projetId,
                                             @RequestBody Phase phase) {

        Phase p = phaseService.createPhase(projetId, phase);

        if (p == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }

    // phases d'un projet
    @GetMapping("/projets/{projetId}/phases")
    public ResponseEntity<List<Phase>> findByProjet(@PathVariable int projetId) {

        List<Phase> phases = phaseService.findByProjet(projetId);
        return new ResponseEntity<>(phases, HttpStatus.OK);
    }

    // phase par id
    @GetMapping("/phases/{id}")
    public ResponseEntity<Optional<Phase>> findById(@PathVariable int id) {

        Optional<Phase> phase = phaseService.findById(id);
        return new ResponseEntity<>(phase, HttpStatus.OK);
    }

    // modifier phase
    @PutMapping("/phases/{id}")
    public ResponseEntity<Phase> update(@PathVariable int id,
                                        @RequestBody Phase phase) {

        Phase updated = phaseService.update(id, phase);

        if (updated == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // supprimer phase
    @DeleteMapping("/phases/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {

        boolean deleted = phaseService.delete(id);

        if (deleted)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // état réalisation
    @PatchMapping("/phases/{id}/realisation")
    public ResponseEntity<Phase> realisation(@PathVariable int id) {

        Phase phase = phaseService.realisation(id);
        return new ResponseEntity<>(phase, HttpStatus.OK);
    }

    // état facturation
    @PatchMapping("/phases/{id}/facturation")
    public ResponseEntity<Phase> facturation(@PathVariable int id) {

        Phase phase = phaseService.facturation(id);
        return new ResponseEntity<>(phase, HttpStatus.OK);
    }

    // état paiement
    @PatchMapping("/phases/{id}/paiement")
    public ResponseEntity<Phase> paiement(@PathVariable int id) {

        Phase phase = phaseService.paiement(id);
        return new ResponseEntity<>(phase, HttpStatus.OK);
    }
}