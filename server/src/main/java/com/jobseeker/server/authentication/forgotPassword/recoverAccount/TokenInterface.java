package com.jobseeker.server.authentication.forgotPassword.recoverAccount;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobseeker.server.models.Tokens;
import java.util.List;
import com.jobseeker.server.models.Users;

public interface TokenInterface extends JpaRepository<Tokens, UUID> {
    List<Tokens> findByUser(Users user);;
}
