package com.jobseeker.server.authentication.update.users.personal.updatePassword;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobseeker.server.models.Tokens;

public interface UpdatePasswordInterface extends JpaRepository<Tokens, UUID> {
    Tokens findByValue(String value);
    List<Tokens> findAllByUserId(UUID userId);
}
