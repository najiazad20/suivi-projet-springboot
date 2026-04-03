package com.example.suivi_projet.projet.mappers;

import com.example.suivi_projet.projet.dto.DocumentRequestDTO;
import com.example.suivi_projet.projet.dto.DocumentResponseDTO;
import com.example.suivi_projet.projet.entities.Document;

public class DocumentMapper {

    public static DocumentResponseDTO toDTO(Document document) {
        int projetId = document.getProjet() != null ? document.getProjet().getId() : 0;
        return new DocumentResponseDTO(
                document.getId(),
                document.getCode(),
                document.getLibelle(),
                document.getDescription(),
                document.getChemin(),
                projetId
        );
    }

    public static Document toEntity(DocumentRequestDTO dto) {
        Document document = new Document();
        document.setCode(dto.code());
        document.setLibelle(dto.libelle());
        document.setDescription(dto.description());
        document.setChemin(dto.chemin());
        return document;
    }

    public static void updateEntity(Document document, DocumentRequestDTO dto) {
        document.setCode(dto.code());
        document.setLibelle(dto.libelle());
        document.setDescription(dto.description());
        document.setChemin(dto.chemin());
    }
}