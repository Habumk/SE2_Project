package com.klearn.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hangul_characters")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class HangulCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "char_value", nullable = false, length = 10)
    private String charValue;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20)
    private String roman;

    @Column(name = "description", nullable = false, length = 500)
    private String desc;

    /** Comma-separated examples */
    @Column(length = 1000)
    private String examples;

    /** consonants, vowels, double, compound */
    @Column(nullable = false, length = 20)
    private String type;
}
