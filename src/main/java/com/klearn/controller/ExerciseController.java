package com.klearn.controller;

import com.klearn.dto.ApiResponse;
import com.klearn.dto.ExerciseSubmitRequest;
import com.klearn.exception.ResourceNotFoundException;
import com.klearn.service.ExerciseService;
import com.klearn.security.UserDetailsImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
@Slf4j
public class ExerciseController {

    private final ExerciseService exerciseService;

    // UC-11: submit exercise answers for evaluation + persistence into user_answer
    @PostMapping("/{id}/submit")
    public ResponseEntity<ApiResponse<Map<String, Object>>> submitExercise(
        @PathVariable("id") Long exerciseId,
        @RequestBody ExerciseSubmitRequest request,
        @AuthenticationPrincipal UserDetailsImpl user
    ) {
        try {
            Long userId = user != null ? user.getUserId() : null;
            if (userId == null) {
                log.warn("Unauthorized exercise submission attempt");
                return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
            }

            log.debug("Submitting exercise {} by user {}", exerciseId, userId);
            var result = exerciseService.submitAndEvaluate(exerciseId, request, userId);
            log.info("Exercise {} submitted successfully by user {}", exerciseId, userId);
            return ResponseEntity.ok(ApiResponse.success("Success", result));
        } catch (ResourceNotFoundException e) {
            log.warn("Exercise or user not found: {}", e.getMessage());
            return ResponseEntity.status(404).body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid exercise submission: {}", e.getMessage());
            return ResponseEntity.status(400).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error submitting exercise", e);
            return ResponseEntity.status(500).body(ApiResponse.error("Internal server error"));
        }
    }

    @PostMapping("/speaking/evaluate")
    public ResponseEntity<ApiResponse<EvaluationResult>> evaluateSpeaking(@RequestBody SpeakingSubmission submission) {
        try {
            log.debug("Evaluating speaking submission");
            BigDecimal score = exerciseService.calculateSpeakingScore(submission.getExpectedText(), submission.getSttResult());
            EvaluationResult result = new EvaluationResult();
            result.setScore(score);
            // Align backend review logic with pronunciationScore threshold (CLEAR + NATIVE_LIKE => pass)
            result.setPass(score.compareTo(new BigDecimal("65")) >= 0);
            log.debug("Speaking evaluation complete: score={}", score);
            return ResponseEntity.ok(ApiResponse.success("Success", result));
        } catch (Exception e) {
            log.error("Error evaluating speaking submission", e);
            return ResponseEntity.status(500).body(ApiResponse.error("Error evaluating speaking"));
        }
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
