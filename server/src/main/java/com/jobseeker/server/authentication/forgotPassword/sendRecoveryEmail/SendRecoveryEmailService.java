package com.jobseeker.server.authentication.forgotPassword.sendRecoveryEmail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jobseeker.server.authentication.tokens.creation.TokenCreate;
import com.jobseeker.server.email.EmailService;
import com.jobseeker.server.models.Users;

@Service
public class SendRecoveryEmailService {
    private final SendRecoveryEmailInterface sendRecoveryEmailInterface;
    private final EmailService emailService;
    private final TokenCreate tokenCreate; // <-- ADDED: inject your token service

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.validation}")
    private long jwtExpirationMs;

    public SendRecoveryEmailService(SendRecoveryEmailInterface sendRecoveryEmailInterface,
            EmailService emailService,
            TokenCreate tokenCreate) { // <-- ADDED
        this.sendRecoveryEmailInterface = sendRecoveryEmailInterface;
        this.emailService = emailService;
        this.tokenCreate = tokenCreate;
    }

    public String sendRecoveryEmail(SendRecoveryEmailValidation sendRecoveryEmailValidation) {
        try {
            // 1. Find user by email
            Users user = sendRecoveryEmailInterface.findByEmail(sendRecoveryEmailValidation.email());
            if (user == null) {
                return "User not found";
            }

            // 2. Generate a recovery token using your existing TokenCreate service
            // You need to pass jwtSecret and jwtExpirationMs (get from
            // application.properties)
            String token = tokenCreate.generateAndSaveToken(
                    user,
                    jwtSecret, // <-- you need this value
                    3600000L // 1 hour = 1000 * 60 * 60 ms
            );

            // 3. Send recovery email with recovery token
            String recoveryUrl = "http://localhost:3000/recovery?token=" + token;
            emailService.sendEmail(
                    sendRecoveryEmailValidation.email(),
                    "Password Recovery",
                    "Click this link to reset your password: " + recoveryUrl);

            return "Recovery email sent successfully";

        } catch (Exception e) {
            return "Failed to send recovery email: " + e.getMessage();
        }
    }
}