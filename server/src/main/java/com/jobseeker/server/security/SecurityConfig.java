package com.jobseeker.server.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Safely fallback to localhost:3000 if the ${CORS} environment variable is
    // missing
    @Value("${CORS:http://localhost:3000}")
    private String allowedOrigin;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Enable CORS using the corsConfigurationSource bean defined below
                .cors(Customizer.withDefaults())

                // 2. CSRF = false (required for JWT-based authentication)
                .csrf(AbstractHttpConfigurer::disable)

                // 3. Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // allow login, register, refresh
                        .requestMatchers("/api/ads/search").permitAll() // allow public ad search
                        .requestMatchers("/api/**").authenticated() // everything else needs token
                        .anyRequest().permitAll() // allow Swagger UI
                )

                // 4. Session Management: stateless (required for JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Maps directly to your application.yaml requirements
        configuration.setAllowedOrigins(Collections.singletonList(allowedOrigin));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Applies this policy strictly to your /api/** base path
        source.registerCorsConfiguration("/api/**",
                source.getCorsConfigurations().getOrDefault("/api/**", configuration));
        source.registerCorsConfiguration("/**", configuration); // Backup mapping for entire app

        return source;
    }
}
