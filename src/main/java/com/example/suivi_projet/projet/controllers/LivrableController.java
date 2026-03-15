package com.example.suivi_projet.projet.controllers;

import com.example.suivi_projet.projet.dto.LivrableDTO;
import com.example.suivi_projet.projet.entities.Livrable;
import com.example.suivi_projet.projet.services.LivrableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LivrableController {

    @Autowired
    private LivrableService livrableService;

    // POST /api/phases/{phaseId}/livrables
    @PostMapping("/phases/{phaseId}/livrables")
    public Livrable createLivrable(@PathVariable int phaseId, @RequestBody LivrableDTO dto){
        Livrable livrable = new Livrable();
        livrable.setCode(dto.getCode());
        livrable.setLibelle(dto.getLibelle());
        livrable.setDescription(dto.getDescription());
        livrable.setChemin(dto.getChemin());
        return livrableService.save(phaseId, livrable);
    }

    // GET /api/phases/{phaseId}/livrables
    @GetMapping("/phases/{phaseId}/livrables")
    public List<Livrable> getLivrablesByPhase(@PathVariable int phaseId){
        return livrableService.findByPhase(phaseId);
    }

    // GET /api/livrables/{id}
    @GetMapping("/livrables/{id}")
    public Livrable getLivrable(@PathVariable int id){
        return livrableService.findById(id);
    }

    // PUT /api/livrables/{id}
    @PutMapping("/livrables/{id}")
    public Livrable updateLivrable(@PathVariable int id, @RequestBody LivrableDTO dto){
        Livrable livrable = new Livrable();
        livrable.setCode(dto.getCode());
        livrable.setLibelle(dto.getLibelle());
        livrable.setDescription(dto.getDescription());
        livrable.setChemin(dto.getChemin());
        return livrableService.update(id, livrable);
    }

    // DELETE /api/livrables/{id}
    @DeleteMapping("/livrables/{id}")
    public boolean deleteLivrable(@PathVariable int id){
        return livrableService.delete(id);
    }

    // Optionnel: upload d’un fichier pour le livrable
    @PostMapping("/phases/{phaseId}/livrables/upload")
    public Livrable uploadFile(@PathVariable int phaseId, @RequestParam("file") MultipartFile file) {
        // ici tu peux ajouter la logique pour enregistrer le fichier sur disque ou serveur
        Livrable livrable = new Livrable();
        livrable.setCode(file.getOriginalFilename());
        livrable.setLibelle(file.getOriginalFilename());
        livrable.setChemin("/uploads/" + file.getOriginalFilename());
        livrable.setDescription("Fichier uploadé");
        return livrableService.save(phaseId, livrable);
    }
}