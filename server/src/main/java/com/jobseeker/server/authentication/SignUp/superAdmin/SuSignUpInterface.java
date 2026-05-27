package com.jobseeker.server.authentication.SignUp.superAdmin;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobseeker.server.models.Users;

public interface SuSignUpInterface extends JpaRepository<Users, UUID> {

    Users findByUsername(String username);

    Users findByEmail(String email);
}
