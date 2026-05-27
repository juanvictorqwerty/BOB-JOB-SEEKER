package com.jobseeker.server.authentication.login;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobseeker.server.models.Users;

public interface LoginInterface extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);
}
