package com.klearn.service;

import com.klearn.model.Vocabulary;
import com.klearn.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlashcardService {

    private final VocabularyRepository vocabularyRepository;

    /**
     * UC-14: Lấy vocabulary của lesson để hiển thị flashcard.
     */
    public List<Vocabulary> getVocabularyByLesson(Long lessonId) {
        return vocabularyRepository.findByLesson_LessonId(lessonId);
    }
}
