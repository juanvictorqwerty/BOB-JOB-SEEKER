package com.jobseeker.server.authentication.update.users.personal.updateEmail;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobseeker.server.models.Users;

@Repository
public interface UserInterface extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);
}
