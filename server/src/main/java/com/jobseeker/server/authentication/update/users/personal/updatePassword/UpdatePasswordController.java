package com.jobseeker.server.authentication.update.users.personal.updatePassword;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CookieValue;

@RestController
public class UpdatePasswordController {
    private final UpdatePasswordService updatePasswordService;

    public UpdatePasswordController(UpdatePasswordService updatePasswordService) {
        this.updatePasswordService = updatePasswordService;
    }

    @PostMapping("/api/auth/update-password")
    public String updatePassword(
            @CookieValue(value = "token", required = false) String token,
            @Valid @RequestBody UpdatePasswordValidation updatePasswordValidation) {

        if (token == null || token.isEmpty()) {
            return "Missing token";
        }

        return updatePasswordService.updatePassword(token, updatePasswordValidation);
    }
}
