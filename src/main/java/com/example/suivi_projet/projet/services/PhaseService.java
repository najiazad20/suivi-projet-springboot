package com.example.suivi_projet.projet.services;

import com.example.suivi_projet.projet.entities.Phase;
import com.example.suivi_projet.projet.entities.Projet;
import com.example.suivi_projet.projet.repositories.PhaseRepository;
import com.example.suivi_projet.projet.repositories.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhaseService {

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private ProjetRepository projetRepository;

    // créer une phase dans un projet
    public Phase createPhase(int projetId, Phase phase) {

        Optional<Projet> projetOptional = projetRepository.findById(projetId);
        if (!projetOptional.isPresent()) return null;

        Projet projet = projetOptional.get();

        // contrôle dates
        if (phase.getDateDebut().before(projet.getDateDebut()) ||
                phase.getDateFin().after(projet.getDateFin())) {
            return null;
        }

        // contrôle montant
        double somme = phaseRepository.findByProjetId(projetId)
                .stream()
                .mapToDouble(Phase::getMontant)
                .sum();

        if (somme + phase.getMontant() > projet.getMontant()) {
            return null;
        }

        phase.setProjet(projet);
        return phaseRepository.save(phase);
    }

    // phases d'un projet
    public List<Phase> findByProjet(int projetId) {
        return phaseRepository.findByProjetId(projetId);
    }

    // phase par id
    public Optional<Phase> findById(int id) {
        return phaseRepository.findById(id);
    }

    // modifier phase
    public Phase update(int id, Phase phaseDetails) {

        Optional<Phase> phaseOptional = phaseRepository.findById(id);
        if (!phaseOptional.isPresent()) return null;

        Phase phase = phaseOptional.get();
        Projet projet = phase.getProjet();

        // contrôle dates
        if (phaseDetails.getDateDebut().before(projet.getDateDebut()) ||
                phaseDetails.getDateFin().after(projet.getDateFin())) {
            return null;
        }

        phase.setCode(phaseDetails.getCode());
        phase.setLibelle(phaseDetails.getLibelle());
        phase.setDescription(phaseDetails.getDescription());
        phase.setDateDebut(phaseDetails.getDateDebut());
        phase.setDateFin(phaseDetails.getDateFin());
        phase.setMontant(phaseDetails.getMontant());

        return phaseRepository.save(phase);
    }

    // supprimer phase
    public boolean delete(int id) {
        Optional<Phase> phaseOptional = phaseRepository.findById(id);

        if (phaseOptional.isPresent()) {
            phaseRepository.delete(phaseOptional.get());
            return true;
        }

        return false;
    }

    // changer état réalisation
    public Phase realisation(int id) {
        Optional<Phase> phaseOptional = phaseRepository.findById(id);
        if (!phaseOptional.isPresent()) return null;

        Phase phase = phaseOptional.get();
        phase.setEtatRealisation(true);

        return phaseRepository.save(phase);
    }

    // changer état facturation
    public Phase facturation(int id) {
        Optional<Phase> phaseOptional = phaseRepository.findById(id);
        if (!phaseOptional.isPresent()) return null;

        Phase phase = phaseOptional.get();
        phase.setEtatFacturation(true);

        return phaseRepository.save(phase);
    }

    // changer état paiement
    public Phase paiement(int id) {
        Optional<Phase> phaseOptional = phaseRepository.findById(id);
        if (!phaseOptional.isPresent()) return null;

        Phase phase = phaseOptional.get();
        phase.setEtatPaiement(true);

        return phaseRepository.save(phase);
    }
}