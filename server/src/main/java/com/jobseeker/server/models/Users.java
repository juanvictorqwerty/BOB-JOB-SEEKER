package com.jobseeker.server.models;

import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.Instant;

@Entity
@Table(
    name = "users",
    indexes = {
        // Index on email: used on every login query and unique-check — must be fast
        @jakarta.persistence.Index(name = "idx_users_email", columnList = "email"),
        // Index on username: used for profile lookups, mentions, and unique-checks
        @jakarta.persistence.Index(name = "idx_users_username", columnList = "username")
    }
)
// @Data generates getters, setters, toString, equals, and hashCode — eliminates boilerplate
// @NoArgsConstructor required by JPA spec (Hibernate needs a no-arg constructor to instantiate entities)
// @AllArgsConstructor convenient for tests and builder patterns
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // UUID auto-generated on insert — no sequence dependency
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @CreationTimestamp // Auto-set by Hibernate on INSERT — immutable, never overwritten
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // ADDED: updatedAt was missing from both the entity and the diagram.
    // Required for: password-reset auditing, cache invalidation, optimistic locking,
    // and detecting stale sessions after profile edits.
    // @UpdateTimestamp auto-populates on every Hibernate UPDATE — no manual setter needed.
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    // Stores the MinIO object key or pre-signed URL for the user's profile picture.
    // length=1024 because MinIO object keys easily exceed the default 255-char limit
    // e.g.: "profiles/{user-uuid}/avatar-{timestamp}.jpg"
    // nullable: users can register without a profile picture
    @Column(name = "profile_picture", length = 1024)
    private String profilePicture;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    // Stores a bcrypt or argon2 hashed password — NEVER store or log plaintext passwords.
    // The hashing is handled in the service layer before this field is set.
    @Column(name = "password", nullable = false)
    private String password;

    // TRUE once the user has verified their email address / completed account activation.
    // Primitive boolean is correct here: this field is never nullable — a user is either checked or not.
    @Column(name = "is_checked", nullable = false)
    private boolean isChecked;

    // Admin-controlled flag. TRUE = user is blocked from logging in or using the platform.
    // Primitive boolean is correct here: also never nullable.
    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;

    // Numeric rank derived from the user's activity or tier — used for display/gamification features.
    // Separate from the Roles table: rank is a dynamic score, role is a static permission group.
    @Column(name = "user_rank", nullable = false)
    private int userRank;

    // @ManyToOne: many users share one role (e.g. all regular users have ROLE_JOB_SEEKER).
    // Links to the Roles table to enable RBAC (Role-Based Access Control).
    //
    // ADDED: the Roles table was designed in the diagram but never connected to Users.
    // Without this FK, the Roles table is an orphaned, unreachable entity — RBAC cannot work.
    //
    // nullable=true: allows user creation before a role is explicitly assigned.
    // The service layer should always set a default role (e.g. ROLE_JOB_SEEKER) on registration.
    @ManyToOne(fetch = FetchType.LAZY) // LAZY: only load role data when explicitly accessed
    @JoinColumn(name = "role_id", nullable = true)
    private Roles role;
}