package com.example.suivi_projet.projet.mappers;

import com.example.suivi_projet.projet.dto.DocumentDTO;
import com.example.suivi_projet.projet.entities.Document;

public class DocumentMapper {

    public static DocumentDTO toDTO(Document document) {

        DocumentDTO dto = new DocumentDTO();

        dto.setId(document.getId());
        dto.setCode(document.getCode());
        dto.setLibelle(document.getLibelle());
        dto.setDescription(document.getDescription());
        dto.setChemin(document.getChemin());

        if (document.getProjet() != null) {
            dto.setProjetId(document.getProjet().getId());
        }

        return dto;
    }

    public static Document toEntity(DocumentDTO dto) {

        Document document = new Document();

        document.setId(dto.getId());
        document.setCode(dto.getCode());
        document.setLibelle(dto.getLibelle());
        document.setDescription(dto.getDescription());
        document.setChemin(dto.getChemin());

        return document;
    }
}