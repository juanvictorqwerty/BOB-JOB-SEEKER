package com.jobseeker.server.authentication.forgotPassword.recoverAccount;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobseeker.server.models.Users;

public interface RecoverAccountInterface extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);
}
