package com.example.suivi_projet.security.config;

import com.example.suivi_projet.security.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthorizationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // AUTHENTIFICATION
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // API AUTH (Récupérer profil ou changer pass) : Tout utilisateur connecté
                        .requestMatchers("/api/auth/me").authenticated()
                        .requestMatchers("/api/auth/change-password").authenticated()
                        // 1. ADMINISTRATEUR (Gestion des utilisateurs & profils)
                        .requestMatchers("/api/employes/**").hasRole("ADMINISTRATEUR")
                        .requestMatchers("/api/profils/**").hasRole("ADMINISTRATEUR")

                        // 2. GESTION DES PROJETS (UC1)
                        // Création : Secrétaire uniquement (ou Directeur)
                        .requestMatchers(HttpMethod.POST, "/api/projets").hasAnyRole("SECRETAIRE", "DIRECTEUR")
                        // Modification administrative : Secrétaire & Directeur
                        .requestMatchers(HttpMethod.PUT, "/api/projets/*").hasAnyRole("SECRETAIRE", "DIRECTEUR")
                        // Modification financière (Montant) & Affectation Chef : Directeur uniquement
                        .requestMatchers(HttpMethod.PATCH, "/api/projets/*/montant").hasRole("DIRECTEUR")
                        .requestMatchers(HttpMethod.PATCH, "/api/projets/*/affecter-chef").hasRole("DIRECTEUR")
                        // Recherche & Consultation : Tout le monde authentifié
                        .requestMatchers(HttpMethod.GET, "/api/projets/**").authenticated()

                        // 3. ORGANISMES (UC1)
                        .requestMatchers("/api/organismes/**").hasAnyRole("SECRETAIRE")

                        // 4. PHASES (UC1 & UC3)
                        // Structure (Chef de projet)
                        .requestMatchers(HttpMethod.POST, "/api/projets/*/phases").hasRole("CHEF_PROJET")
                        .requestMatchers(HttpMethod.PATCH, "/api/phases/*/realisation").hasRole("CHEF_PROJET")
                        .requestMatchers("/api/phases/*/employes/**").hasRole("CHEF_PROJET")
                        // Finance (Comptable)
                        .requestMatchers(HttpMethod.PATCH, "/api/phases/*/facturation").hasRole("COMPTABLE")
                        .requestMatchers(HttpMethod.PATCH, "/api/phases/*/paiement").hasRole("COMPTABLE")

                        // 5. LIVRABLES & DOCUMENTS
                        .requestMatchers("/api/livrables/**").hasAnyRole("CHEF_PROJET", "DIRECTEUR")
                        .requestMatchers("/api/projets/*/documents").hasAnyRole("CHEF_PROJET", "DIRECTEUR", "SECRETAIRE")

                        // 6. REPORTING (UC3)
                        .requestMatchers("/api/reporting/**").hasAnyRole("DIRECTEUR", "COMPTABLE")
                        .requestMatchers("/api/phases/terminees-non-facturees").hasRole("COMPTABLE")
                        .requestMatchers("/api/phases/facturees-non-payees").hasRole("COMPTABLE")

                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}