package com.jobseeker.server.JobSearch;

import com.jobseeker.server.models.JobPost;
import com.jobseeker.server.models.MarketPlace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DTO {
    private List<JobPost> jobPosts;
    private List<MarketPlace> marketplaceListings;
    private long totalJobsFound;
    private long totalMarketplaceFound;
}