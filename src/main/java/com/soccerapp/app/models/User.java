package com.soccerapp.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="\"users\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "First name cannot be empty")
    @Column(name = "first_name", nullable = false)
    private String fname;

    @NotEmpty(message = "Last name cannot be empty")
    @Column(name = "last_name", nullable = false)
    private String lname;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    @Column(name = "email_id", nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "password cannot be null")
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull(message = "Age cannot be null")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 50, message = "Age must be less than 50")
    @Column(name = "age", nullable = false)
    private int age;

    @NotEmpty(message = "Position cannot be empty")
    @Column(name = "position", nullable = false)
    private String position;

    @NotEmpty(message = "Skill level cannot be empty")
    @Column(name = "skill_level", nullable = false)
    private String skillLevel;

    @NotEmpty(message = "Availability cannot be empty")
    @Column(name = "availability", nullable = false)
    private String availability;

    @NotEmpty(message = "Location cannot be empty")
    @Column(name = "location", nullable = false)
    private String location;

    @NotEmpty(message = "Sex cannot be empty")
    @Column(name = "sex", nullable = false)
    private String sex;

    @Column(name = "team")
    private String team;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

