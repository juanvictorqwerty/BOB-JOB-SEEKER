package com.jobseeker.server.authentication.forgotPassword.sendRecoveryEmail;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobseeker.server.models.Users;

public interface SendRecoveryEmailInterface extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);
}
