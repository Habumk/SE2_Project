package com.klearn.controller;

import com.klearn.dto.ApiResponse;
import com.klearn.dto.WrongAnswerDto;
import com.klearn.security.UserDetailsImpl;
import com.klearn.service.LessonReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonReviewController {

    private final LessonReviewService lessonReviewService;

    // UC-11: Wrong Answer Review data
    @GetMapping("/{id}/wrong-answers")
    public ResponseEntity<ApiResponse<List<WrongAnswerDto>>> getWrongAnswers(
        @PathVariable("id") Long lessonId,
        @AuthenticationPrincipal UserDetailsImpl user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        List<WrongAnswerDto> wrongAnswers = lessonReviewService.getWrongAnswers(lessonId, user.getUserId());
        return ResponseEntity.ok(ApiResponse.success("Success", wrongAnswers));
    }
}

