package com.jobseeker.server.authentication.tokens.revoque.One;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

import com.jobseeker.server.models.Tokens;

public interface RevOneInterface extends JpaRepository<Tokens, UUID> {
    Tokens findByValue(String value);
}
