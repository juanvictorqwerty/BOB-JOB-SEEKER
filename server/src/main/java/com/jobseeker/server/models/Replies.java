package com.jobseeker.server.models;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.Instant;
import jakarta.persistence.Entity;

@Entity
@Table(name = "replies",
         indexes = {
              @jakarta.persistence.Index(name = "idx_replies_company_id", columnList = "company_id"),
              @jakarta.persistence.Index(name = "idx_replies_user_id", columnList = "user_id")
         }
)
@NoArgsConstructor
@AllArgsConstructor
public class Replies {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Automatically handles UUID generation on insert
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private String id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = true)
    private Instant updatedAt;

    @Column(name = "company_id", nullable = false)
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false, updatable = false)
    private String companyId;

    @Column(name = "user_id", nullable = false)
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private String userId;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "is_accepted", nullable = true)
    private boolean isAccepted;
}
