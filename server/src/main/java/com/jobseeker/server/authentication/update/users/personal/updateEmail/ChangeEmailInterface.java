package com.jobseeker.server.authentication.update.users.personal.updateEmail;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobseeker.server.models.Tokens;

@Repository
public interface ChangeEmailInterface extends JpaRepository<Tokens, UUID> {
    Tokens findByValue(String value);

}