package com.jobseeker.server.authentication.tokens.renew;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RenewTokenValidation(
        @NotBlank(message = "Refresh token is required") String refreshToken) {
}
