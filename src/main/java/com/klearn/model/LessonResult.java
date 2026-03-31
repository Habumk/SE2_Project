package com.klearn.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "lesson_result")
@Data
@NoArgsConstructor
public class LessonResult {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "reading_score")
    private Integer readingScore = 0;

    @Column(name = "listening_score")
    private Integer listeningScore = 0;

    @Column(name = "speaking_score")
    private Integer speakingScore = 0;

    @Column(name = "writing_score")
    private Integer writingScore = 0;

    @Column(name = "total_score")
    private Integer totalScore = 0;

    @Column(name = "xp_earned")
    private Integer xpEarned = 0;

    @Column(name = "completed_at")
    private LocalDateTime completedAt = LocalDateTime.now();
}
