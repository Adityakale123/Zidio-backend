package com.zidio.connect.controller;


import com.zidio.connect.dto.ApiResponse;
import com.zidio.connect.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getAllUsers()));
    }

    @GetMapping("/users/pending")
    public ResponseEntity<ApiResponse> getPendingUsers() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getPendingUsers()));
    }

    @PutMapping("/users/{userId}/approve")
    public ResponseEntity<ApiResponse> approveUser(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(ApiResponse.success(adminService.approveUser(userId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/users/{userId}/block")
    public ResponseEntity<ApiResponse> blockUser(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(ApiResponse.success(adminService.blockUser(userId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            adminService.deleteUser(userId);
            return ResponseEntity.ok(ApiResponse.success("User deleted", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse> getAnalytics() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getSystemAnalytics()));
    }
}
