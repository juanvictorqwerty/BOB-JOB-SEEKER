package com.jobseeker.server.authentication.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record LoginValidation(
        @NotEmpty(message = "email cannot be empty") @Email(message = "Email is not valid") String email,

        @NotEmpty(message = "password cannot be empty") String password) {

}
