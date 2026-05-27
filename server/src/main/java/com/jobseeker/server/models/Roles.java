package com.jobseeker.server.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
// @Table explicitly sets the DB table name to "roles".
// Without this annotation, Hibernate defaults to the class name ("Roles").
// "roles" is a reserved word in some SQL dialects — always quote it with an
// explicit @Table to be safe.
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // UUID auto-generated on insert
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    // Role label displayed in the system — e.g. "ADMIN", "RECRUITER", "JOB_SEEKER"
    // Unique: prevents duplicate role definitions from being created
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    // Numeric rank/privilege level for this role — higher number = higher
    // authority.
    // Used for hierarchical permission checks (e.g. can RECRUITER perform this
    // action?).
    // Unique: no two roles can share the same numeric rank, enforcing a strict
    // hierarchy.
    @Column(name = "number", nullable = false, unique = true)
    private int number;
}
