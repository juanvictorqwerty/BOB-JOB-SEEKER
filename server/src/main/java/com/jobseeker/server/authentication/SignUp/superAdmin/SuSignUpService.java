package com.jobseeker.server.authentication.SignUp.superAdmin;

import org.springframework.stereotype.Service;

import com.jobseeker.server.authentication.dto.AuthDto;
import com.jobseeker.server.authentication.tokens.creation.TokenCreate;
import com.jobseeker.server.models.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class SuSignUpService {
    private final TokenCreate tokenCreate;
    private final SuSignUpInterface suSignUpInterface;
    private final BCryptPasswordEncoder passwordEncoder;
    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${JWT_VALIDATION}")
    private String jwtExpirationStr;

    @Value("${SUPER_ADMIN_CODE}")
    private String superCode;

    public SuSignUpService(SuSignUpInterface suSignUpInterface, TokenCreate tokenCreate,
            BCryptPasswordEncoder passwordEncoder) {
        this.suSignUpInterface = suSignUpInterface;
        this.tokenCreate = tokenCreate;
        this.passwordEncoder = passwordEncoder;
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
        // If parsed product is in seconds (e.g. less than 100 million), convert to
        // milliseconds
        if (product < 100_000_000L) {
            product *= 1000L;
        }
        return product;
    }

    public ResponseEntity<AuthDto> register(SuSignUpValidation suSignUpValidation) {
        try {

            Users checkUsername = suSignUpInterface.findByUsername(suSignUpValidation.username());
            if (checkUsername != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(AuthDto.builder()
                                .success(false)
                                .token(null)
                                .message("Username already exists")
                                .build());
            }
            Users checkEmail = suSignUpInterface.findByEmail(suSignUpValidation.email());
            if (checkEmail != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(AuthDto.builder()
                                .success(false)
                                .token(null)
                                .message("Email already exists")
                                .build());
            }
            if (!superCode.equals(suSignUpValidation.supercode())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(AuthDto.builder()
                                .success(false)
                                .token(null)
                                .message("Invalid Super Code")
                                .build());
            }

            String hashedPassword = passwordEncoder.encode(suSignUpValidation.password());
            Users newUser = Users.builder()
                    .username(suSignUpValidation.username())
                    .email(suSignUpValidation.email())
                    .password(hashedPassword)
                    .userRank(0)
                    .isChecked(true)
                    .isBlocked(false)
                    .build();

            suSignUpInterface.save(newUser);

            String token = tokenCreate.generateAndSaveToken(newUser, jwtSecret, getJwtExpirationMs());

            return ResponseEntity.ok(AuthDto.builder()
                    .success(true)
                    .token(token)
                    .message("Admin registered successfully")
                    .rank(newUser.getUserRank())
                    .build());

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(AuthDto.builder()
                            .success(false)
                            .token(null)
                            .message("Registration failed: " + e.getMessage())
                            .build());
        }
    }
}
