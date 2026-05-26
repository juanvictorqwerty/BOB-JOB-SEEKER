package com.jobseeker.server.authentication.update.users.personal.updatePassword;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobseeker.server.models.Users;

public interface UserInterface extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);
}
