package com.jobseeker.server.authentication.forgotPassword.sendRecoveryEmail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SendRecoveryEmailValidation(
        @NotNull(message = "email is required") @NotEmpty(message = "email is required") @Email(message = "email is invalid") String email) {
}
