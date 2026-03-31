package com.klearn.dto;

import lombok.Data;

@Data
public class LeaderboardEntryDto {
    private Integer rank;
    private Long userId;
    private String name;
    private Integer currentLevel;
    private Integer totalXp;
    private Integer streakCount;
}
