package com.klearn.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WrongAnswerDto {
    private Long questionId;
    private String questionContent;
    private String expectedText;

    private String selectedAnswer;
    private String correctAnswer;
    private String explanation;

    private BigDecimal pronunciationScore;
    private String sttResult;

    private String exerciseType;
}

