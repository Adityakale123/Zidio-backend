package com.zidio.connect.service;

import com.zidio.connect.entity.JobPosting;
import com.zidio.connect.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    public List<JobPosting> getAllActiveJobs() {
        return jobPostingRepository.findByStatus(JobPosting.JobStatus.ACTIVE);
    }

    public JobPosting getJobById(Long id) {
        return jobPostingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public List<JobPosting> searchJobs(String keyword, String type, String location) {
        List<JobPosting> jobs = jobPostingRepository.findByStatus(JobPosting.JobStatus.ACTIVE);

        if (keyword != null && !keyword.isEmpty()) {
            jobs = jobs.stream()
                    .filter(job -> job.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                            job.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
        }

        if (type != null && !type.isEmpty()) {
            jobs = jobs.stream()
                    .filter(job -> job.getType().toString().equalsIgnoreCase(type))
                    .toList();
        }

        if (location != null && !location.isEmpty()) {
            jobs = jobs.stream()
                    .filter(job -> job.getLocation() != null &&
                            job.getLocation().toLowerCase().contains(location.toLowerCase()))
                    .toList();
        }

        return jobs;
    }
}
