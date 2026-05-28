package com.jobseeker.server.postJob.marketplace;

import com.jobseeker.server.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface MarketPlaceUserRepo extends JpaRepository<Users, UUID> {

}
