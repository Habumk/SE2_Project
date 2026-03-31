package com.klearn.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "listening_exercises")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ListeningExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Korean text to speak */
    @Column(nullable = false, length = 200)
    private String text;

    /** Correct answer in Vietnamese */
    @Column(nullable = false, length = 200)
    private String answer;

    /** JSON array of option strings */
    @Column(nullable = false, length = 1000)
    private String options;
}
