package com.example.suivi_projet.security.controllers;

import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.organisation.repositories.EmployeRepository;
import com.example.suivi_projet.security.dto.*;
import com.example.suivi_projet.security.jwt.JwtUtil;
import com.example.suivi_projet.security.services.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final EmployeRepository employeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetService passwordResetService;

    // Constructeur avec 4 arguments pour correspondre à l'injection
    public AuthController(AuthenticationManager am, JwtUtil ju, EmployeRepository er, PasswordEncoder pe) {
        this.authManager = am;
        this.jwtUtil = ju;
        this.employeRepository = er;
        this.passwordEncoder = pe;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String login, @RequestParam String email) {
        try {
            passwordResetService.sendNewPassword(login, email);
            return ResponseEntity.ok("Un nouveau mot de passe a été envoyé à votre adresse email");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        return ResponseEntity.ok(new AuthResponseDTO(jwtUtil.generateToken(req.username())));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Principal principal) {
        return ResponseEntity.ok(employeRepository.findByLogin(principal.getName()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO req, Principal principal) {
        Employe emp = employeRepository.findByLogin(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(req.oldPassword(), emp.getPassword())) {
            return ResponseEntity.badRequest().body("Ancien mot de passe incorrect");
        }
        emp.setPassword(passwordEncoder.encode(req.newPassword()));
        employeRepository.save(emp);
        return ResponseEntity.ok("Mot de passe mis à jour");
    }
}