package com.jobseeker.server.manageYourPost.user;

import com.jobseeker.server.models.MarketPlace;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/marketplace")
@RequiredArgsConstructor
public class MYPUController {

    private final MYPUValidation validationService;

    // Inject your application's JWT secret from application.properties /
    // application.yml
    @Value("${JWT_SECRET}")
    private String jwtSecret;

    /**
     * Fetch all listings for the authenticated user decoded from the token cookie.
     */
    @GetMapping("/user/myposts")
    public ResponseEntity<List<MarketPlace>> getUserPosts(
            @CookieValue(name = "token", required = false) String token) {

        if (token == null || token.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing session token cookie.");
        }

        // 1. Decode token on the backend and pull the verified user_id claim
        UUID userId = extractUserIdFromToken(token);

        // 2. Fetch and return data contextually
        List<MarketPlace> posts = validationService.getUserMarketplaceData(userId);
        return ResponseEntity.ok(posts);
    }

    // In com/jobseeker/server/manageYourPost/user/MYPUController.java
    @PatchMapping("/{postId}/status")
    public ResponseEntity<MarketPlace> togglePostStatus(
            @PathVariable UUID postId,
            @RequestParam(name = "isOpen") Boolean isOpen) { // Changed @RequestBody to @RequestParam
        MarketPlace updatedPost = validationService.updatePostStatus(postId, isOpen);
        return ResponseEntity.ok(updatedPost);
    }

    /**
     * Helper to safely verify signature and parse user_id matching TokenCreate
     * logic.
     */
    private UUID extractUserIdFromToken(String token) {
        try {
            // Replicate key deriving logic from TokenCreate.java to guarantee a match
            byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
            if (keyBytes.length < 32) {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                keyBytes = digest.digest(keyBytes);
            }
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);

            // Parse claims securely
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Extract the specific string claim defined during token creation
            String userIdStr = claims.get("user_id", String.class);
            if (userIdStr == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Malformated token: user_id missing.");
            }

            return UUID.fromString(userIdStr);

        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            // Catches expired, tampered, or malformed tokens cleanly
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired session token.", e);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cryptographic algorithm mismatch.", e);
        }
    }
}