package com.klearn.controller;

import com.klearn.model.User;
import com.klearn.service.AuthService;
import com.klearn.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final AuthService authService;
    private final BadgeService badgeService;

    @GetMapping("/profile")
    public String viewProfile(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        User user = authService.findByEmail(authentication.getName());
        model.addAttribute("user", user);
        if (user != null) {
            model.addAttribute("earnedBadges", badgeService.getUserBadges(user.getUserId()));
        }
        model.addAttribute("currentPage", "profile");
        return "pages/profile";
    }
}
