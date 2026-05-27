package com.jobseeker.server.authentication.SignUp.regularUser;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobseeker.server.models.Users;

//This is the interface that will be used to interact with the database
public interface RegSignUpInterface extends JpaRepository<Users, UUID> {

    // Find a user by username
    Users findByUsername(String username);

    // Find a user by email
    Users findByEmail(String email);

}
