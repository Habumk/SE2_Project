package com.klearn.controller;

import com.klearn.model.Exercise;
import com.klearn.model.Lesson;
import com.klearn.model.LessonResult;
import com.klearn.security.UserDetailsImpl;
import com.klearn.service.LessonService;
import com.klearn.service.LessonReviewService;
import com.klearn.repository.LessonResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * UC-05: Lesson Player + Skill Pages + Review + Flashcard
 */
@Controller
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final LessonReviewService lessonReviewService;
    private final LessonResultRepository lessonResultRepository;

    // ================= PLAYER =================
    @GetMapping("/{id}/player")
    public String viewLessonPlayer(@PathVariable Long id, Model model,
                                   @AuthenticationPrincipal UserDetailsImpl user) {
        Lesson lesson = lessonService.getLessonById(id);
        if (lesson == null) return "redirect:/courses";

        model.addAttribute("lesson", lesson);
        model.addAttribute("theory", lessonService.getTheoryByLesson(id));
        model.addAttribute("vocabularies", lessonService.getVocabulariesByLesson(id));
        model.addAttribute("currentPage", "courses");

        if (user != null) {
            model.addAttribute("currentUserId", user.getUserId());
        }

        return "lessons/player";
    }

    // ================= LISTENING =================
    @GetMapping("/{id}/listening")
    public String viewListening(@PathVariable Long id, Model model) {
        Lesson lesson = lessonService.getLessonById(id);
        if (lesson == null) return "redirect:/courses";

        model.addAttribute("lesson", lesson);

        model.addAttribute("listeningExercises",
                lessonService.getExercisesByLessonAndType(id, Exercise.ExerciseType.listening));

        model.addAttribute("currentPage", "courses");

        return "pages/listening";
    }

    // ================= SPEAKING =================
    @GetMapping("/{id}/speaking")
    public String viewSpeaking(@PathVariable Long id, Model model) {
        Lesson lesson = lessonService.getLessonById(id);
        if (lesson == null) return "redirect:/courses";

        model.addAttribute("lesson", lesson);
        model.addAttribute("exercises", lessonService.getSpeakingExercisesByLesson(id));
        model.addAttribute("currentPage", "courses");

        return "pages/speaking";
    }

    // ================= READING =================
    @GetMapping("/{id}/reading")
    public String viewReading(@PathVariable Long id, Model model) {
        Lesson lesson = lessonService.getLessonById(id);
        if (lesson == null) return "redirect:/courses";

        model.addAttribute("lesson", lesson);
        model.addAttribute("readingPassages", lessonService.getReadingPassagesByLesson(id));
        model.addAttribute("currentPage", "courses");

        return "pages/reading";
    }

    // ================= WRITING =================
    @GetMapping("/{id}/writing")
    public String viewWriting(@PathVariable Long id, Model model) {
        Lesson lesson = lessonService.getLessonById(id);
        if (lesson == null) return "redirect:/courses";

        model.addAttribute("lesson", lesson);
        model.addAttribute("exercises", lessonService.getWritingExercisesByLesson(id));
        model.addAttribute("currentPage", "courses");

        return "pages/writing";
    }

    // ================= REVIEW =================
    @GetMapping("/{id}/review")
    public String viewLessonReview(@PathVariable Long id, Model model,
                                   @AuthenticationPrincipal UserDetailsImpl user) {
        Lesson lesson = lessonService.getLessonById(id);
        if (lesson == null) return "redirect:/courses";

        model.addAttribute("lesson", lesson);
        model.addAttribute("currentPage", "courses");

        if (user != null) {
            Long userId = user.getUserId();

            LessonResult result = lessonResultRepository
                    .findByUser_UserIdAndLesson_LessonId(userId, id)
                    .orElse(null);

            model.addAttribute("lessonResult", result);
            model.addAttribute("wrongAnswers",
                    lessonReviewService.getWrongAnswers(id, userId));
        }

        return "lessons/review";
    }

    // ================= FLASHCARD =================
    @GetMapping("/{id}/flashcard")
    public String viewLessonFlashcard(@PathVariable Long id, Model model) {
        Lesson lesson = lessonService.getLessonById(id);
        if (lesson == null) return "redirect:/courses";

        model.addAttribute("lesson", lesson);
        model.addAttribute("currentPage", "courses");

        return "lessons/flashcard";
    }
}