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
@Table(name = "marketplace",
       indexes = {
           @jakarta.persistence.Index(name = "idx_marketplace_posting_user_id", columnList = "posting_user_id"),
           @jakarta.persistence.Index(name = "idx_marketplace_location", columnList = "location"),
           @jakarta.persistence.Index(name = "idx_marketplace_title", columnList = "title")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketPlace {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Automatically handles UUID generation on insert
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @CreationTimestamp
    @Column(name="created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = true)
    private Instant updatedAt;

    //Reference to users table to identify who posted the job
    @Column (name="posting_User_Id", nullable = false)
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name= "user_id", nullable = false, updatable = false)
    private UUID postingUserId;

    @Column(name="location", nullable = false, columnDefinition = "jsonb")
    private String location;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "jsonb")
    private String description;

    @Column(name= "image_url", nullable = true, columnDefinition = "jsonb")
    private String imageUrl;

    @Column(name = "is_open", nullable = false)
    private boolean isOpen;
}
