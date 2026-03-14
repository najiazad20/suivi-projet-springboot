package com.example.suivi_projet.organisation.repositories;

import com.example.suivi_projet.organisation.entities.Organisme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganismeRepository extends JpaRepository<Organisme, Integer> {

    Organisme findByCode(String code);

}