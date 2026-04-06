
package com.example.suivi_projet.organisation.repositories;

import com.example.suivi_projet.organisation.entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EmployeRepository extends JpaRepository<Employe, Integer> {

    Employe findByMatricule(String matricule);

    java.util.Optional<Employe> findByLogin(String login);
    @Query("SELECT e FROM Employe e WHERE e.id NOT IN (" +
            "SELECT l.employe.id FROM LigneEmployePhase l " +
            "WHERE (l.dateDebut <= :dateFin AND l.dateFin >= :dateDebut))")
    List<Employe> findAvailableEmployes(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin);
}
