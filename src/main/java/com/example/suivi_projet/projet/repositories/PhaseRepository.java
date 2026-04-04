package com.example.suivi_projet.projet.repositories;

import com.example.suivi_projet.projet.entities.Phase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhaseRepository extends JpaRepository<Phase, Integer> {

    List<Phase> findByProjetId(int projetId);

    List<Phase> findByEtatRealisationTrueAndEtatFacturationFalse();

    List<Phase> findByEtatFacturationTrueAndEtatPaiementFalse();
    List<Phase> findByEtatPaiementTrue();

}
