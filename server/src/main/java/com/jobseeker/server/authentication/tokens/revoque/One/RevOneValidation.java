package com.jobseeker.server.authentication.tokens.revoque.One;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RevOneValidation(
                @NotBlank(message = "Token is required") String token) {
}
