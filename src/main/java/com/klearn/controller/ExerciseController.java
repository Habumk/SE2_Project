package com.klearn.controller;

import com.klearn.dto.ApiResponse;
import com.klearn.dto.ExerciseSubmitRequest;
import com.klearn.service.ExerciseService;
import com.klearn.security.UserDetailsImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    // UC-11: submit exercise answers for evaluation + persistence into user_answer
    @PostMapping("/{id}/submit")
    public ResponseEntity<ApiResponse<Map<String, Object>>> submitExercise(
        @PathVariable("id") Long exerciseId,
        @RequestBody ExerciseSubmitRequest request,
        @AuthenticationPrincipal UserDetailsImpl user
    ) {
        Long userId = user != null ? user.getUserId() : null;
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        var result = exerciseService.submitAndEvaluate(exerciseId, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Success", result));
    }

    @PostMapping("/speaking/evaluate")
    public ResponseEntity<ApiResponse<EvaluationResult>> evaluateSpeaking(@RequestBody SpeakingSubmission submission) {
        BigDecimal score = exerciseService.calculateSpeakingScore(submission.getExpectedText(), submission.getSttResult());
        EvaluationResult result = new EvaluationResult();
        result.setScore(score);
        // Align backend review logic with pronunciationScore threshold (CLEAR + NATIVE_LIKE => pass)
        result.setPass(score.compareTo(new BigDecimal("65")) >= 0);
        return ResponseEntity.ok(ApiResponse.success("Success", result));
    }

    @Data
    static class SpeakingSubmission {
        private String expectedText;
        private String sttResult;
    }

    @Data
    static class EvaluationResult {
        private BigDecimal score;
        private boolean pass;
    }
}
