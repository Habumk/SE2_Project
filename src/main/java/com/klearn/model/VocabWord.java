package com.klearn.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vocab_words")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class VocabWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String kr;

    @Column(nullable = false, length = 100)
    private String roman;

    @Column(nullable = false, length = 200)
    private String vi;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(length = 500)
    private String example;
}
