package com.jobseeker.server.authentication.update.users.personal.updatePassword;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class UpdatePasswordController {
    private final UpdatePasswordService updatePasswordService;

    public UpdatePasswordController(UpdatePasswordService updatePasswordService) {
        this.updatePasswordService = updatePasswordService;
    }

    @PostMapping("/api/auth/update-password")
    public String updatePassword(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody UpdatePasswordValidation updatePasswordValidation) {

        // Extract token from "Bearer <token>"
        String token = authHeader.replace("Bearer ", "");

        return updatePasswordService.updatePassword(token, updatePasswordValidation);
    }
}
