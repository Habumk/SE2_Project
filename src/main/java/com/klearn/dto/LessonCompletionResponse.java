package com.klearn.dto;

import com.klearn.model.Badge;
import lombok.Data;

import java.util.List;

@Data
public class LessonCompletionResponse {
    private boolean completed;
    private Integer totalScore;
    private Integer xpEarned;
    private List<Badge> newlyEarnedBadges;
}

