package com.example.suivi_projet.projet.repositories;

import com.example.suivi_projet.projet.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    List<Document> findByProjetId(int projetId);

}
