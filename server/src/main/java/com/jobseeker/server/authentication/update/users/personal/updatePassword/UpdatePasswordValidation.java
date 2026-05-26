package com.jobseeker.server.authentication.update.users.personal.updatePassword;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePasswordValidation(
        @NotNull(message = "password is required") @NotEmpty(message = "password is required") @Size(max = 128, message = "password is too long") String oldPassword,
        @NotNull(message = "new password is required") @NotEmpty(message = "new password is required") @Size(max = 128, message = "new password is too long") String newPassword) {
}