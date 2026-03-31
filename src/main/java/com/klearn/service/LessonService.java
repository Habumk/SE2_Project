package com.klearn.service;

import com.klearn.model.*;
import com.klearn.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final TheoryRepository theoryRepository;
    private final VocabularyRepository vocabularyRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserProgressRepository userProgressRepository;
    private final StreakService streakService;

    public Lesson getLessonById(Long id) {
        return lessonRepository.findById(id).orElse(null);
    }

    public Theory getTheoryByLesson(Long lessonId) {
        return theoryRepository.findByLesson_LessonId(lessonId).orElse(null);
    }

    public Iterable<Vocabulary> getVocabulariesByLesson(Long lessonId) {
        return vocabularyRepository.findByLesson_LessonId(lessonId);
    }

    public Iterable<Exercise> getExercisesByLesson(Long lessonId) {
        return exerciseRepository.findByLesson_LessonId(lessonId);
    }

    @Transactional
    public void markLessonProgress(Long userId, Long lessonId, String status) {
        Optional<UserProgress> progressOpt = userProgressRepository.findByUser_UserIdAndLesson_LessonId(userId, lessonId);
        
        UserProgress progress;
        if (progressOpt.isPresent()) {
            progress = progressOpt.get();
        } else {
            progress = new UserProgress();
            User user = new User();
            user.setUserId(userId);
            Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
            progress.setUser(user);
            progress.setLesson(lesson);
        }
        
        progress.setStatus(status);
        userProgressRepository.save(progress);

        if ("completed".equals(status)) {
            streakService.updateStreak(userId);
        }
    }
}
