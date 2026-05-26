package com.jobseeker.server.authentication.tokens.creation;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import com.jobseeker.server.models.Tokens;
import com.jobseeker.server.models.Users;
import com.jobseeker.server.authentication.tokens.TokenRepo;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenCreate {

    private final TokenRepo tokensRepository;

    // Inject the tokens repository via constructor
    public TokenCreate(TokenRepo tokensRepository) {
        this.tokensRepository = tokensRepository;
    }

    /**
     * Generates a signed JWT token and persists its record into the database.
     * * @param user The fully constructed modern entity row for the user
     * 
     * @param jwtSecret       The secret configuration key
     * @param jwtExpirationMs Lifespan of the token in milliseconds
     * @return The raw compact string representation of the JWT token
     */
    public String generateAndSaveToken(Users user, String jwtSecret, long jwtExpirationMs) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpirationMs);

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
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        // 1. Build the actual string token values using JJWT
        String jwtString = Jwts.builder()
                .subject(user.getEmail())
                .claim("username", user.getUsername())
                .claim("userRank", user.getUserRank())
                .claim("user_id", user.getId().toString()) // <— This where to store the user token
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key, Jwts.SIG.HS256)
                .compact();

        // 2. Map fields to the Tokens Database Entity
        Tokens tokenEntity = Tokens.builder()
                .user(user) // The Modern Way: Associate the whole object! JPA handles the FK mapping.
                .value(jwtString)
                .expiresAt(expiration.toInstant()) // Convert Date to Instant cleanly
                .isValid(true)
                .build();

        // 3. Save/Insert the token record into the PostgreSQL database
        tokensRepository.save(tokenEntity);

        return jwtString;
    }
}