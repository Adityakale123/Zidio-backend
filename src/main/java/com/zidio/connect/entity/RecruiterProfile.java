package com.zidio.connect.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recruiter_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_website")
    private String companyWebsite;

    private String phone;

    @Column(name = "company_description", columnDefinition = "TEXT")
    private String companyDescription;

    private String location;
    private String industry;
}