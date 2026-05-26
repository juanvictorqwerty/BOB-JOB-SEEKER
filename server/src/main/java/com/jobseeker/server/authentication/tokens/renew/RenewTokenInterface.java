package com.jobseeker.server.authentication.tokens.renew;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobseeker.server.models.Tokens;

public interface RenewTokenInterface extends JpaRepository<Tokens, UUID> {

    Tokens findByValue(String value);
}
