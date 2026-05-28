package com.jobseeker.server.postJob.marketplace;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MarketPlaceValidation {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Location JSON is required")
    private String location;

    @NotNull(message = "Description JSON is required")
    private String description;
}