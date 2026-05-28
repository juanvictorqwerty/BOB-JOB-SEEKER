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

    // Added LEFT JOIN FETCH j.company (Assuming company holds your employer email
    // details)
    @Query("SELECT j FROM JobPost j LEFT JOIN FETCH j.company WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%')) AND j.isOpened = true")
    List<JobPost> searchJobPosts(@Param("query") String query, Pageable pageable);

    // Added LEFT JOIN FETCH m.postingUser to cleanly extract user profiles/emails
    @Query("SELECT m FROM MarketPlace m LEFT JOIN FETCH m.postingUser WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) AND m.isOpen = true")
    List<MarketPlace> searchMarketplaceListings(@Param("query") String query, Pageable pageable);

    @Query("SELECT COUNT(j) FROM JobPost j WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%')) AND j.isOpened = true")
    long countJobPosts(@Param("query") String query);

    @Query("SELECT COUNT(m) FROM MarketPlace m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) AND m.isOpen = true")
    long countMarketplaceListings(@Param("query") String query);
}