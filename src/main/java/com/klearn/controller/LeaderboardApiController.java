package com.klearn.controller;

import com.klearn.dto.ApiResponse;
import com.klearn.dto.LeaderboardEntryDto;
import com.klearn.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/leaderboard")
    public ResponseEntity<ApiResponse<List<LeaderboardEntryDto>>> getLeaderboard() {
        List<LeaderboardEntryDto> entries = leaderboardService.getTopLeaderboard(20);
        if (entries.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.error("LEADERBOARD_NOT_AVAILABLE"));
        }
        return ResponseEntity.ok(ApiResponse.success("Success", entries));
    }
}
