package com.example.suivi_projet.projet.repositories;

import com.example.suivi_projet.projet.entities.LigneEmployePhase;
import com.example.suivi_projet.projet.entities.LigneEmployePhaseId;
import com.example.suivi_projet.projet.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneEmployePhaseRepository extends JpaRepository<LigneEmployePhase, LigneEmployePhaseId> {

    List<LigneEmployePhase> findByEmployeId(int employeId);

    List<LigneEmployePhase> findByPhaseId(int phaseId);

}
