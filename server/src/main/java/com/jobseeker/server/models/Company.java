package com.jobseeker.server.models;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Company {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID) // Automatically handles UUID generation on insert
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = true)
    private Instant updatedAt;

    @Column(name = "logo_url", nullable = true)
    private String logoUrl;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "jsonb")
    private String description;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name="is_blocked", nullable = false )
    private Boolean isBlocked;

    @Column(name = "admins", nullable = false, columnDefinition = "jsonb")
    private String admins;

    @Column(name= "staff", nullable = true, columnDefinition = "jsonb")
    private String staff;

    @Column(name = "latitude", nullable = true)
    private Double latitude;

    @Column(name = "longitude", nullable = true)
    private Double longitude;

    @Column(name = "city", nullable = false)
    private String city;
}
