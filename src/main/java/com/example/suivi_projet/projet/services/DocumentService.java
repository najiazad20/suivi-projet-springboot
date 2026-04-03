package com.example.suivi_projet.projet.services;

import com.example.suivi_projet.projet.dto.DocumentRequestDTO;
import com.example.suivi_projet.projet.dto.DocumentResponseDTO;
import com.example.suivi_projet.projet.entities.Document;
import com.example.suivi_projet.projet.repositories.DocumentRepository;
import com.example.suivi_projet.projet.repositories.ProjetRepository;
import com.example.suivi_projet.projet.mappers.DocumentMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ProjetRepository projetRepository;

    public DocumentResponseDTO save(DocumentRequestDTO dto) {
        Document document = DocumentMapper.toEntity(dto);
        projetRepository.findById(dto.projetId()).ifPresent(document::setProjet);
        Document saved = documentRepository.save(document);
        return DocumentMapper.toDTO(saved);
    }

    public List<DocumentResponseDTO> findByProjet(int projetId) {
        return documentRepository.findByProjetId(projetId)
                .stream()
                .map(DocumentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DocumentResponseDTO findById(int id) {
        return documentRepository.findById(id)
                .map(DocumentMapper::toDTO)
                .orElse(null);
    }

    public DocumentResponseDTO update(int id, DocumentRequestDTO dto) {
        return documentRepository.findById(id)
                .map(document -> {
                    DocumentMapper.updateEntity(document, dto);
                    projetRepository.findById(dto.projetId()).ifPresent(document::setProjet);
                    return DocumentMapper.toDTO(documentRepository.save(document));
                }).orElse(null);
    }

    public boolean delete(int id) {
        return documentRepository.findById(id)
                .map(document -> {
                    documentRepository.delete(document);
                    return true;
                }).orElse(false);
    }

    public List<DocumentResponseDTO> findByLibelle(String libelle) {
        return documentRepository.findByLibelle(libelle)
                .stream()
                .map(DocumentMapper::toDTO)
                .collect(Collectors.toList());
    }
}