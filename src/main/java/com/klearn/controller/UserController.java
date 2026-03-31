package com.klearn.controller;

import com.klearn.dto.ApiResponse;
import com.klearn.model.Badge;
import com.klearn.model.User;
import com.klearn.security.UserDetailsImpl;
import com.klearn.service.AuthService;
import com.klearn.service.BadgeService;
import com.klearn.repository.UserRepository;
import com.klearn.repository.UserBadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST API for user-related actions.
 * @RestController → returns JSON (not Thymeleaf views).
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final BadgeService badgeService;
    private final UserRepository userRepository;
    private final UserBadgeRepository userBadgeRepository;

    /**
     * UC D6: Dark mode preference sync – POST /api/users/theme
     */
    @PostMapping("/theme")
    public ResponseEntity<ApiResponse<Void>> updateTheme(
        @RequestBody Map<String, String> body,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }
        String theme = body.get("theme");
        if (theme == null || (!theme.equals("light") && !theme.equals("dark"))) {
            theme = "light";
        }
        User user = userRepository.findById(userDetails.getUserId()).orElse(null);
        if (user != null) {
            user.setTheme(theme);
            userRepository.save(user);
        }
        return ResponseEntity.ok(ApiResponse.success("Theme updated", null));
    }

    /**
     * UC-12: GET /api/users/{id}/progress – XP, level, streak summary.
     */
    @GetMapping("/{id}/progress")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserProgress(
        @PathVariable("id") Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(ApiResponse.error("User not found"));
        }
        Map<String, Object> progress = Map.of(
            "totalXp",       user.getTotalXp()       != null ? user.getTotalXp()       : 0,
            "currentLevel",  user.getCurrentLevel()  != null ? user.getCurrentLevel()  : 1,
            "streakCount",   user.getStreakCount()    != null ? user.getStreakCount()    : 0,
            "lastActiveDate", user.getLastActiveDate() != null ? user.getLastActiveDate().toString() : ""
        );
        return ResponseEntity.ok(ApiResponse.success("Success", progress));
    }

    /**
     * UC-12: GET /api/users/{id}/badges
     */
    @GetMapping("/{id}/badges")
    public ResponseEntity<ApiResponse<List<Badge>>> getUserBadges(
        @PathVariable("id") Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }
        List<Badge> badges = userBadgeRepository.findByUser_UserId(userId).stream()
            .map(ub -> ub.getBadge())
            .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Success", badges));
    }
}
