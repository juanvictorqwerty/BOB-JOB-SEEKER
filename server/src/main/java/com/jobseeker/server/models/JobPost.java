package com.jobseeker.server.models;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "job_posts",
       indexes = {
           @jakarta.persistence.Index(name = "idx_job_posts_company_id", columnList = "company_id"),
           @jakarta.persistence.Index(name = "idx_job_posts_title", columnList = "title"),
           @jakarta.persistence.Index(name = "idx_job_posts_location", columnList = "location")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor

public class JobPost {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Automatically handles UUID generation on insert
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = true)
    private Instant updatedAt;

    @Column(name = "company_id", nullable = false)
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false, updatable = false)
    private UUID companyId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "jsonb")
    private String description;

    @Column(name = "salary", nullable = true)
    private Double salary;

    @Column(name = "location", nullable = false, columnDefinition = "jsonb")
    private String location;
}
