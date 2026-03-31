package com.klearn.controller;

import com.klearn.dto.ApiResponse;
import com.klearn.model.Vocabulary;
import com.klearn.service.FlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UC-14: Flashcard API – GET /api/lessons/{id}/vocabulary
 * Trả JSON cho AJAX (Thymeleaf flashcard page).
 */
@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class FlashcardController {

    private final FlashcardService flashcardService;

    @GetMapping("/{id}/vocabulary")
    public ResponseEntity<ApiResponse<List<Vocabulary>>> getVocabulary(@PathVariable Long id) {
        List<Vocabulary> vocabs = flashcardService.getVocabularyByLesson(id);
        if (vocabs.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.error("VOCABULARY_NOT_FOUND"));
        }
        return ResponseEntity.ok(ApiResponse.success("Success", vocabs));
    }
}
