package com.jobseeker.server.authentication.update.users.changeRank;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.jobseeker.server.models.Users;

@Repository
public interface ChangeRankInterface extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);

}
