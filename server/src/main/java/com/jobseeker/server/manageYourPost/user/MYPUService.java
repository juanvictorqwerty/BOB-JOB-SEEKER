package com.jobseeker.server.manageYourPost.user;

import com.jobseeker.server.models.MarketPlace;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MYPUService implements MYPUValidation {

    private final MYPUInterface mypuInterface;

    @Override
    @Transactional(readOnly = true)
    public List<MarketPlace> getUserMarketplaceData(UUID userId) {
        // You could inject a UserRepository here to validate if the user exists first,
        // but fetching directly via the indexed foreign key is clean and efficient.
        return mypuInterface.findByPostingUserId(userId);
    }

    @Override
    @Transactional
    public MarketPlace updatePostStatus(@NonNull UUID postId, Boolean isOpen) {
        // 1. Fetch and validate that the listing actually exists
        MarketPlace post = mypuInterface.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Marketplace post not found with id: " + postId));

        // 2. Update the status flag
        post.setIsOpen(isOpen);

        // 3. Save and return (@UpdateTimestamp handles your updatedAt field
        // automatically here)
        return mypuInterface.save(post);
    }
}