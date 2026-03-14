package com.example.suivi_projet.facturation.repositories;



import com.example.suivi_projet.facturation.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureRepository extends JpaRepository<Facture, Integer> {

}
