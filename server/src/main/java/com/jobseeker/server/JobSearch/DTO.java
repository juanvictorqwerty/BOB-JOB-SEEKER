package com.jobseeker.server.JobSearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DTO {
    private List<JobResult> jobPosts;
    private List<MarketplaceResult> marketplaceListings;
    private long totalJobsFound;
    private long totalMarketplaceFound;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobResult {
        private UUID id;
        private String title;
        private String city;
        private BigDecimal salary;
        private String description;
        private String geoLocation;
        private Instant createdAt;
        private String email; // The company contact email
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarketplaceResult {
        private UUID id;
        private String title;
        private String description;
        private String location;
        private String imageUrl;
        private Instant createdAt;
        private Boolean isOpen;
        private String email; // The poster's personal user email
    }
}