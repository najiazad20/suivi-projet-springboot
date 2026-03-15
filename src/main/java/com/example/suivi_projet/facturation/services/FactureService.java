package com.example.suivi_projet.facturation.services;

import com.example.suivi_projet.facturation.entities.Facture;
import com.example.suivi_projet.facturation.repositories.FactureRepository;
import com.example.suivi_projet.projet.entities.Phase;
import com.example.suivi_projet.projet.repositories.PhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FactureService {

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    public Facture createFacture(int phaseId, Facture facture) {
        Optional<Phase> optionalPhase = phaseRepository.findById(phaseId);
        if (optionalPhase.isEmpty()) {
            throw new RuntimeException("Phase introuvable");
        }

        Phase phase = optionalPhase.get();

        // Règle : phase terminée
        if (!phase.isEtatRealisation()) {
            throw new RuntimeException("La phase doit être terminée");
        }

        // Règle : pas deux factures
        if (phase.isEtatFacturation()) {
            throw new RuntimeException("Cette phase est déjà facturée");
        }

        facture.setPhase(phase);

        // Mettre à jour l'état facturation de la phase
        phase.setEtatFacturation(true);
        phaseRepository.save(phase);

        return factureRepository.save(facture);
    }

    public Facture updateFacture(int id, Facture factureDetails) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable"));
        facture.setCode(factureDetails.getCode());
        facture.setDateFacture(factureDetails.getDateFacture());
        // Ne pas changer la phase ici pour éviter incohérence
        return factureRepository.save(facture);
    }

    public void deleteFacture(int id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable"));
        Phase phase = facture.getPhase();
        // Mettre à jour l'état facturation de la phase
        phase.setEtatFacturation(false);
        phaseRepository.save(phase);

        factureRepository.delete(facture);
    }

    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }

    public Facture getFactureById(int id) {
        return factureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable"));
    }
    public List<Facture> getFacturesByDate(Date date) {
        return factureRepository.findByDateFacture(date);
    }
}