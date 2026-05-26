package com.jobseeker.server.models;

import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.Instant;

@Entity
@Table(
    name = "users",
    //We index email and username for faster lookups, especially since they are unique and likely to be used in queries
    indexes = {
        @jakarta.persistence.Index(name = "idx_users_email", columnList = "email"),
        @jakarta.persistence.Index(name = "idx_users_username", columnList = "username")
    }
)
// The @Data annotation from Lombok generates getters, setters, toString, equals, and hashCode methods automatically, reducing boilerplate code and improving readability. 
// The @NoArgsConstructor and @AllArgsConstructor annotations generate constructors for the class, allowing for easy instantiation with or without parameters. 
// This makes the code cleaner and more maintainable, as we don't have to manually write these methods and constructors.
//  Additionally, using Lombok helps to keep the focus on the core logic of the class rather than on the boilerplate code, enhancing overall readability and maintainability.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Automatically handles UUID generation on insert
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;  

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_checked", nullable = false)
    private boolean isChecked;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;

    @Column(name = "user_rank", nullable = false)
    private int userRank;
}