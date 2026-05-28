package com.jobseeker.server.manageYourPost.user;

import com.jobseeker.server.models.MarketPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MYPUInterface extends JpaRepository<MarketPlace, UUID> {

    // Derived query: Hooks into your 'postingUser' relationship and extracts its
    // 'id'
    List<MarketPlace> findByPostingUserId(UUID userId);
}