package com.jobseeker.server.authentication.SignUp.superAdmin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SuSignUpValidation(
        @NotEmpty(message = "username cannot be empty") @Size(min = 5, max = 30, message = "username must be between 5 and 30 characters") @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "username can only contain letters, numbers and underscores") String username,

        @NotEmpty(message = "Email cannot be empty") @Email(message = "Email is not valid") String email,

        @NotEmpty(message = "Super Code is required") String supercode,

        @NotEmpty(message = "Password cannot be empty") String password) {

}