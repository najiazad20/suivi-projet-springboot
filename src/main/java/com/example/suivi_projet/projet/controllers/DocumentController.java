package com.example.suivi_projet.projet.controllers;

import com.example.suivi_projet.projet.entities.Document;
import com.example.suivi_projet.projet.services.DocumentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/api/projets/{projetId}/documents")
    public ResponseEntity<Document> save(@PathVariable int projetId,
                                         @RequestBody Document document){

        Document doc = documentService.save(projetId,document);

        return new ResponseEntity<>(doc,HttpStatus.CREATED);
    }

    @GetMapping("/api/projets/{projetId}/documents")
    public ResponseEntity<List<Document>> findByProjet(@PathVariable int projetId){

        List<Document> docs = documentService.findByProjet(projetId);

        return new ResponseEntity<>(docs,HttpStatus.OK);
    }

    @GetMapping("/api/documents/{id}")
    public ResponseEntity<Document> findById(@PathVariable int id){

        Document doc = documentService.findById(id);

        return new ResponseEntity<>(doc,HttpStatus.OK);
    }

    @PutMapping("/api/documents/{id}")
    public ResponseEntity<Document> update(@PathVariable int id,
                                           @RequestBody Document document){

        Document doc = documentService.update(id,document);

        return new ResponseEntity<>(doc,HttpStatus.OK);
    }

    @DeleteMapping("/api/documents/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){

        boolean deleted = documentService.delete(id);

        if(deleted)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/documents/{id}/download")
    public ResponseEntity<String> download(@PathVariable int id){

        Document doc = documentService.findById(id);

        return new ResponseEntity<>(doc.getChemin(),HttpStatus.OK);
    }
    // nouvelle API recherche par libelle
    @GetMapping("/api/documents/search")
    public ResponseEntity<List<Document>> findByLibelle(@RequestParam String libelle){

        List<Document> docs = documentService.findByLibelle(libelle);

        return new ResponseEntity<>(docs,HttpStatus.OK);
    }
}