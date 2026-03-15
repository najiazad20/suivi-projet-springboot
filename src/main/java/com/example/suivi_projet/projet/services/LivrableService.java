package com.example.suivi_projet.projet.services;

import com.example.suivi_projet.projet.entities.Livrable;
import com.example.suivi_projet.projet.entities.Phase;
import com.example.suivi_projet.projet.repositories.LivrableRepository;
import com.example.suivi_projet.projet.repositories.PhaseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivrableService {

    @Autowired
    private LivrableRepository livrableRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    public Livrable save(int phaseId, Livrable livrable){

        Phase phase = phaseRepository.findById(phaseId).orElse(null);

        livrable.setPhase(phase);

        return livrableRepository.save(livrable);
    }

    public List<Livrable> findByPhase(int phaseId){
        return livrableRepository.findByPhaseId(phaseId);
    }

    public Livrable findById(int id){
        return livrableRepository.findById(id).orElse(null);
    }

    public Livrable update(int id, Livrable livrable){

        Livrable l = livrableRepository.findById(id).orElse(null);

        if(l != null){

            l.setCode(livrable.getCode());
            l.setLibelle(livrable.getLibelle());
            l.setDescription(livrable.getDescription());
            l.setChemin(livrable.getChemin());

            return livrableRepository.save(l);
        }

        return null;
    }

    public boolean delete(int id){

        Livrable livrable = livrableRepository.findById(id).orElse(null);

        if(livrable != null){
            livrableRepository.delete(livrable);
            return true;
        }

        return false;
    }
}