package com.klearn.controller;

import com.klearn.model.User;
import com.klearn.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final AuthService authService;

    @GetMapping("/dashboard")
    public String viewDashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        User user = authService.findByEmail(authentication.getName());
        model.addAttribute("user", user);
        return "pages/dashboard";
    }
    
    @GetMapping({"/", "/home"})
    public String index() {
        return "pages/index";
    }
}
