package com.klearn.controller;

import com.klearn.dto.ApiResponse;
import com.klearn.dto.LeaderboardEntryDto;
import com.klearn.security.UserDetailsImpl;
import com.klearn.service.LeaderboardService;
import com.klearn.service.XpService;
import com.klearn.service.StreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UC-12: Leaderboard REST API – GET /api/leaderboard
 * Trả JSON cho AJAX từ Thymeleaf leaderboard page.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LeaderboardApiController {

    private final LeaderboardService leaderboardService;
    private final XpService xpService;
    private final StreakService streakService;

    @GetMapping("/leaderboard")
    public ResponseEntity<ApiResponse<List<LeaderboardEntryDto>>> getLeaderboard() {
        List<LeaderboardEntryDto> entries = leaderboardService.getTopLeaderboard(20);
        if (entries.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.error("LEADERBOARD_NOT_AVAILABLE"));
        }
        return ResponseEntity.ok(ApiResponse.success("Success", entries));
    }

    @GetMapping("/leaderboard/test-data")
    public ResponseEntity<ApiResponse<String>> addTestData(@AuthenticationPrincipal UserDetailsImpl user) {
        if (user == null) return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));

        try {
            // Add sample data for testing
            xpService.addSampleDataForTesting(user.getUserId());
            streakService.updateStreak(user.getUserId());

            return ResponseEntity.ok(ApiResponse.success("Test data added successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error("Failed to add test data"));
        }
    }
}