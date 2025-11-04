package com.zidio.connect.service;

import com.zidio.connect.entity.Application;
import com.zidio.connect.entity.RecruiterProfile;
import com.zidio.connect.entity.JobPosting;
import com.zidio.connect.entity.StudentProfile;
import com.zidio.connect.entity.User;
import com.zidio.connect.repository.ApplicationRepository;
import com.zidio.connect.repository.JobPostingRepository;
import com.zidio.connect.repository.RecruiterProfileRepository;
import com.zidio.connect.repository.StudentProfileRepository;
import com.zidio.connect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecruiterService {

    @Autowired
    private RecruiterProfileRepository recruiterProfileRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private UserRepository userRepository;

    public RecruiterProfile getProfile(Long userId) {
        return recruiterProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public RecruiterProfile updateProfile(Long userId, RecruiterProfile profileData) {
        RecruiterProfile profile = recruiterProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setCompanyName(profileData.getCompanyName());
        profile.setCompanyWebsite(profileData.getCompanyWebsite());
        profile.setPhone(profileData.getPhone());
        profile.setCompanyDescription(profileData.getCompanyDescription());
        profile.setLocation(profileData.getLocation());
        profile.setIndustry(profileData.getIndustry());

        return recruiterProfileRepository.save(profile);
    }

    public JobPosting createJobPosting(Long recruiterId, JobPosting jobPosting) {
        jobPosting.setRecruiterId(recruiterId);
        return jobPostingRepository.save(jobPosting);
    }

    public JobPosting updateJobPosting(Long recruiterId, Long jobId, JobPosting jobData) {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiterId().equals(recruiterId)) {
            throw new RuntimeException("Unauthorized");
        }

        job.setTitle(jobData.getTitle());
        job.setDescription(jobData.getDescription());
        job.setRequirements(jobData.getRequirements());
        job.setLocation(jobData.getLocation());
        job.setSalary(jobData.getSalary());
        job.setDuration(jobData.getDuration());
        job.setApplicationDeadline(jobData.getApplicationDeadline());
        job.setStatus(jobData.getStatus());

        return jobPostingRepository.save(job);
    }

    public void deleteJobPosting(Long recruiterId, Long jobId) {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiterId().equals(recruiterId)) {
            throw new RuntimeException("Unauthorized");
        }

        jobPostingRepository.delete(job);
    }

    public List<JobPosting> getMyJobPostings(Long recruiterId) {
        return jobPostingRepository.findByRecruiterId(recruiterId);
    }

    public List<Map<String, Object>> getApplicationsForJob(Long recruiterId, Long jobId) {
        JobPosting job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiterId().equals(recruiterId)) {
            throw new RuntimeException("Unauthorized");
        }

        List<Application> applications = applicationRepository.findByJobId(jobId);


        return applications.stream()
                .map(app -> {
                    Map<String, Object> details = new HashMap<>();


                    details.put("id", app.getId());
                    details.put("coverLetter", app.getCoverLetter());
                    details.put("status", app.getStatus().toString());
                    details.put("appliedAt", app.getAppliedAt());


                    try {
                        StudentProfile student = studentProfileRepository.findByUserId(app.getStudentId())
                                .orElse(null);

                        if (student != null) {
                            User user = userRepository.findById(app.getStudentId())
                                    .orElse(null);

                            Map<String, Object> studentInfo = new HashMap<>();


                            if (user != null) {
                                studentInfo.put("id", user.getId());
                                studentInfo.put("fullName", user.getFullName());
                                studentInfo.put("email", user.getEmail());
                            }


                            studentInfo.put("phone", student.getPhone());
                            studentInfo.put("education", student.getEducation());
                            studentInfo.put("college", student.getCollege());
                            studentInfo.put("graduationYear", student.getGraduationYear());
                            studentInfo.put("skills", student.getSkills());
                            studentInfo.put("bio", student.getBio());
                            studentInfo.put("resumeUrl", student.getResumeUrl());

                            details.put("student", studentInfo);
                        }
                    } catch (Exception e) {
                        System.err.println("Error fetching student details for application " + app.getId() + ": " + e.getMessage());
                    }

                    return details;
                })
                .collect(Collectors.toList());
    }

    public Application updateApplicationStatus(Long recruiterId, Long applicationId, String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        JobPosting job = jobPostingRepository.findById(application.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getRecruiterId().equals(recruiterId)) {
            throw new RuntimeException("Unauthorized");
        }

        application.setStatus(Application.ApplicationStatus.valueOf(status.toUpperCase()));
        return applicationRepository.save(application);
    }
}