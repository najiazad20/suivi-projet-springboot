package com.example.suivi_projet.security.controllers;

import com.example.suivi_projet.security.dto.*;
import com.example.suivi_projet.security.jwt.JwtUtil;
import com.example.suivi_projet.organisation.entities.Employe;
import com.example.suivi_projet.organisation.repositories.EmployeRepository;
import com.example.suivi_projet.exceptions.BusinessException;

import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EmployeRepository employeRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          EmployeRepository employeRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.employeRepository = employeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // LOGIN
    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        Employe emp = employeRepository.findByLogin(request.username());

        String token = jwtUtil.generateToken(emp.getLogin());

        return new AuthResponseDTO(
                token,
                emp.getLogin(),
                emp.getProfil().getCode()
        );
    }

    // UTILISATEUR  CONNECTE
    @GetMapping("/me")
    public Object me() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // CHANGE  PASSWORD
    @PostMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordDTO dto) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Employe emp = employeRepository.findByLogin(username);

        if (!passwordEncoder.matches(dto.oldPassword(), emp.getPassword())) {
            throw new BusinessException("Ancien mot de passe incorrect");
        }

        emp.setPassword(passwordEncoder.encode(dto.newPassword()));
        employeRepository.save(emp);

        return "Mot de passe modifié avec succès";
    }
}