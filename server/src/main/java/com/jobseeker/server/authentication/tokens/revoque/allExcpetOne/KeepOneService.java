package com.jobseeker.server.authentication.tokens.revoque.allExcpetOne;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.jobseeker.server.models.Tokens;

@Service
public class KeepOneService {
    private final KeepOneInterface keepOneInterface;

    public KeepOneService(KeepOneInterface keepOneInterface) {
        this.keepOneInterface = keepOneInterface;
    }

    public String keepOne(KeepOneValidation keepOneValidation) {
        try {
            // Find the token to identify the user
            Tokens token = keepOneInterface.findByValue(keepOneValidation.token());

            if (token == null) {
                return ("Token not found");
            }

            // Get the user ID from the found token
            UUID userId = token.getUser().getId(); // or token.getUser().getId()

            // Find ALL tokens belonging to this user
            List<Tokens> allUserTokens = keepOneInterface.findAllByUserId(userId);

            if (allUserTokens.isEmpty()) {
                return "No tokens found for user";
            }

            // Revoke all tokens except the current one
            for (Tokens userToken : allUserTokens) {
                if (!userToken.getValue().equals(keepOneValidation.token())) {
                    userToken.setIsValid(false);
                }
            }

            // Save all revoked tokens (use saveAll for efficiency)
            keepOneInterface.saveAll(allUserTokens);

            return "{ \"success\": true, \"token\": \"null\", \"message\": \"All tokens revoked successfully except the current one\"}";

        } catch (Exception e) {
            return "{ \"success\": false, \"token\": \"null\", \"message\": \"Token revocation failed: "
                    + e.getMessage() + "\"}";
        }
    }
}
