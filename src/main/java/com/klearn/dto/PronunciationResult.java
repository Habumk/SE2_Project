package com.klearn.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PronunciationResult {
    private String expectedText;
    private String sttResult;
    private BigDecimal score;
    private String feedbackTier;
    private String feedback;
}

