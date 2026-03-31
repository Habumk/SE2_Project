package com.klearn.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "theory")
@Data
@NoArgsConstructor
public class Theory {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theory_id")
    private Long theoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
}
