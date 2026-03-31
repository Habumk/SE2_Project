package com.klearn.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grammar_lessons")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GrammarLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 50)
    private String level;

    @Column(name = "description", nullable = false, length = 500)
    private String desc;

    /** HTML content for the lesson */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /** JSON array of examples: [{"kr":"...", "vi":"..."}] */
    @Column(columnDefinition = "TEXT")
    private String examples;
}
