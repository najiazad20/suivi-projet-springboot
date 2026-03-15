package com.example.suivi_projet.organisation.repositories;

import com.example.suivi_projet.organisation.entities.Organisme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganismeRepository extends JpaRepository<Organisme, Integer> {


    List<Organisme> findByNom(String nom);

    List<Organisme> findByCode(String code);

    List<Organisme> findByNomContact(String nomContact);
}