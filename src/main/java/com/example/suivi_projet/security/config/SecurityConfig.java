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


                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/forgot-password").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()


                        .requestMatchers("/api/auth/me").authenticated()
                        .requestMatchers("/api/auth/change-password").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/employes/disponibles").hasAnyRole("CHEF_PROJET", "ADMINISTRATEUR")
                        .requestMatchers("/api/employes/**").hasRole("ADMINISTRATEUR")
                        .requestMatchers("/api/profils/**").hasRole("ADMINISTRATEUR")

                        .requestMatchers(HttpMethod.POST, "/api/projets").hasAnyRole("SECRETAIRE", "DIRECTEUR")

                        .requestMatchers(HttpMethod.PUT, "/api/projets/*").hasAnyRole("DIRECTEUR")

                        .requestMatchers(HttpMethod.PATCH, "/api/projets/*/montant").hasRole("DIRECTEUR")
                        .requestMatchers(HttpMethod.PATCH, "/api/projets/*/affecter-chef").hasRole("DIRECTEUR")


                        .requestMatchers(HttpMethod.GET, "/api/projets/*/phases").hasAnyRole("SECRETAIRE", "CHEF_PROJET")                        // 3. ORGANISMES (UC1)
                        .requestMatchers(HttpMethod.GET, "/api/projets/**").hasAnyRole("SECRETAIRE", "DIRECTEUR")
                        .requestMatchers("/api/projets/**").hasRole("DIRECTEUR")
                        .requestMatchers("/api/organismes/**").hasAnyRole("SECRETAIRE")



                        .requestMatchers(HttpMethod.PATCH, "/api/phases/*/realisation").hasRole("CHEF_PROJET")
                        .requestMatchers(HttpMethod.PATCH, "/api/phases/*/facturation").hasRole("COMPTABLE")
                        .requestMatchers(HttpMethod.PATCH, "/api/phases/*/paiement").hasRole("COMPTABLE")
                        .requestMatchers("/api/phases/terminees-non-facturees").hasRole("COMPTABLE")
                        .requestMatchers("/api/phases/facturees-non-payees").hasRole("COMPTABLE")
                        .requestMatchers("/api/phases/*/employes/**").hasRole("CHEF_PROJET")
                        .requestMatchers("/api/phases/*/employes").hasRole("CHEF_PROJET")



                        .requestMatchers("/api/phases/**").hasRole("CHEF_PROJET")


                        .requestMatchers("/api/livrables/**").hasAnyRole("CHEF_PROJET", "DIRECTEUR")
                        .requestMatchers("/api/projets/*/documents").hasAnyRole("CHEF_PROJET", "DIRECTEUR", "SECRETAIRE")
                        .requestMatchers("/api/documents/**").hasAnyRole("CHEF_PROJET", "DIRECTEUR", "SECRETAIRE")

                        .requestMatchers("/api/reporting/**").hasAnyRole("DIRECTEUR", "CHEF_PROJET", "COMPTABLE")

                        .requestMatchers("/api/factures/**").hasAnyRole("COMPTABLE")
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}