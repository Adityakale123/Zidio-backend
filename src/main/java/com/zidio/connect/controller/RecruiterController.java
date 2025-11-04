package com.zidio.connect.controller;


import com.zidio.connect.dto.ApiResponse;
import com.zidio.connect.entity.JobPosting;
import com.zidio.connect.entity.RecruiterProfile;
import com.zidio.connect.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recruiter")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse> getProfile(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(ApiResponse.success(recruiterService.getProfile(userId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse> updateProfile(@PathVariable Long userId, @RequestBody RecruiterProfile profile) {
        try {
            return ResponseEntity.ok(ApiResponse.success(recruiterService.updateProfile(userId, profile)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/jobs/{recruiterId}")
    public ResponseEntity<ApiResponse> createJob(@PathVariable Long recruiterId, @RequestBody JobPosting job) {
        try {
            return ResponseEntity.ok(ApiResponse.success(recruiterService.createJobPosting(recruiterId, job)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/jobs/{recruiterId}")
    public ResponseEntity<ApiResponse> getMyJobs(@PathVariable Long recruiterId) {
        return ResponseEntity.ok(ApiResponse.success(recruiterService.getMyJobPostings(recruiterId)));
    }

    @PutMapping("/jobs/{recruiterId}/{jobId}")
    public ResponseEntity<ApiResponse> updateJob(@PathVariable Long recruiterId, @PathVariable Long jobId, @RequestBody JobPosting job) {
        try {
            return ResponseEntity.ok(ApiResponse.success(recruiterService.updateJobPosting(recruiterId, jobId, job)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/jobs/{recruiterId}/{jobId}")
    public ResponseEntity<ApiResponse> deleteJob(@PathVariable Long recruiterId, @PathVariable Long jobId) {
        try {
            recruiterService.deleteJobPosting(recruiterId, jobId);
            return ResponseEntity.ok(ApiResponse.success("Job deleted", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

@GetMapping("/applications/{recruiterId}/{jobId}")
public ResponseEntity<ApiResponse> getApplications(@PathVariable Long recruiterId, @PathVariable Long jobId) {
    try {
        List<Map<String, Object>> applications = recruiterService.getApplicationsForJob(recruiterId, jobId);
        return ResponseEntity.ok(ApiResponse.success("Applications retrieved successfully", applications));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }
}
    @PutMapping("/application/{recruiterId}/{applicationId}")
    public ResponseEntity<ApiResponse> updateApplicationStatus(@PathVariable Long recruiterId, @PathVariable Long applicationId, @RequestParam String status) {
        try {
            return ResponseEntity.ok(ApiResponse.success(recruiterService.updateApplicationStatus(recruiterId, applicationId, status)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
