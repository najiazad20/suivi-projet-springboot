package com.example.suivi_projet.projet.repositories;

import com.example.suivi_projet.projet.entities.Livrable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivrableRepository extends JpaRepository<Livrable, Integer> {

    List<Livrable> findByPhaseId(int phaseId);

}
