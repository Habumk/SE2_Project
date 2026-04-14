package com.klearn.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klearn.exception.ResourceNotFoundException;
import com.klearn.model.Exercise;
import com.klearn.model.Lesson;
import com.klearn.model.LessonResult;
import com.klearn.model.ReadingPassage;
import com.klearn.model.User;
import com.klearn.model.UserProgress;
import com.klearn.security.UserDetailsImpl;
import com.klearn.service.AuthService;
import com.klearn.service.LessonService;
import com.klearn.service.LessonReviewService;
import com.klearn.repository.LessonResultRepository;
import com.klearn.repository.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * UC-05: Lesson Player + Skill Pages + Review + Flashcard
 */
@Controller
@RequestMapping("/lessons")
@RequiredArgsConstructor
@Slf4j
public class LessonController {

    private final LessonService lessonService;
    private final LessonReviewService lessonReviewService;
    private final LessonResultRepository lessonResultRepository;
    private final AuthService authService;
    private final UserProgressRepository userProgressRepository;
    private final ObjectMapper objectMapper;

    // ================= PLAYER =================
    @GetMapping("/{id}/player")
    public String viewLessonPlayer(@PathVariable Long id, Model model,
                                   @AuthenticationPrincipal UserDetailsImpl user,
                                   Authentication auth) {
        log.debug("Loading lesson player for lesson: {}", id);
        try {
            Lesson lesson = lessonService.getLessonById(id);
            addUserToModel(auth, model);
            model.addAttribute("lesson", lesson);
            model.addAttribute("theory", lessonService.getTheoryByLesson(id));
            model.addAttribute("vocabularies", lessonService.getVocabulariesByLesson(id));
            model.addAttribute("currentPage", "courses");

            if (user != null) {
                model.addAttribute("currentUserId", user.getUserId());
            }

            return "lessons/player";
        } catch (ResourceNotFoundException e) {
            log.warn("Lesson not found: {}", id);
            return "redirect:/courses";
        }
    }

    // ================= LISTENING =================
    @GetMapping("/{id}/listening")
    @Transactional(readOnly = true)
    public String viewListening(@PathVariable Long id, Model model, Authentication auth) {
        log.debug("Loading listening exercises for lesson: {}", id);
        try {
            Lesson lesson = lessonService.getLessonById(id);
            addUserToModel(auth, model);
            model.addAttribute("lesson", lesson);
            model.addAttribute("listeningExercises",
                    lessonService.getExercisesByLessonAndType(id, Exercise.ExerciseType.listening));
            model.addAttribute("currentPage", "courses");

            return "pages/listening";
        } catch (ResourceNotFoundException e) {
            log.warn("Lesson not found: {}", id);
            return "redirect:/courses";
        }
    }

    // ================= SPEAKING =================
    @GetMapping("/{id}/speaking")
    public String viewSpeaking(@PathVariable Long id, Model model, Authentication auth) {
        log.debug("Loading speaking exercises for lesson: {}", id);
        try {
            Lesson lesson = lessonService.getLessonById(id);
            addUserToModel(auth, model);
            model.addAttribute("lesson", lesson);
            model.addAttribute("speakingPrompts", lessonService.getSpeakingPromptsByLesson(id));
            model.addAttribute("currentPage", "courses");

            return "pages/speaking";
        } catch (ResourceNotFoundException e) {
            log.warn("Lesson not found: {}", id);
            return "redirect:/courses";
        }
    }

    // ================= READING =================
    @GetMapping("/{id}/reading")
    public String viewReading(@PathVariable Long id, Model model, Authentication auth) {
        log.debug("Loading reading exercises for lesson: {}", id);
        try {
            Lesson lesson = lessonService.getLessonById(id);
            addUserToModel(auth, model);
            model.addAttribute("lesson", lesson);
            model.addAttribute("readingPassages", lessonService.getReadingPassagesByLesson(id));
            model.addAttribute("readingQuestionsByPassage", buildReadingQuestions(id));
            model.addAttribute("currentPage", "courses");

            return "pages/reading";
        } catch (ResourceNotFoundException e) {
            log.warn("Lesson not found: {}", id);
            return "redirect:/courses";
        }
    }

    // ================= WRITING =================
    @GetMapping("/{id}/writing")
    public String viewWriting(@PathVariable Long id, Model model, Authentication auth) {
        log.debug("Loading writing exercises for lesson: {}", id);
        try {
            Lesson lesson = lessonService.getLessonById(id);
            addUserToModel(auth, model);
            model.addAttribute("lesson", lesson);
            model.addAttribute("writingChars", lessonService.getWritingCharExercisesByLesson(id));
            model.addAttribute("writingTranslations", lessonService.getWritingTranslateExercisesByLesson(id));
            model.addAttribute("currentPage", "courses");

            return "pages/writing";
        } catch (ResourceNotFoundException e) {
            log.warn("Lesson not found: {}", id);
            return "redirect:/courses";
        }
    }

    // ================= REVIEW =================
    @GetMapping("/{id}/review")
    public String viewLessonReview(@PathVariable Long id, Model model,
                                   @AuthenticationPrincipal UserDetailsImpl user,
                                   Authentication auth) {
        log.debug("Loading review for lesson: {}", id);
        try {
            Lesson lesson = lessonService.getLessonById(id);
            addUserToModel(auth, model);
            model.addAttribute("lesson", lesson);
            model.addAttribute("currentPage", "courses");

            if (user != null) {
                Long userId = user.getUserId();

                LessonResult result = lessonResultRepository
                        .findByUser_UserIdAndLesson_LessonId(userId, id)
                        .orElse(null);

                model.addAttribute("lessonResult", result);
                List<com.klearn.dto.WrongAnswerDto> wrongAnswers = lessonReviewService.getWrongAnswers(id, userId);
                Map<String, List<com.klearn.dto.WrongAnswerDto>> wrongAnswersByType = wrongAnswers.stream()
                        .collect(Collectors.groupingBy(
                                answer -> answer.getExerciseType() != null ? answer.getExerciseType() : "unknown",
                                java.util.LinkedHashMap::new,
                                Collectors.toList()
                        ));

                model.addAttribute("wrongAnswers", wrongAnswers);
                model.addAttribute("wrongAnswersByType", wrongAnswersByType);
            }

            return "lessons/review";
        } catch (ResourceNotFoundException e) {
            log.warn("Lesson not found: {}", id);
            return "redirect:/courses";
        }
    }

    // ================= FLASHCARD =================
    @GetMapping("/{id}/flashcard")
    public String viewLessonFlashcard(@PathVariable Long id, Model model, Authentication auth) {
        log.debug("Loading flashcard for lesson: {}", id);
        try {
            Lesson lesson = lessonService.getLessonById(id);
            addUserToModel(auth, model);
            model.addAttribute("lesson", lesson);
            model.addAttribute("currentPage", "courses");

            return "lessons/flashcard";
        } catch (ResourceNotFoundException e) {
            log.warn("Lesson not found: {}", id);
            return "redirect:/courses";
        }
    }

    private Map<Long, List<Map<String, Object>>> buildReadingQuestions(Long lessonId) {
        Map<Long, List<Map<String, Object>>> questionsByPassage = new LinkedHashMap<>();

        for (ReadingPassage passage : lessonService.getReadingPassagesByLesson(lessonId)) {
            try {
                List<Map<String, Object>> questions = objectMapper.readValue(
                        passage.getQuestions(),
                        new TypeReference<List<Map<String, Object>>>() {}
                );
                questionsByPassage.put(passage.getId(), questions);
            } catch (Exception e) {
                questionsByPassage.put(passage.getId(), new ArrayList<>());
            }
        }

        return questionsByPassage;
    }

    private void addUserToModel(Authentication auth, Model model) {
        if (auth == null) {
            return;
        }

        User user = authService.findByEmail(auth.getName());
        if (user == null) {
            return;
        }

        model.addAttribute("user", user);
        UserProgress progress = userProgressRepository.findByUser(user).orElse(null);
        model.addAttribute("progress", progress);
    }
}
