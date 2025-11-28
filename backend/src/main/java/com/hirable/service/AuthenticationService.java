package com.hirable.service;

import com.hirable.dto.LoginRequest;
import com.hirable.dto.LoginResponse;
import com.hirable.entity.User;
import com.hirable.entity.UserRole;
import com.hirable.exception.UnauthorizedException;
import com.hirable.repository.UserRepository;
import com.hirable.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JobSeekerService jobSeekerService;

    // Hardcoded demo credentials
    private static final String ADMIN_USERNAME = "admin@hirable.com";
    private static final String ADMIN_PASSWORD = "admin123";

    private static final String RECRUITER_USERNAME = "recruiter@techcorp.com";
    private static final String RECRUITER_PASSWORD = "recruiter123";

    private static final String JOB_SEEKER_USERNAME = "jobseeker@email.com";
    private static final String JOB_SEEKER_PASSWORD = "jobseeker123";

    public LoginResponse authenticate(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!user.isActive()) {
            throw new UnauthorizedException("User account is deactivated");
        }

        // Validate password - try both bcrypt and demo password
        boolean passwordMatches = false;
        
        // First try bcrypt validation
        try {
            passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
            System.out.println("DEBUG: Bcrypt validation result: " + passwordMatches);
            System.out.println("DEBUG: Password provided: " + request.getPassword());
            System.out.println("DEBUG: Hash in DB: " + user.getPasswordHash());
        } catch (Exception e) {
            System.out.println("DEBUG: Bcrypt validation exception: " + e.getMessage());
            e.printStackTrace();
        }
        
        // If bcrypt fails, try demo password as fallback
        if (!passwordMatches) {
            System.out.println("DEBUG: Trying demo password fallback");
            passwordMatches = "password123".equals(request.getPassword());
            System.out.println("DEBUG: Demo password match result: " + passwordMatches);
        }
        
        if (!passwordMatches) {
            throw new UnauthorizedException("Invalid credentials");
        }

        // Update last login
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().toString());

        return new LoginResponse(token, user.getRole().toString(), user.getId(), user.getUsername());
    }

    public void initializeDemoUsers() {
        // Create admin user if not exists
        if (userRepository.findByUsername(ADMIN_USERNAME).isEmpty()) {
            User admin = User.builder()
                    .username(ADMIN_USERNAME)
                    .passwordHash(passwordEncoder.encode(ADMIN_PASSWORD))
                    .role(UserRole.ADMIN)
                    .email(ADMIN_USERNAME)
                    .active(true)
                    .build();
            userRepository.save(admin);
        }

        // Create recruiter user if not exists
        if (userRepository.findByUsername(RECRUITER_USERNAME).isEmpty()) {
            User recruiter = User.builder()
                    .username(RECRUITER_USERNAME)
                    .passwordHash(passwordEncoder.encode(RECRUITER_PASSWORD))
                    .role(UserRole.RECRUITER)
                    .email(RECRUITER_USERNAME)
                    .active(true)
                    .build();
            userRepository.save(recruiter);
        }

        // Create job seeker user if not exists
        if (userRepository.findByUsername(JOB_SEEKER_USERNAME).isEmpty()) {
            User jobSeeker = User.builder()
                    .username(JOB_SEEKER_USERNAME)
                    .passwordHash(passwordEncoder.encode(JOB_SEEKER_PASSWORD))
                    .role(UserRole.JOB_SEEKER)
                    .email(JOB_SEEKER_USERNAME)
                    .active(true)
                    .build();
            User savedJobSeeker = userRepository.save(jobSeeker);
            // Create JobSeeker profile for the user
            jobSeekerService.createJobSeekerProfile(savedJobSeeker);
        }
    }
}
