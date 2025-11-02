package com.zidio.connect.repository;

import com.zidio.connect.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByRecruiterId(Long recruiterId);
    List<JobPosting> findByStatus(JobPosting.JobStatus status);
    List<JobPosting> findByType(JobPosting.JobType type);
}
