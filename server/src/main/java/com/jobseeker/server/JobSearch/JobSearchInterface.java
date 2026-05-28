package com.jobseeker.server.JobSearch;

import com.jobseeker.server.models.JobPost;
import com.jobseeker.server.models.MarketPlace;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobSearchInterface extends JpaRepository<JobPost, UUID> {

    // 1. Search job posts by title (Case-Insensitive)
    @Query("SELECT j FROM JobPost j WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%')) AND j.isOpened = true")
    List<JobPost> searchJobPosts(@Param("query") String query, Pageable pageable);

    // 2. Search marketplace posts by title (Case-Insensitive)
    @Query("SELECT m FROM MarketPlace m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) AND m.isOpen = true")
    List<MarketPlace> searchMarketplaceListings(@Param("query") String query, Pageable pageable);

    // 3. Counting items for tracking metadata
    @Query("SELECT COUNT(j) FROM JobPost j WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%')) AND j.isOpened = true")
    long countJobPosts(@Param("query") String query);

    @Query("SELECT COUNT(m) FROM MarketPlace m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) AND m.isOpen = true")
    long countMarketplaceListings(@Param("query") String query);
}