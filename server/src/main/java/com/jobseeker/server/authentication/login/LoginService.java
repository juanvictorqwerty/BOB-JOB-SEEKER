package com.jobseeker.server.authentication.login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jobseeker.server.authentication.tokens.creation.TokenCreate;
import com.jobseeker.server.models.Users;

@Service
public class LoginService {
    private final LoginInterface loginInterface;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenCreate tokenCreate;

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${JWT_VALIDATION}")
    private String jwtExpirationStr;

    public LoginService(LoginInterface loginInterface, BCryptPasswordEncoder passwordEncoder,
            TokenCreate tokenCreate) {
        this.loginInterface = loginInterface;
        this.passwordEncoder = passwordEncoder;
        this.tokenCreate = tokenCreate; // Was: null
    }

    private long getJwtExpirationMs() {
        if (jwtExpirationStr == null) {
            return 86400000L;
        }
        String clean = jwtExpirationStr.trim();
        long product = 1;
        if (clean.contains("*")) {
            try {
                for (String part : clean.split("\\*")) {
                    product *= Long.parseLong(part.trim());
                }
            } catch (NumberFormatException e) {
                return 86400000L;
            }
        } else {
            try {
                product = Long.parseLong(clean);
            } catch (NumberFormatException e) {
                return 86400000L;
            }
        }
        if (product < 100_000_000L) {
            product *= 1000L;
        }
        return product;
    }

    public String login(LoginValidation loginValidation) {
        try {
            Users user = loginInterface.findByEmail(loginValidation.email());
            if (user == null) {
                return "Login failed: Email not found.";
            }
            if (user.isBlocked()) {
                return "Login failed: User is blocked.";
            }
            if (!passwordEncoder.matches(loginValidation.password(), user.getPassword())) {
                return "Login failed: Invalid email or password";
            }
            String token = tokenCreate.generateAndSaveToken(user, jwtSecret, getJwtExpirationMs());
            return "Login successful. Token: " + token + "rank" + user.getUserRank();
        } catch (Exception e) {
            return "Login failed: " + e.getMessage();
        }
    }
}