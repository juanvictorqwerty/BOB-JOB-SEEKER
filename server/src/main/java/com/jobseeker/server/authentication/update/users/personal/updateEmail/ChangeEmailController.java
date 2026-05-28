package com.jobseeker.server.authentication.update.users.personal.updateEmail;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CookieValue;

@RestController
public class ChangeEmailController {
    private final ChangEmailService changEmailService;

    public ChangeEmailController(ChangEmailService changEmailService) {
        this.changEmailService = changEmailService;
    }

    @PostMapping("/api/auth/change-email")
    public String changeEmail(
            @CookieValue(value = "token", required = false) String token,
            @Valid @RequestBody ChangeEmailValidation changeEmailValidation) {

        if (token == null || token.isEmpty()) {
            return "Missing token";
        }

        return changEmailService.changeEmail(token, changeEmailValidation);
    }
}