  package com.example.suivi_projet.security.services;

import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.organisation.repositories.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PasswordResetService {

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public void sendNewPassword(String login, String email) {

        Employe emp = employeRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Login introuvable"));

        if (!emp.getEmail().equals(email)) {
            throw new RuntimeException("Email incorrect");
        }

        String newPassword = generatePassword();

        emp.setPassword(passwordEncoder.encode(newPassword));
        employeRepository.save(emp);

        emailService.sendNewPassword(email, newPassword);
    }

    private String generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder pass = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            pass.append(chars.charAt(rnd.nextInt(chars.length())));
        }

        return pass.toString();
    }
}