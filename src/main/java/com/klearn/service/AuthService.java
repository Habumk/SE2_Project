package com.klearn.service;

import com.klearn.exception.AuthenticationException;
import com.klearn.exception.ValidationException;
import com.klearn.model.User;
import com.klearn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.debug("Attempting to register user with email: {}", email);
        
        // E1: Email đã tồn tại
        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Registration attempt with existing email: {}", email);
            throw new AuthenticationException("Email already in use. Please login.");
        }

        // E2: Password yếu
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            log.warn("Registration attempt with weak password for email: {}", email);
            throw new ValidationException(
                "Password must be 8+ chars with uppercase, lowercase and numbers.");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole("ROLE_LEARNER");
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", email);
        return savedUser;
    }

    /**
     * UC-01: Update last_login_at after successful login.
     * Called from SecurityConfig successHandler.
     */
    @Transactional
    public void updateLastLogin(Long userId) {
        log.debug("Updating last login for user: {}", userId);
        userRepository.findById(userId).ifPresentOrElse(user -> {
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
            log.debug("Last login updated for user: {}", userId);
        }, () -> {
            log.warn("User not found for last login update: {}", userId);
        });
    }

    public User findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        return userRepository.findByEmail(email).orElse(null);
    }
}
