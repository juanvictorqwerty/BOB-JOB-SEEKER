package com.jobseeker.server.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "job_posts", indexes = {
        // Index on company_id: the most common join — "find all posts by company X"
        @jakarta.persistence.Index(name = "idx_job_posts_company_id", columnList = "company_id"),
        // Index on title: supports the unique constraint below and prefix/exact search
        // queries
        @jakarta.persistence.Index(name = "idx_job_posts_title", columnList = "title"),
        // Index on location: supports geographic filtering
        // Note: for deep JSONB queries, consider a GIN index in production migrations
        @jakarta.persistence.Index(name = "idx_city", columnList = "city")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // UUID auto-generated on insert
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @CreationTimestamp // Auto-set by Hibernate on INSERT — immutable after creation
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // @UpdateTimestamp auto-populates on every Hibernate UPDATE — tracks last edit
    // timestamp
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    // @ManyToOne: many job posts belong to one company.
    //
    // CRITICAL FIX: The field type MUST be the target entity (Company), NOT a raw
    // UUID.
    // The previous code had @Column + @ManyToOne + @JoinColumn all on a 'private
    // UUID companyId' field.
    // This is INVALID JPA — three reasons:
    // 1. @ManyToOne requires the field to be the entity type (Company), not a UUID.
    // 2. @Column and @JoinColumn conflict when placed on the same field.
    // 3. Hibernate throws MappingException at startup — the app never runs.
    //
    // @JoinColumn creates the "company_id" FK column in job_posts.
    // updatable=false: once a job post is assigned to a company, that cannot
    // change.
    @ManyToOne(fetch = FetchType.LAZY) // LAZY: only load company data when explicitly accessed
    @JoinColumn(name = "company_id", nullable = false, updatable = false)
    private Company company;

    // Diagram marks title as (U) — enforcing uniqueness at DB level prevents
    // duplicate job listings
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    // JSONB: flexible schema for description (e.g. {text, requirements, benefits,
    // experience_level})
    // Serialized/deserialized via ObjectMapper in the service layer
    @Column(name = "description", nullable = false, columnDefinition = "jsonb")
    private String description;

    // CHANGED from Double → BigDecimal:
    // Double/Float use binary floating-point which accumulates rounding errors for
    // monetary values
    // e.g., 99.99 stored as Double may become 99.98999999999... in the DB.
    // BigDecimal maps to NUMERIC(15,2) in PostgreSQL — exact decimal arithmetic, no
    // precision loss.
    @Column(name = "salary", nullable = true, precision = 15, scale = 2)
    private BigDecimal salary;

    @Column(name = "city", nullable = false)
    private String city;

    // JSONB: flexible location schema (e.g. {city, country, lat, lon, remote:
    // true})
    @Column(name = "geo_location", nullable = false, columnDefinition = "jsonb")
    private String geoLocation;

    // Added from diagram — this field was designed but missing from the entity.
    // Essential for filtering open vs. closed job postings — the most fundamental
    // business query on this table.
    // Boolean WRAPPER (not primitive) for forward-compatibility if nullable state
    // is ever needed.
    // Defaults to true so new posts are immediately visible/open.
    @Column(name = "is_opened", nullable = false)
    private Boolean isOpened;
}
