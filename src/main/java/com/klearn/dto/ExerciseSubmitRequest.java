package com.klearn.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseSubmitRequest {

    // Submit answers per question within the exercise.
    // For each question, client sends:
    // - selectedAnswerId (listening/reading)
    // - writtenResponse (writing)
    // - sttResult (speaking)
    private List<QuestionSubmission> submissions;

    @Data
    public static class QuestionSubmission {
        private Long questionId;
        private Long selectedAnswerId;
        private String writtenResponse;
        private String sttResult;
    }
}

