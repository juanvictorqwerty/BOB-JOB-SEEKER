package com.jobseeker.server.authentication.update.users.changeRank;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ChangeRankValidation(
        @NotNull @NotBlank String userEmail,
        @NotNull @NotBlank Integer newRank) {

}
