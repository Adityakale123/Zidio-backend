package com.zidio.connect.service;


import com.zidio.connect.dto.SystemAnalyticsDto;
import com.zidio.connect.entity.JobPosting;
import com.zidio.connect.entity.User;
import com.zidio.connect.repository.ApplicationRepository;
import com.zidio.connect.repository.JobPostingRepository;
import com.zidio.connect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getPendingUsers() {
        return userRepository.findByStatus(User.Status.PENDING);
    }

    public User approveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(User.Status.APPROVED);
        return userRepository.save(user);
    }

    public User blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(User.Status.BLOCKED);
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public SystemAnalyticsDto getSystemAnalytics() {
        long totalUsers = userRepository.count();
        long totalStudents = userRepository.findByRole(User.Role.STUDENT).size();
        long totalRecruiters = userRepository.findByRole(User.Role.RECRUITER).size();
        long totalJobs = jobPostingRepository.count();
        long activeJobs = jobPostingRepository.findByStatus(JobPosting.JobStatus.ACTIVE).size();
        long totalApplications = applicationRepository.count();

        return new SystemAnalyticsDto(
                totalUsers,
                totalStudents,
                totalRecruiters,
                totalJobs,
                activeJobs,
                totalApplications
        );
    }
}
