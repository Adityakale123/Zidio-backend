package com.zidio.connect.service;


import com.zidio.connect.entity.Application;
import com.zidio.connect.entity.Bookmark;
import com.zidio.connect.entity.StudentProfile;
import com.zidio.connect.repository.ApplicationRepository;
import com.zidio.connect.repository.BookmarkRepository;
import com.zidio.connect.repository.JobPostingRepository;
import com.zidio.connect.repository.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class StudentService {

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    public StudentProfile getProfile(Long userId) {
        return studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public StudentProfile updateProfile(Long userId, StudentProfile profileData) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setPhone(profileData.getPhone());
        profile.setEducation(profileData.getEducation());
        profile.setSkills(profileData.getSkills());
        profile.setBio(profileData.getBio());
        profile.setCollege(profileData.getCollege());
        profile.setGraduationYear(profileData.getGraduationYear());

        return studentProfileRepository.save(profile);
    }

    public String uploadResume(Long userId, MultipartFile file) throws IOException {
        String uploadDir = "./uploads/resumes/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.write(filePath, file.getBytes());

        StudentProfile profile = studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setResumeUrl("/uploads/resumes/" + fileName);
        studentProfileRepository.save(profile);

        return profile.getResumeUrl();
    }

    @Transactional
    public Application applyToJob(Long studentId, Long jobId, String coverLetter) {
        // Check if already applied
        Optional<Application> existing = applicationRepository.findByStudentIdAndJobId(studentId, jobId);
        if (existing.isPresent()) {
            throw new RuntimeException("Already applied to this job");
        }

        Application application = new Application();
        application.setStudentId(studentId);
        application.setJobId(jobId);
        application.setCoverLetter(coverLetter);

        return applicationRepository.save(application);
    }

    public List<Application> getMyApplications(Long studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    @Transactional
    public void toggleBookmark(Long studentId, Long jobId) {
        Optional<Bookmark> existing = bookmarkRepository.findByStudentIdAndJobId(studentId, jobId);
        if (existing.isPresent()) {
            bookmarkRepository.delete(existing.get());
        } else {
            Bookmark bookmark = new Bookmark();
            bookmark.setStudentId(studentId);
            bookmark.setJobId(jobId);
            bookmarkRepository.save(bookmark);
        }
    }

    public List<Bookmark> getMyBookmarks(Long studentId) {
        return bookmarkRepository.findByStudentId(studentId);
    }
}
