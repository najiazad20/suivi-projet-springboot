package com.example.suivi_projet.organisation.repositories;

import com.example.suivi_projet.organisation.entities.Profil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilRepository extends JpaRepository<Profil, Integer> {
}