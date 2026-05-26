package com.jobseeker.server.models;

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
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Entity
@Table(name = "replies", indexes = {
        // Index on company_id: fast lookup of all replies directed at a specific
        // company
        @jakarta.persistence.Index(name = "idx_replies_company_id", columnList = "company_id"),
        // Index on user_id: fast lookup of all replies submitted by a specific user
        // (applicant)
        @jakarta.persistence.Index(name = "idx_replies_user_id", columnList = "user_id")
})
// @Data generates getters, setters, toString, equals, hashCode — required for
// any service-layer access
// Without @Data, calls like .getFileUrl() or .getCompany() fail to compile
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Replies {

    @Id
    // UUID auto-generated on insert — changed from String to UUID to match diagram
    // design
    // and maintain PK type consistency across all entities (all other PKs are UUID)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @CreationTimestamp // Auto-set by Hibernate on INSERT — never needs manual assignment
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // @UpdateTimestamp auto-populates on every Hibernate UPDATE — tracks when the
    // reply status changes
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    // @ManyToOne: many replies can be addressed to one company.
    //
    // CRITICAL FIX: The field type MUST be the target entity (Company), NOT a raw
    // UUID or String.
    // Using @ManyToOne on a UUID/String field is INVALID JPA — Hibernate throws
    // MappingException on startup because it cannot resolve the association.
    //
    // @JoinColumn defines the FK column "company_id" in the replies table.
    // Do NOT add @Column here — @JoinColumn already owns the column definition;
    // combining @Column + @JoinColumn on the same field causes a column duplication
    // conflict.
    @ManyToOne(fetch = FetchType.LAZY) // LAZY: company data only loaded when explicitly accessed — prevents N+1
    @JoinColumn(name = "company_id", nullable = false, updatable = false)
    private Company company;

    // @ManyToOne: many replies can be submitted by one user (the job applicant /
    // responder).
    // Same fix as above — field must be Users entity, not a raw String or UUID.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private Users user;

    // Stores the MinIO object key or URL pointing to the applicant's submitted file
    // (CV, cover letter, etc.)
    // length=1024 because MinIO object keys easily exceed the default 255-char
    // column length
    // e.g.: "resumes/{user-uuid}/{timestamp}-{original-filename}.pdf"
    @Column(name = "file_url", nullable = false, length = 1024)
    private String fileUrl;

    // Boolean WRAPPER (not primitive) — required because this column has 3 valid
    // states:
    // NULL = application not yet reviewed (pending)
    // TRUE = application approved
    // FALSE = application rejected
    //
    // Using primitive 'boolean' makes NULL impossible (defaults to false),
    // which would incorrectly treat "unreviewed" as "rejected".
    //
    // Renamed from isAccepted → isApproved to match the diagram field name
    // "isApproved"
    @Column(name = "is_approved", nullable = true)
    private Boolean isApproved;
}
