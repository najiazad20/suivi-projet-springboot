package com.example.suivi_projet.organisation.services;

import com.example.suivi_projet.organisation.entities.Organisme;
import com.example.suivi_projet.organisation.repositories.OrganismeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganismeService {

    @Autowired
    private OrganismeRepository repository;

    public Organisme save(Organisme org) {
        return repository.save(org);
    }

    public Optional<Organisme> findById(int id) {
        return repository.findById(id);
    }

    public List<Organisme> findAll() {
        return repository.findAll();
    }

    public List<Organisme> findByNom(String nom) {
        return repository.findByNom(nom);
    }

    public List<Organisme> findByCode(String code) {
        return repository.findByCode(code);
    }

    public List<Organisme> findByNomContact(String contact) {
        return repository.findByNomContact(contact);
    }

    public boolean delete(int id) {
        Optional<Organisme> org = repository.findById(id);
        if(org.isPresent()) {
            repository.delete(org.get());
            return true;
        }
        return false;
    }
}