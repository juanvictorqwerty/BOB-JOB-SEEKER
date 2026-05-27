package com.jobseeker.server.authentication.tokens.revoque.One;

import org.springframework.stereotype.Service;

import com.jobseeker.server.models.Tokens;

@Service
public class RevOneService {
    private final RevOneInterface revOneInterface;

    public RevOneService(RevOneInterface revOneInterface) {
        this.revOneInterface = revOneInterface;
    }

    public String revOneToken(RevOneValidation revOneValidation) {
        try {
            Tokens tokens = revOneInterface.findByValue(revOneValidation.token());
            if (tokens == null) {
                return ("Token not found");
            }
            tokens.setIsValid(false);
            revOneInterface.save(tokens);
            return "Token Revoked Successfully";
        } catch (Exception e) {
            return ("Token Revocation Failed");
        }
    }
}
