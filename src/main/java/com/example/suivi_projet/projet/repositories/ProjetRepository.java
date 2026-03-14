package com.example.suivi_projet.projet.repositories;

import com.example.suivi_projet.projet.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetRepository extends JpaRepository<Projet, Integer> {

    Projet findByCode(String code);
    Projet findByMontant(Double montant);
}
