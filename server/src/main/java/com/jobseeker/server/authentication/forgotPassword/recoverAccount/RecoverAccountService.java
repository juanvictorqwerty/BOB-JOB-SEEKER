package com.jobseeker.server.authentication.forgotPassword.recoverAccount;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;

import com.jobseeker.server.authentication.tokens.revoque.all.AllRevokeInterface;
import com.jobseeker.server.models.Tokens;
import com.jobseeker.server.models.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class RecoverAccountService {
    private final RecoverAccountInterface recoverAccountInterface;
    private final AllRevokeInterface allRevokeInterface;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public RecoverAccountService(
            RecoverAccountInterface recoverAccountInterface,
            AllRevokeInterface allRevokeInterface,
            PasswordEncoder passwordEncoder) {
        this.recoverAccountInterface = recoverAccountInterface;
        this.allRevokeInterface = allRevokeInterface;
        this.passwordEncoder = passwordEncoder;
    }

    public String recoverAccount(RecoverAccountValidation recoverAccountValidation, String recoveryTokenValue) {
        try {
            // 1. Prepare key and parse the stateless JWT recovery token
            byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
            if (keyBytes.length < 32) {
                java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
                keyBytes = digest.digest(keyBytes);
            }
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(recoveryTokenValue)
                    .getPayload();

            String userIdString = claims.get("user_id", String.class);
            if (userIdString == null) return "Invalid token payload.";
            UUID userId = UUID.fromString(userIdString);

            // 2. Find the user
            Users user = recoverAccountInterface.findById(userId).orElse(null);
            if (user == null) {
                return "User not found.";
            }

            // 3. Update the password
            String hashedPassword = passwordEncoder.encode(recoverAccountValidation.newPassword());
            user.setPassword(hashedPassword);
            recoverAccountInterface.save(user);

            // 4. Revocation Logic:
            // If token is in DB (standard session), revoke all EXCEPT current.
            // If token is NOT in DB (stateless recovery), revoke ALL stored tokens.
            Tokens dbToken = allRevokeInterface.findByValue(recoveryTokenValue);
            List<Tokens> allUserTokens = allRevokeInterface.findAllByUserId(userId);
            for (Tokens t : allUserTokens) {
                if (dbToken == null || !t.getValue().equals(recoveryTokenValue)) {
                    t.setIsValid(false);
                }
            }
            allRevokeInterface.saveAll(allUserTokens);

            return "Account recovered successfully. Existing sessions have been invalidated.";
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return "Recovery token has expired (1 hour limit).";
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            return "Invalid recovery token.";
        } catch (Exception e) {
            System.err.println("Error during account recovery: " + e.getMessage());
            return "An unexpected error occurred during account recovery.";
        }
    }
}
