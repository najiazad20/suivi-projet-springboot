
package com.example.suivi_projet.organisation.repositories;

import com.example.suivi_projet.organisation.entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeRepository extends JpaRepository<Employe, Integer> {

    Employe findByMatricule(String matricule);

    Employe findByLogin(String login);

}