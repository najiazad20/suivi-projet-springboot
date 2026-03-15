package com.example.suivi_projet.projet.repositories;

import com.example.suivi_projet.projet.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjetRepository extends JpaRepository<Projet, Integer> {

    Projet findByCode(String code);
    ;
    List<Projet> findByMontant(double montant);
}
