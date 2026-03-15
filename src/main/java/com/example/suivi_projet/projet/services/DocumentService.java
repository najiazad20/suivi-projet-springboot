package com.example.suivi_projet.projet.services;

import com.example.suivi_projet.projet.entities.Document;
import com.example.suivi_projet.projet.entities.Projet;
import com.example.suivi_projet.projet.repositories.DocumentRepository;
import com.example.suivi_projet.projet.repositories.ProjetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ProjetRepository projetRepository;

    public Document save(int projetId, Document document){

        Projet projet = projetRepository.findById(projetId).orElse(null);

        document.setProjet(projet);

        return documentRepository.save(document);
    }

    public List<Document> findByProjet(int projetId){

        return documentRepository.findByProjetId(projetId);
    }

    public Document findById(int id){
        return documentRepository.findById(id).orElse(null);
    }

    public Document update(int id, Document doc){

        Document d = documentRepository.findById(id).orElse(null);

        if(d != null){
            d.setLibelle(doc.getLibelle());
            d.setDescription(doc.getDescription());
            d.setChemin(doc.getChemin());

            return documentRepository.save(d);
        }

        return null;
    }

    public boolean delete(int id){

        Document doc = documentRepository.findById(id).orElse(null);

        if(doc != null){
            documentRepository.delete(doc);
            return true;
        }

        return false;
    }
    public List<Document> findByLibelle(String libelle){
        return documentRepository.findByLibelle(libelle);
    }

}