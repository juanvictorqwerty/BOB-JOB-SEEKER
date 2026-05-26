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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "marketplace",
       indexes = {
           // Index on posting_user_id: fast lookups of all listings by a specific user
           @jakarta.persistence.Index(name = "idx_marketplace_posting_user_id", columnList = "posting_user_id"),
           // Index on location: supports geographic filtering queries
           // Note: for deep JSONB sub-field queries, consider a GIN index in a Flyway migration
           @jakarta.persistence.Index(name = "idx_marketplace_location", columnList = "location"),
           // Index on title: supports title search and sorting queries
           @jakarta.persistence.Index(name = "idx_marketplace_title", columnList = "title")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // UUID auto-generated on insert
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @CreationTimestamp // Auto-set by Hibernate on INSERT — immutable after creation
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // @UpdateTimestamp auto-populates on every Hibernate UPDATE — tracks listing edits
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    // @ManyToOne: many marketplace listings can be created by one user.
    //
    // CRITICAL FIX: The field type MUST be the target entity (Users), NOT a raw UUID.
    // The previous code had @Column + @ManyToOne + @JoinColumn on a 'private UUID postingUserId'.
    // This is INVALID JPA — three reasons:
    //   1. @ManyToOne requires the field to be the entity type (Users), not UUID.
    //   2. @Column and @JoinColumn conflict when placed on the same field.
    //   3. Hibernate throws MappingException at startup — the app never runs.
    //
    // ALSO FIXED: @JoinColumn name was "user_id" — incorrect, would shadow the FKs in other tables.
    // Changed to "posting_user_id" to uniquely identify this FK in the marketplace table.
    //
    // ALSO FIXED: The old @Column name was "posting_User_Id" (mixed case).
    // PostgreSQL lowercases all unquoted identifiers — mixed-case column names are a silent landmine
    // that causes "column not found" errors when querying with quoted identifiers.
    @ManyToOne(fetch = FetchType.LAZY) // LAZY: only load user data when explicitly accessed
    @JoinColumn(name = "posting_user_id", nullable = false, updatable = false)
    private Users postingUser;

    // JSONB: flexible location schema — e.g. {"city": "Paris", "country": "FR", "remote": false}
    // Serialized/deserialized via ObjectMapper in the service layer
    @Column(name = "location", nullable = false, columnDefinition = "jsonb")
    private String location;

    // Indexed above for search performance — title is the primary lookup field for marketplace listings
    @Column(name = "title", nullable = false)
    private String title;

    // JSONB: flexible description schema — e.g. {"text": "...", "tags": ["electronics", "used"]}
    // Using JSONB allows structured descriptions without schema migrations for new description fields
    @Column(name = "description", nullable = false, columnDefinition = "jsonb")
    private String description;

    // Stores a JSONB array of MinIO object keys for listing images.
    // e.g. ["marketplace/{user-id}/{listing-id}/img1.jpg", "marketplace/{user-id}/{listing-id}/img2.jpg"]
    // nullable: a listing can be posted without images
    // TODO: consider replacing with a @OneToMany FileMetadata relationship for full MinIO metadata support
    @Column(name = "image_url", nullable = true, columnDefinition = "jsonb")
    private String imageUrl;

    // Tracks whether this listing is still active and visible to other users.
    // Boolean WRAPPER (not primitive) for forward-compatibility if nullable state is needed.
    // Defaults to true so new listings are immediately open/visible.
    @Column(name = "is_open", nullable = false)
    private Boolean isOpen = true;
}
