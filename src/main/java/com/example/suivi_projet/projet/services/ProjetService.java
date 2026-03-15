package com.example.suivi_projet.projet.services;

import com.example.suivi_projet.projet.entities.Projet;
import com.example.suivi_projet.projet.repositories.ProjetRepository;
import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.organisation.entities.Organisme;
import com.example.suivi_projet.organisation.repositories.EmployeRepository;
import com.example.suivi_projet.organisation.repositories.OrganismeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private OrganismeRepository organismeRepository;

    @Autowired
    private EmployeRepository employeRepository;

    // Sauvegarder ou créer un projet
    public Projet save(Projet projet) {
        return projetRepository.save(projet);
    }

    // Supprimer un projet par ID
    public boolean delete(int id) {
        Optional<Projet> projetOptional = projetRepository.findById(id);
        if (projetOptional.isPresent()) {
            projetRepository.delete(projetOptional.get());
            return true;
        } else {
            return false;
        }
    }

    // Récupérer tous les projets
    public List<Projet> findAll() {
        return projetRepository.findAll();
    }

    // Compter le nombre de projets
    public long countProjets() {
        return projetRepository.count();
    }

    // Trouver un projet par son code
    public Optional<Projet> findByCode(String code) {
        return Optional.ofNullable(projetRepository.findByCode(code));
    }

    // Exemple de méthode pour trouver par montant
    public List<Projet> findByMontant(double montant) {
        return projetRepository.findByMontant(montant);
    }

    // Vérification existence organisme
    public boolean isValidOrganisme(int organismeId) {
        return organismeRepository.findById(organismeId).isPresent();
    }

    // Vérification existence chef de projet
    public boolean isValidChefProjet(int chefId) {
        return employeRepository.findById(chefId).isPresent();
    }
}