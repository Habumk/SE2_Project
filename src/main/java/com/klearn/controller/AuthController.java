package com.klearn.controller;

import com.klearn.dto.RegisterRequest;
import com.klearn.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * UC-01 & UC-02: Authentication pages controller.
 * @Controller → returns Thymeleaf view names (NOT JSON).
 * POST /auth/login is handled by Spring Security formLogin – no @PostMapping needed.
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ── UC-01: Show login page ────────────────────────────────────────────────
    @GetMapping("/login")
    public String showLoginPage(
        @RequestParam(value = "error", required = false)  String error,
        @RequestParam(value = "logout", required = false) String logout,
        Model model
    ) {
        model.addAttribute("activeTab", "login");
        model.addAttribute("registerRequest", new RegisterRequest());

        // UC-01 E1: Spring Security failureUrl appends ?error=true
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password.");
        }
        // UC-03 success message
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out.");
        }

        return "auth/login";
    }

    // ── UC-02: Show register tab ──────────────────────────────────────────────
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("activeTab", "register");
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/login";
    }

    // ── UC-02: Handle registration ────────────────────────────────────────────
    @PostMapping("/register")
    public String register(
        @ModelAttribute RegisterRequest request,
        @RequestParam(value = "confirmPassword", required = false, defaultValue = "") String confirmPassword,
        Model model
    ) {
        model.addAttribute("activeTab", "register");
        model.addAttribute("registerRequest", request);

        // UC-02 E2: Confirm password mismatch
        if (!confirmPassword.equals(request.getPassword())) {
            model.addAttribute("registerError", "Passwords do not match.");
            return "auth/login";
        }

        try {
            authService.register(request.getName(), request.getEmail(), request.getPassword());
            // UC-02 A1: Auto-login after register → redirect to login with success hint
            return "redirect:/auth/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("registerError", e.getMessage());
            return "auth/login";
        }
    }

    // ── UC-02 A2 / UC-01 A2: Forgot password placeholder ─────────────────────
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        // Out-of-scope per SRS Phần G – redirect back to login for now
        return "redirect:/auth/login";
    }
}