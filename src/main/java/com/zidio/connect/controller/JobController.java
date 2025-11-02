package com.zidio.connect.controller;


import com.zidio.connect.dto.ApiResponse;
import com.zidio.connect.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/public/all")
    public ResponseEntity<ApiResponse> getAllJobs() {
        return ResponseEntity.ok(ApiResponse.success(jobService.getAllActiveJobs()));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse> getJob(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.success(jobService.getJobById(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/public/search")
    public ResponseEntity<ApiResponse> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String location) {
        return ResponseEntity.ok(ApiResponse.success(jobService.searchJobs(keyword, type, location)));
    }
}