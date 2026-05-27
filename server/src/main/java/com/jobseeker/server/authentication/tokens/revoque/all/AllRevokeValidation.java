package com.jobseeker.server.authentication.tokens.revoque.all;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AllRevokeValidation(
        @NotBlank(message = "Token is required") String token) {
}
