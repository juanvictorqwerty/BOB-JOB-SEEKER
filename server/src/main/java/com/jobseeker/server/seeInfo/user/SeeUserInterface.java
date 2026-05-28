package com.jobseeker.server.seeInfo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import com.jobseeker.server.models.Users;

public interface SeeUserInterface extends JpaRepository<Users, UUID> {

    Optional<Users> findById(UUID id);
}