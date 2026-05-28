package com.jobseeker.server.JobSearch;

import com.jobseeker.server.models.JobPost;
import com.jobseeker.server.models.MarketPlace;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobSearchService {

    private final JobSearchInterface searchRepository;

    public JobSearchService(JobSearchInterface searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Transactional(readOnly = true)
    public DTO unifiedSearch(String textQuery, int page, int size) {
        // Safe string fallback to prevent SQL wildcards matching null rows
        String safeQuery = (textQuery == null) ? "" : textQuery.trim();

        // 10 by 10 structural sort rules based on infinite query offsets
        Pageable jobPageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Pageable marketPageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Fetch independent subsets from both tables simultaneously
        List<JobPost> jobs = searchRepository.searchJobPosts(safeQuery, jobPageable);
        List<MarketPlace> marketplace = searchRepository.searchMarketplaceListings(safeQuery, marketPageable);

        // Fetch totals for infinite client pagination cursor tracking
        long jobCount = searchRepository.countJobPosts(safeQuery);
        long marketCount = searchRepository.countMarketplaceListings(safeQuery);

        // Map JobPost entities to clean UI-friendly structures including target company
        // email
        List<DTO.JobResult> mappedJobs = jobs.stream()
                .map(job -> DTO.JobResult.builder()
                        .id(job.getId())
                        .title(job.getTitle())
                        .city(job.getCity())
                        .salary(job.getSalary())
                        .description(job.getDescription())
                        .geoLocation(job.getGeoLocation())
                        .createdAt(job.getCreatedAt())
                        .email(job.getCompany() != null ? job.getCompany().getEmail() : null)
                        .build())
                .toList();

        // Map MarketPlace entities to clean UI structures including owner/poster email
        List<DTO.MarketplaceResult> mappedMarketplace = marketplace.stream()
                .map(item -> DTO.MarketplaceResult.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .location(item.getLocation())
                        .imageUrl(item.getImageUrl())
                        .createdAt(item.getCreatedAt())
                        .isOpen(item.getIsOpen())
                        .email(item.getPostingUser() != null ? item.getPostingUser().getEmail() : null)
                        .build())
                .toList();

        // Package mapped records alongside pagination metadata back to the controller
        return DTO.builder()
                .jobPosts(mappedJobs)
                .marketplaceListings(mappedMarketplace)
                .totalJobsFound(jobCount)
                .totalMarketplaceFound(marketCount)
                .build();
    }
}