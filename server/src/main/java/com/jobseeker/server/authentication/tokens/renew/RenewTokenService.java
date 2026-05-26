package com.jobseeker.server.authentication.tokens.renew;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jobseeker.server.models.Tokens;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class RenewTokenService {

    private final RenewTokenInterface tokenRepository;

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public RenewTokenService(RenewTokenInterface tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String renewToken(RenewTokenValidation renewTokenValidation) {
        try {
            Tokens tokenEntity = tokenRepository.findByValue(renewTokenValidation.refreshToken());
            if (tokenEntity == null) {
                return "Token not found.";
            }

            Claims claims;
            try {
                claims = Jwts.parser()
                        .verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                        .build()
                        .parseSignedClaims(renewTokenValidation.refreshToken())
                        .getPayload();
            } catch (Exception e) {
                return "Invalid token: " + e.getMessage();
            }

            String userIdFromToken = claims.get("user_id", String.class);
            if (userIdFromToken == null) {
                return "Token missing user_id claim.";
            }

            UUID dbUserId = tokenEntity.getUser().getId();
            if (!dbUserId.toString().equals(userIdFromToken)) {
                return "Token user mismatch.";
            }

            tokenEntity.setExpiresAt(Instant.now().plusSeconds(30L * 24 * 60 * 60));
            tokenRepository.save(tokenEntity);

            return "{ \"message\": \"Token renewed successfully\"}token:" + tokenEntity;

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}