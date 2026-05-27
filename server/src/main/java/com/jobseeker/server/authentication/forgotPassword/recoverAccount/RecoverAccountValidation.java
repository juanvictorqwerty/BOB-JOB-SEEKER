package com.jobseeker.server.authentication.forgotPassword.recoverAccount;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RecoverAccountValidation(
        @NotNull(message = "Where is the token ?") @NotBlank(message = "Where is the token ?") String token,
        @NotNull(message = "Password is required") @NotEmpty(message = "Password is required") @Size(min = 6, max = 128, message = "password must be between 6 and 128 characters") String newPassword) {
}
