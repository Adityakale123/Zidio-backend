
package com.zidio.connect.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_postings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recruiter_id", nullable = false)
    private Long recruiterId;

    @Column(nullable = false)
    private String title;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType type;

    private String location;
    private String salary;
    private String duration;

    @Column(name = "application_deadline")
    private LocalDate applicationDeadline;

    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.ACTIVE;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public enum JobType {
        JOB, INTERNSHIP
    }

    public enum JobStatus {
        ACTIVE, CLOSED, DRAFT
    }
}