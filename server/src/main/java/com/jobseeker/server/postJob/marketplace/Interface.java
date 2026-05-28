package com.jobseeker.server.postJob.marketplace;

import com.jobseeker.server.models.MarketPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface Interface extends JpaRepository<MarketPlace, UUID> {
}