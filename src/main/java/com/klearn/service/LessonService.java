package com.klearn.service;

import com.klearn.exception.ResourceNotFoundException;
import com.klearn.model.*;
import com.klearn.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LessonService {

    private final LessonRepository lessonRepository;
    private final TheoryRepository theoryRepository;
    private final VocabularyRepository vocabularyRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserProgressRepository userProgressRepository;
    private final ReadingPassageRepository readingPassageRepository;
    private final SpeakingExerciseRepository speakingExerciseRepository;
    private final WritingCharRepo writingCharRepo;
    private final WritingTranslateRepo writingTranslateRepo;
    private final StreakService streakService;

    // ================= BASIC =================
    public Lesson getLessonById(Long id) {
        log.debug("Fetching lesson with id: {}", id);
        return lessonRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("Lesson not found: {}", id);
                return new ResourceNotFoundException("Lesson", id.toString());
            });
    }

    public Theory getTheoryByLesson(Long lessonId) {
        log.debug("Fetching theory for lesson: {}", lessonId);
        return theoryRepository.findByLesson_LessonId(lessonId)
            .orElseThrow(() -> {
                log.warn("Theory not found for lesson: {}", lessonId);
                return new ResourceNotFoundException("Theory", "lesson_" + lessonId);
            });
    }

    public Iterable<Vocabulary> getVocabulariesByLesson(Long lessonId) {
        log.debug("Fetching vocabularies for lesson: {}", lessonId);
        List<Vocabulary> vocabs = (List<Vocabulary>) vocabularyRepository.findByLesson_LessonId(lessonId);
        log.debug("Found {} vocabularies for lesson: {}", vocabs.size(), lessonId);
        return vocabs;
    }

    public Iterable<Exercise> getExercisesByLesson(Long lessonId) {
        log.debug("Fetching exercises for lesson: {}", lessonId);
        List<Exercise> exercises = (List<Exercise>) exerciseRepository.findByLesson_LessonId(lessonId);
        log.debug("Found {} exercises for lesson: {}", exercises.size(), lessonId);
        return exercises;
    }

    // ================= CORE FILTER (QUAN TRỌNG) =================
    public List<Exercise> getExercisesByLessonAndType(Long lessonId, Exercise.ExerciseType type) {
        log.debug("Fetching {} exercises for lesson: {}", type, lessonId);
        return exerciseRepository.findByLesson_LessonIdAndType(lessonId, type);
    }

    // ================= SKILL METHODS =================
    public List<Exercise> getListeningExercisesByLesson(Long lessonId) {
        log.debug("Fetching listening exercises for lesson: {}", lessonId);
        return getExercisesByLessonAndType(lessonId, Exercise.ExerciseType.listening);
    }

    public List<Exercise> getSpeakingExercisesByLesson(Long lessonId) {
        log.debug("Fetching speaking exercises for lesson: {}", lessonId);
        return getExercisesByLessonAndType(lessonId, Exercise.ExerciseType.speaking);
    }

    public List<SpeakingExercise> getSpeakingPromptsByLesson(Long lessonId) {
        log.debug("Fetching speaking prompts for lesson: {}", lessonId);
        return speakingExerciseRepository.findByLesson_LessonId(lessonId);
    }

    public List<ReadingPassage> getReadingPassagesByLesson(Long lessonId) {
        log.debug("Fetching reading passages for lesson: {}", lessonId);
        return readingPassageRepository.findByLesson_LessonId(lessonId);
    }

    public List<Exercise> getWritingExercisesByLesson(Long lessonId) {
        log.debug("Fetching writing exercises for lesson: {}", lessonId);
        return getExercisesByLessonAndType(lessonId, Exercise.ExerciseType.writing);
    }

    public List<WritingCharExercise> getWritingCharExercisesByLesson(Long lessonId) {
        log.debug("Fetching writing char exercises for lesson: {}", lessonId);
        return writingCharRepo.findByLesson_LessonId(lessonId);
    }

    public List<WritingTranslateExercise> getWritingTranslateExercisesByLesson(Long lessonId) {
        log.debug("Fetching writing translate exercises for lesson: {}", lessonId);
        return writingTranslateRepo.findByLesson_LessonId(lessonId);
    }

    // ================= PROGRESS =================
    @Transactional
    public void markLessonProgress(Long userId, Long lessonId, String status) {

        Optional<UserProgress> progressOpt =
                userProgressRepository.findByUser_UserIdAndLesson_LessonId(userId, lessonId);

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

        // Update streak nếu hoàn thành
        if ("completed".equals(status)) {
            streakService.updateStreak(userId);
        }
    }
}
