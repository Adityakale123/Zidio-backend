package com.zidio.connect.controller;

import com.zidio.connect.dto.ApiResponse;
import com.zidio.connect.entity.StudentProfile;
import com.zidio.connect.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse> getProfile(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(ApiResponse.success(studentService.getProfile(userId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse> updateProfile(@PathVariable Long userId, @RequestBody StudentProfile profile) {
        try {
            return ResponseEntity.ok(ApiResponse.success(studentService.updateProfile(userId, profile)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/resume/{userId}")
    public ResponseEntity<ApiResponse> uploadResume(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        try {
            String url = studentService.uploadResume(userId, file);
            return ResponseEntity.ok(ApiResponse.success("Resume uploaded", url));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse> applyToJob(@RequestParam Long studentId, @RequestParam Long jobId, @RequestParam(required = false) String coverLetter) {
        try {
            return ResponseEntity.ok(ApiResponse.success(studentService.applyToJob(studentId, jobId, coverLetter)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/applications/{studentId}")
    public ResponseEntity<ApiResponse> getApplications(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(studentService.getMyApplications(studentId)));
    }

    @PostMapping("/bookmark")
    public ResponseEntity<ApiResponse> toggleBookmark(@RequestParam Long studentId, @RequestParam Long jobId) {
        try {
            studentService.toggleBookmark(studentId, jobId);
            return ResponseEntity.ok(ApiResponse.success("Bookmark toggled", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/bookmarks/{studentId}")
    public ResponseEntity<ApiResponse> getBookmarks(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(studentService.getMyBookmarks(studentId)));
    }
}