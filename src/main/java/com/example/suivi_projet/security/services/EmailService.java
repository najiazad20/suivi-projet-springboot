package com.example.suivi_projet.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendNewPassword(String to, String password) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Réinitialisation du mot de passe");

        message.setText(
                "Bonjour,\n\n" +
                        "Votre nouveau mot de passe est : " + password +
                        "\n\nMerci."
        );

        mailSender.send(message);
    }
}
