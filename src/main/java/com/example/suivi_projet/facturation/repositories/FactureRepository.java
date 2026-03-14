package com.example.suivi_projet.facturation.repositories;

import com.example.suivi_projet.facturation.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FactureRepository extends JpaRepository<Facture, Integer> {

    Optional<Facture> findByCode(String code);

    List<Facture> findByDateFacture(Date dateFacture);

}
