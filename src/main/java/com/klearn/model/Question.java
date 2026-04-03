package com.klearn.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    @ToString.Exclude // Tránh lỗi vòng lặp vô tận khi log dữ liệu
    private Exercise exercise;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "expected_text", length = 500)
    private String expectedText;

    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    /**
     * Danh sách các câu trả lời lựa chọn.
     * Sử dụng @OrderBy để đảm bảo index của câu trả lời không bị thay đổi
     * giữa các lần truy vấn, giúp logic tìm correctIndex luôn đúng.
     */
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("answerId ASC")
    private List<Answer> answers;
}