package com.jobseeker.server.authentication.update.users.personal.updateEmail;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class ChangeEmailController {
    private final ChangEmailService changEmailService;

    public ChangeEmailController(ChangEmailService changEmailService) {
        this.changEmailService = changEmailService;
    }

    @PostMapping("api/auth/change-email")
    public String changeEmail(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ChangeEmailValidation changeEmailValidation) {

        // Extract token from "Bearer <token>"
        String token = authHeader.replace("Bearer ", "");

        return changEmailService.changeEmail(token, changeEmailValidation);
    }
}