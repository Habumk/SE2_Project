package com.klearn.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * UC-05: Lesson Player + Review + Flashcard (@Controller – returns Thymeleaf view).
 */
@Controller
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;
    private final LessonReviewService lessonReviewService;
    private final LessonResultRepository lessonResultRepository;

    // UC-05/06/07/08/09/10: Lesson player with tabs
    @GetMapping("/{id}/player")
    public String viewLessonPlayer(@PathVariable Long id, Model model,
                                   @AuthenticationPrincipal UserDetailsImpl user) {
        Lesson lesson = lessonService.getLessonById(id);
        if (lesson == null) return "redirect:/courses";

        model.addAttribute("lesson", lesson);
        model.addAttribute("theory", lessonService.getTheoryByLesson(id));
        model.addAttribute("vocabularies", lessonService.getVocabulariesByLesson(id));
        model.addAttribute("exercises", lessonService.getExercisesByLesson(id));
        model.addAttribute("currentPage", "courses");

        // Pre-populate userId for AJAX calls in player
        if (user != null) {
            model.addAttribute("currentUserId", user.getUserId());
        }
        return "lessons/player";
    }

    // UC-11: Review lesson – score summary + wrong answer review
    @GetMapping("/{id}/review")
    public String viewLessonReview(@PathVariable Long id, Model model,
                                   @AuthenticationPrincipal UserDetailsImpl user) {
        Lesson lesson = lessonService.getLessonById(id);
        if (lesson == null) return "redirect:/courses";

        model.addAttribute("lesson", lesson);
        model.addAttribute("currentPage", "courses");

        if (user != null) {
            Long userId = user.getUserId();

            // UC-11: Total score from lesson_result
            LessonResult result = lessonResultRepository
                .findByUser_UserIdAndLesson_LessonId(userId, id)
                .orElse(null);
            model.addAttribute("lessonResult", result);

            // UC-11a: Wrong Answer Review
            model.addAttribute("wrongAnswers", lessonReviewService.getWrongAnswers(id, userId));
        }
        return "lessons/review";
    }

    // UC-14: Flashcard mode
    @GetMapping("/{id}/flashcard")
    public String viewLessonFlashcard(@PathVariable Long id, Model model) {
        Lesson lesson = lessonService.getLessonById(id);
        if (lesson == null) return "redirect:/courses";

        model.addAttribute("lesson", lesson);
        model.addAttribute("currentPage", "courses");
        return "lessons/flashcard";
    }
}
