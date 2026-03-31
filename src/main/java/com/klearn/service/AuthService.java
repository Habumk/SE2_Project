package com.klearn.service;

import com.klearn.model.User;
import com.klearn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * UC-01 & UC-02: Authentication service – register + login helpers.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // UC-02 – password must be 8+ chars with uppercase, lowercase, and digit
    private static final Pattern PASSWORD_PATTERN =
        Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");

    /**
     * UC-02: Register new user.
     * Validates email uniqueness and password strength.
     */
    @Transactional
    public User register(String name, String email, String password) {
        // E1: Email đã tồn tại
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use. Please login.");
        }

        // E2: Password yếu
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new RuntimeException(
                "Password must be 8+ chars with uppercase, lowercase and numbers.");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole("ROLE_LEARNER");
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    /**
     * UC-01: Update last_login_at after successful login.
     * Called from SecurityConfig successHandler.
     */
    @Transactional
    public void updateLastLogin(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
