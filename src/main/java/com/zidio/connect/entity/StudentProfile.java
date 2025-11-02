package com.zidio.connect.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    private String phone;
    private String education;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(name = "resume_url")
    private String resumeUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String college;

    @Column(name = "graduation_year")
    private Integer graduationYear;
}