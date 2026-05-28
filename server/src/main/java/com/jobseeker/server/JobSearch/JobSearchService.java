package com.jobseeker.server.JobSearch;

import com.jobseeker.server.models.JobPost;
import com.jobseeker.server.models.MarketPlace;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
public class JobSearchService {

    private final JobSearchInterface searchRepository;

    public JobSearchService(JobSearchInterface searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Transactional(readOnly = true)
    public DTO unifiedSearch(String textQuery, int page, int size) {
        // Safe string fallback
        String safeQuery = (textQuery == null) ? "" : textQuery.trim();

        // Separate sort rules matching the specific audit timestamp configurations of
        // your tables
        Pageable jobPageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Pageable marketPageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Fetch subsets from both operational domains simultaneously
        List<JobPost> jobs = searchRepository.searchJobPosts(safeQuery, jobPageable);
        List<MarketPlace> marketplace = searchRepository.searchMarketplaceListings(safeQuery, marketPageable);

        // Fetch counts for proper TanStack client-side page state calculation
        long jobCount = searchRepository.countJobPosts(safeQuery);
        long marketCount = searchRepository.countMarketplaceListings(safeQuery);

        return DTO.builder()
                .jobPosts(jobs)
                .marketplaceListings(marketplace)
                .totalJobsFound(jobCount)
                .totalMarketplaceFound(marketCount)
                .build();
    }
}