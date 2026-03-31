package com.klearn.controller;

import com.klearn.dto.ApiResponse;
import com.klearn.model.Badge;
import com.klearn.security.UserDetailsImpl;
import com.klearn.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    // UC-11: check badges and return newly earned ones
    @PostMapping("/check")
    public ResponseEntity<ApiResponse<List<Badge>>> checkBadges(
        @AuthenticationPrincipal UserDetailsImpl user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }
        List<Badge> newlyEarned = badgeService.checkAndAwardBadgesAndReturn(user.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Success", newlyEarned));
    }
}

