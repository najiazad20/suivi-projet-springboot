package com.example.suivi_projet.security.config;

import com.example.suivi_projet.security.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtFilter;

    public SecurityConfig(JwtAuthorizationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
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

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // ADMIN
                        .requestMatchers("/api/employes/**").hasRole("ADMIN")

                        // SECRETAIRE
                        .requestMatchers("/api/organismes/**").hasRole("SECRETAIRE")
                        .requestMatchers("/api/projets/**").hasAnyRole("SECRETAIRE", "DIRECTEUR")

                        // DIRECTEUR
                        .requestMatchers("/api/reporting/**").hasRole("DIRECTEUR")

                        // CHEF PROJET
                        .requestMatchers("/api/phases/**").hasRole("CHEF_PROJET")
                        .requestMatchers("/api/livrables/**").hasRole("CHEF_PROJET")
                        .requestMatchers("/api/ligne-employe-phase/**").hasRole("CHEF_PROJET")

                        // COMPTABLE
                        .requestMatchers("/api/factures/**").hasRole("COMPTABLE")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}