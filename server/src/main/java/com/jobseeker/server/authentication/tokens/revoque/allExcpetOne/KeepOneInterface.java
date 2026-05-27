package com.jobseeker.server.authentication.tokens.revoque.allExcpetOne;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobseeker.server.models.Tokens;

@Repository
public interface KeepOneInterface extends JpaRepository<Tokens, UUID> {
    Tokens findByValue(String token);

    List<Tokens> findAllByUserId(UUID userId);
}
