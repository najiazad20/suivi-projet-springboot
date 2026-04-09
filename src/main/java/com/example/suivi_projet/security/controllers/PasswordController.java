 package com.example.suivi_projet.security.controllers;

import com.example.suivi_projet.security.services.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class PasswordController {

    @Autowired
    private PasswordResetService service;

    @PostMapping("/forgot-password")
    public String forgot(@RequestParam String login,
                         @RequestParam String email) {

        service.sendNewPassword(login, email);
        return "Mot de passe envoyé par email";
    }
}

