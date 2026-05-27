package com.jobseeker.server.authentication.tokens.revoque.all;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

import com.jobseeker.server.models.Tokens;

@Service
public class AllRevokeService {
    private final AllRevokeInterface allRevokeInterface;

    public AllRevokeService(AllRevokeInterface allRevokeInterface) {
        this.allRevokeInterface = allRevokeInterface;
    }

    public String revokeAllTokens(AllRevokeValidation allRevokeValidation) {
        try {
            // Find the token to identify the user
            Tokens token = allRevokeInterface.findByValue(allRevokeValidation.token());

            if (token == null) {
                return ("Token not found");
            }

            // Get the user ID from the found token
            UUID userId = token.getUser().getId(); // or token.getUser().getId()

            // Find ALL tokens belonging to this user
            List<Tokens> allUserTokens = allRevokeInterface.findAllByUserId(userId);

            if (allUserTokens.isEmpty()) {
                return "No tokens found for user";
            }

            // Revoke all tokens
            for (Tokens userToken : allUserTokens) {
                userToken.setIsValid(false);
            }

            // Save all revoked tokens (use saveAll for efficiency)
            allRevokeInterface.saveAll(allUserTokens);

            return "{ \"success\": true, \"token\": \"null\", \"message\": \"All tokens revoked successfully\"}";

        } catch (Exception e) {
            return "{ \"success\": false, \"token\": \"null\", \"message\": \"Token revocation failed: "
                    + e.getMessage() + "\"}";
        }
    }
}