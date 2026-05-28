package com.jobseeker.server.manageYourPost.user;

import com.jobseeker.server.models.MarketPlace;
import java.util.List;
import java.util.UUID;

public interface MYPUValidation {
    /**
     * Validates if a user exists and retrieves all marketplace posts created by
     * them.
     * 
     * @param userId The UUID of the posting user
     * @return List of MarketPlace listings
     */
    List<MarketPlace> getUserMarketplaceData(UUID userId);

    /**
     * Toggles the status of a marketplace post.
     * 
     * @param postId The UUID of the post
     * @param isOpen The new status to set
     * @return The updated MarketPlace entity
     */
    MarketPlace updatePostStatus(UUID postId, Boolean isOpen);
}