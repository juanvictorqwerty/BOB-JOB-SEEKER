package com.jobseeker.server.models;

import java.util.UUID;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tokens", indexes = {
        // Index on value: token lookup happens on EVERY authenticated request — must be
        // extremely fast
        @jakarta.persistence.Index(name = "idx_tokens_value", columnList = "value"),
        // Index on user_id: enables fast bulk-revocation of all tokens for a given user
        // (e.g. on logout-all-devices or account compromise)
        @jakarta.persistence.Index(name = "idx_tokens_user_id", columnList = "user_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tokens {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // UUID auto-generated on insert
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private UUID id;

    // @ManyToOne: many tokens can belong to one user (one user = multiple
    // devices/sessions).
    //
    // CRITICAL FIX: The field type MUST be the target entity (Users), NOT a raw
    // UUID.
    // The previous code had @Column + @ManyToOne + @JoinColumn all on a 'private
    // UUID userId'.
    // This is INVALID JPA — three reasons:
    // 1. @ManyToOne requires the field to be the entity type (Users), not UUID.
    // 2. @Column and @JoinColumn conflict when placed on the same field.
    // 3. Hibernate throws MappingException at startup — the app never runs.
    //
    // @JoinColumn creates the FK column "user_id" in the tokens table.
    // updatable=false: a token is permanently tied to the user who created it.
    @ManyToOne(fetch = FetchType.LAZY) // LAZY: only load user data when explicitly accessed
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private Users user;

    // The raw token string (JWT access token or opaque refresh token).
    // Unique: prevents duplicate active tokens from being issued.
    // length=512: full JWT strings regularly exceed the default 255-char column
    // limit.
    @Column(name = "value", nullable = false, unique = true, length = 512)
    private String value;

    @CreationTimestamp // Auto-set by Hibernate on INSERT — immutable, no manual assignment needed
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // @UpdateTimestamp auto-populates on every Hibernate UPDATE.
    // FIXED: removed 'columnDefinition = "UTC"' — this is not valid SQL syntax.
    // Instant in Java is always UTC by definition; no DB-level timezone hint is
    // needed here.
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    // The point in time when this token expires and must be rejected by the auth
    // filter.
    // Must be explicitly set by the service layer on token creation (e.g. now + 7
    // days).
    // FIXED: removed 'columnDefinition = "UTC"' — same reason as above; Instant is
    // already UTC.
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    // Boolean WRAPPER (not primitive):
    // TRUE = token is valid and can be used
    // FALSE = token has been explicitly revoked (logout, security event)
    //
    // DB default TRUE means every newly created token is immediately valid.
    // Using Boolean wrapper instead of boolean primitive avoids accidental
    // null-pointer issues
    // and keeps the field consistent with nullable Boolean fields elsewhere in the
    // schema.
    @Column(name = "is_valid", nullable = false)
    private Boolean isValid;
}