package com.jobseeker.server.authentication.tokens;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobseeker.server.models.Tokens;

@Repository
public interface TokenRepo extends JpaRepository<Tokens, UUID> {

    // REVOKING ALL TOKENS OF A USER

}
