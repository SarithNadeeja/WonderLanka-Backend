package com.wanderlanka.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ===== CORS =====
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ===== CSRF =====
                .csrf(csrf -> csrf.disable())

                // ===== AUTHORIZATION =====
                .authorizeHttpRequests(auth -> auth

                        // auth endpoints
                        .requestMatchers("/api/auth/**").permitAll()

                        // allow browser preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // allow serving uploaded images
                        .requestMatchers("/uploads/**").permitAll()

                        // everything else requires JWT
                        .anyRequest().authenticated()
                )

                // ===== JWT FILTER =====
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // ===== PASSWORD ENCODER =====
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ===== CORS CONFIG =====
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept"
        ));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
