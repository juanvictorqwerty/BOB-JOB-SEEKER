package com.jobseeker.server.models;

import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Table(
    name = "tokens"
    // We index the value field for faster lookups, especially since it is unique and likely to be used in queries
    // We also index the userId field for faster lookups when validating tokens for a specific user
    , indexes = {
        @jakarta.persistence.Index(name = "idx_tokens_value", columnList = "value"),
        @jakarta.persistence.Index(name = "idx_tokens_user_id", columnList = "user_id")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tokens {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Automatically handles UUID generation on insert
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    //Reference to Users table
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "value", nullable = false, unique = true)
    private String value;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private java.time.Instant createdAt;

    @Column(name = "updated_at", nullable = true, columnDefinition = "UTC")
    private Instant updatedAt;

    @Column(name = "expires_at", nullable = false, columnDefinition = "UTC")
    private java.time.Instant expiresAt;

    @Column(name = "is_valid", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isValid;
}
