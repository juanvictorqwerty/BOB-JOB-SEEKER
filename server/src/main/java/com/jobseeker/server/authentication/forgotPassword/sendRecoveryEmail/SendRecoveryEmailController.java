package com.jobseeker.server.authentication.forgotPassword.sendRecoveryEmail;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class SendRecoveryEmailController {
    private final SendRecoveryEmailService sendRecoveyEmailService;

    public SendRecoveryEmailController(SendRecoveryEmailService sendRecoveyEmailService) {
        this.sendRecoveyEmailService = sendRecoveyEmailService;
    }

    @PostMapping("/api/auth/send-recovery-email")
    public String sendRecoveryEmail(@Valid @RequestBody SendRecoveryEmailValidation sendRecoveyEmailValidation) {
        return sendRecoveyEmailService.sendRecoveryEmail(sendRecoveyEmailValidation);
    }
}
