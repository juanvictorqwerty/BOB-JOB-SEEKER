package com.jobseeker.server.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "companies", indexes = {
        // Index on name: company name is searched frequently — also backs the unique
        // constraint
        @jakarta.persistence.Index(name = "idx_companies_name", columnList = "name"),
        // Index on email: used for login and duplicate-check queries
        @jakarta.persistence.Index(name = "idx_companies_email", columnList = "email"),
        // Index on city: supports geographic filtering of companies by city
        @jakarta.persistence.Index(name = "idx_companies_city", columnList = "city"),
        // Index on is_blocked: admin queries to list all blocked companies must be fast
        @jakarta.persistence.Index(name = "idx_companies_is_blocked", columnList = "is_blocked")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID) // UUID auto-generated on insert
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @CreationTimestamp // Auto-set by Hibernate on INSERT — immutable after creation
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // FIXED: added @UpdateTimestamp so Hibernate auto-populates this field on every
    // UPDATE.
    // Previously the field existed but had no @UpdateTimestamp annotation, meaning
    // it would
    // always remain NULL unless the service layer manually set it — unreliable and
    // error-prone.
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    // Stores the MinIO object key or pre-signed URL for the company's logo image.
    // length=1024 because MinIO object keys easily exceed the default 255-char
    // column limit
    // e.g.: "logos/{company-uuid}/logo-{timestamp}.png"
    // nullable: a company can register without uploading a logo immediately
    @Column(name = "logo_url", nullable = true, length = 1024)
    private String logoUrl;

    // Unique company name — backed by both a unique constraint and the index above
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    // JSONB: flexible description schema — e.g. {"about": "...", "industry":
    // "Tech", "size": "50-200"}
    // Allows adding new description sub-fields without schema migrations
    @Column(name = "description", nullable = false, columnDefinition = "jsonb")
    private String description;

    // Company contact / login email — unique across all companies
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // Admin-controlled flag. TRUE = company is blocked from posting jobs or using
    // the platform.
    // Boolean WRAPPER (not primitive): allows future nullable state if semantics
    // ever change.
    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked;

    // JSONB array of user UUIDs who have admin access to this company account.
    // e.g. ["uuid1", "uuid2"]
    // NOTE: This violates 2NF (repeating group). For strict normalization and FK
    // integrity,
    // extract to a join table: company_admins(company_id UUID FK, user_id UUID FK).
    // Keeping as JSONB for now if schema flexibility is preferred over
    // queryability.
    @Column(name = "admins", nullable = false, columnDefinition = "jsonb")
    private String admins;

    // JSONB array of user UUIDs who are staff members of this company.
    // Same normalization caveat as admins above — consider a join table for
    // production.
    @Column(name = "staff", nullable = true, columnDefinition = "jsonb")
    private String staff;

    // Geographic coordinates for the company's primary location.
    // Used for distance-based job search queries.
    // nullable: not all companies need to expose their exact coordinates
    @Column(name = "latitude", nullable = true)
    private Double latitude;

    @Column(name = "longitude", nullable = true)
    private Double longitude;

    // Primary city for the company — indexed above for geographic filtering
    @Column(name = "city", nullable = false)
    private String city;
}
