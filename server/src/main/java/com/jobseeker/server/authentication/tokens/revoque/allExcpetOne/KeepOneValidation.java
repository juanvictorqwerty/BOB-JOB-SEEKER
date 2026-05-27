package com.jobseeker.server.authentication.tokens.revoque.allExcpetOne;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record KeepOneValidation(
        @NotNull @NotBlank String token) {
}
