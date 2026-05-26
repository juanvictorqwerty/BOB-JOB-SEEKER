package com.jobseeker.server.authentication.SignUp.regularUser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.jobseeker.server.models.Users;
import com.jobseeker.server.authentication.tokens.creation.TokenCreate;

@Service
public class RegSignUpService {

    private final RegSignUpInterface regSignUpInterface;
    private final TokenCreate tokenCreate; // Injected instance
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${JWT_VALIATION}")
    private String jwtExpirationStr;

    // Constructor injection for both dependent components
    public RegSignUpService(RegSignUpInterface regSignUpInterface, TokenCreate tokenCreate) {
        this.regSignUpInterface = regSignUpInterface;
        this.tokenCreate = tokenCreate;
    }

    private long getJwtExpirationMs() {
        if (jwtExpirationStr == null) {
            return 86400000L; // default 1 day in milliseconds
        }
        String clean = jwtExpirationStr.trim();
        long product = 1;
        if (clean.contains("*")) {
            try {
                for (String part : clean.split("\\*")) {
                    product *= Long.parseLong(part.trim());
                }
            } catch (NumberFormatException e) {
                return 86400000L; // Fallback
            }
        } else {
            try {
                product = Long.parseLong(clean);
            } catch (NumberFormatException e) {
                return 86400000L; // Fallback
            }
        }
        // If parsed product is in seconds (e.g. less than 100 million), convert to milliseconds
        if (product < 100_000_000L) {
            product *= 1000L;
        }
        return product;
    }

    public String register(RegSignUpValidation regSignUpValidation) {
        try {
            // Uniqueness Checks
            if (regSignUpInterface.findByUsername(regSignUpValidation.username()) != null) {
                return "Registration impossible: Username is already taken.";
            }
            if (regSignUpInterface.findByEmail(regSignUpValidation.email()) != null) {
                return "Registration impossible: Email is already registered.";
            }

            String hashedPassword = passwordEncoder.encode(regSignUpValidation.password());

            Users newAccount = Users.builder()
                    .username(regSignUpValidation.username())
                    .email(regSignUpValidation.email())
                    .password(hashedPassword)
                    .isChecked(true)
                    .isBlocked(false)
                    .userRank(3)
                    .build();

            // CRITICAL STEP: Save the user first so that PostgreSQL generates its UUID ID!
            Users savedUser = regSignUpInterface.save(newAccount);

            // PASS the newly persisted user object down. JPA extracts the user_id for the
            // database table link.
            String token = tokenCreate.generateAndSaveToken(savedUser, jwtSecret, getJwtExpirationMs());

            return "New Account Created Successfully. Token: " + token;

        } catch (Exception e) {
            return "Registration failed: " + e.getMessage();
        }
    }
}