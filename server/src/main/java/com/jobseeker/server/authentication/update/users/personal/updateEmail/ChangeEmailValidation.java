package com.jobseeker.server.authentication.update.users.personal.updateEmail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ChangeEmailValidation(

        @NotBlank(message = "New email is required") @Email(message = "Invalid email format") String newEmail,

        @NotBlank(message = "Current password is required for verification") String currentPassword

) {
}