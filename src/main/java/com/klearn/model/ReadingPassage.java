package com.klearn.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reading_passages")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ReadingPassage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String level;

    /** Korean text passage */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    /** Vietnamese translation */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String translation;

    /** JSON array of questions */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String questions;
}
