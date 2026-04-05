package com.example.suivi_projet.security.services;



import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.organisation.repositories.EmployeRepository;
import com.example.suivi_projet.exceptions.ResourceNotFoundException;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeRepository employeRepository;

    public CustomUserDetailsService(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        Employe employe = employeRepository.findByLogin(username);

        if (employe == null) {
            throw new ResourceNotFoundException("Utilisateur introuvable avec login : " + username);
        }

        return new User(
                employe.getLogin(),
                employe.getPassword(),
                List.of(() -> "ROLE_" + employe.getProfil().getCode())
        );
    }
}
