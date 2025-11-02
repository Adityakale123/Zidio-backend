package com.zidio.connect.service;


import com.zidio.connect.dto.AuthResponse;
import com.zidio.connect.dto.LoginRequest;
import com.zidio.connect.dto.RegisterRequest;
import com.zidio.connect.entity.RecruiterProfile;
import com.zidio.connect.entity.StudentProfile;
import com.zidio.connect.entity.User;
import com.zidio.connect.repository.RecruiterProfileRepository;
import com.zidio.connect.repository.StudentProfileRepository;
import com.zidio.connect.repository.UserRepository;
import com.zidio.connect.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private RecruiterProfileRepository recruiterProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
        user.setStatus(User.Status.PENDING);

        user = userRepository.save(user);

        // Create profile based on role
        if (user.getRole() == User.Role.STUDENT) {
            StudentProfile profile = new StudentProfile();
            profile.setUserId(user.getId());
            studentProfileRepository.save(profile);
        } else if (user.getRole() == User.Role.RECRUITER) {
            RecruiterProfile profile = new RecruiterProfile();
            profile.setUserId(user.getId());
            profile.setCompanyName(request.getCompanyName());
            recruiterProfileRepository.save(profile);
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().toString());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().toString())
                .status(user.getStatus().toString())
                .userId(user.getId())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (user.getStatus() == User.Status.BLOCKED) {
            throw new RuntimeException("Account is blocked");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().toString());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().toString())
                .status(user.getStatus().toString())
                .userId(user.getId())
                .build();
    }
}
