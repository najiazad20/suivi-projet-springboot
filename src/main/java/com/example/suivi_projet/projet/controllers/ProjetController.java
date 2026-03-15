package com.example.suivi_projet.projet.controllers;

import com.example.suivi_projet.projet.entities.Projet;
import com.example.suivi_projet.projet.services.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projets")
public class ProjetController {

    @Autowired
    private ProjetService projetService;

    // Créer ou sauvegarder un projet
    @PostMapping("/save")
    public ResponseEntity<Projet> save(@RequestBody Projet projet) {
        Projet savedProjet = projetService.save(projet);
        return new ResponseEntity<>(savedProjet, HttpStatus.CREATED);
    }

    // Supprimer un projet par ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean deleted = projetService.delete(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer tous les projets
    @GetMapping("/all")
    public ResponseEntity<List<Projet>> findAll() {
        List<Projet> projets = projetService.findAll();
        return new ResponseEntity<>(projets, HttpStatus.OK);
    }

    // Compter le nombre de projets
    @GetMapping("/count")
    public ResponseEntity<Long> countProjets() {
        long count = projetService.countProjets();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // Trouver un projet par code
    @GetMapping("/byCode/{code}")
    public ResponseEntity<Optional<Projet>> findByCode(@PathVariable String code) {
        Optional<Projet> projet = projetService.findByCode(code);
        return new ResponseEntity<>(projet, HttpStatus.OK);
    }

    // Trouver des projets par montant
    @GetMapping("/byMontant/{montant}")
    public ResponseEntity<List<Projet>> findByMontant(@PathVariable double montant) {
        List<Projet> projets = projetService.findByMontant(montant);
        return new ResponseEntity<>(projets, HttpStatus.OK);
    }
}