package com.jobseeker.server.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie; // Import this
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extract the token from the parsed cookie array
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) { // Looks for cookie named 'token'
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 2. Process token if found
        if (token != null) {
            try {
                byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
                if (keyBytes.length < 32) {
                    try {
                        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
                        keyBytes = digest.digest(keyBytes);
                    } catch (java.security.NoSuchAlgorithmException e) {
                        byte[] padded = new byte[32];
                        System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));
                        keyBytes = padded;
                    }
                }

                Claims claims = Jwts.parser()
                        .verifyWith(Keys.hmacShaKeyFor(keyBytes))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String userId = claims.get("user_id", String.class);

                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userId, null, new ArrayList<>());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Invalid token context fallback safely
            }
        }

        filterChain.doFilter(request, response);
    }
}