package com.example.suivi_projet.security.services;

import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.organisation.repositories.EmployeRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeRepository employeRepository;

    public CustomUserDetailsService(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Employe employe = employeRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Employé non trouvé : " + username));


        String roleName = "ROLE_" + employe.getProfil().getLibelle().toUpperCase().replace(" ", "_");

        return new User(
                employe.getLogin(),
                employe.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(roleName))
        );
    }
}