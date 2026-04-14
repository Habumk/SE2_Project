package com.klearn.controller;

import com.klearn.model.User;
import com.klearn.model.UserProgress;
import com.klearn.repository.GrammarLessonRepository;
import com.klearn.repository.HangulCharacterRepository;
import com.klearn.repository.UserProgressRepository;
import com.klearn.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final AuthService authService;
    private final UserProgressRepository userProgressRepository;
    private final HangulCharacterRepository hangulCharacterRepository;
    private final GrammarLessonRepository grammarLessonRepository;

    @GetMapping("/legacy-home")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard-legacy")
    public String dashboard(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "dashboard");
        return "pages/dashboard";
    }

    @GetMapping("/hangul")
    public String hangul(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "hangul");
        model.addAttribute("consonants", hangulCharacterRepository.findByTypeOrderByIdAsc("consonants"));
        model.addAttribute("vowels", hangulCharacterRepository.findByTypeOrderByIdAsc("vowels"));
        model.addAttribute("doubleConsonants", hangulCharacterRepository.findByTypeOrderByIdAsc("double"));
        model.addAttribute("compoundVowels", hangulCharacterRepository.findByTypeOrderByIdAsc("compound"));
        return "pages/hangul";
    }

    @GetMapping("/grammar")
    public String grammar(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "grammar");
        model.addAttribute("grammarLessons", grammarLessonRepository.findAll());
        return "pages/grammar";
    }
    @GetMapping("/speaking")
    public String speaking(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "speaking");
        return "pages/speaking";
    }

    @GetMapping("/reading")
    public String reading(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "reading");
        return "pages/reading";
    }

    @GetMapping("/flashcards")
    public String flashcards(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "flashcards");
        return "pages/flashcards";
    }

    @GetMapping("/quiz")
    public String quiz(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "quiz");
        return "pages/quiz";
    }

    @GetMapping("/studyroom")
    public String studyroom(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "studyroom");
        return "pages/studyroom";
    }

    @GetMapping("/speaking-room")
    public String speakingRoom(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "speaking-room");
        return "speaking-room/index";
    }

    @GetMapping("/roadmap")
    public String roadmap(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "roadmap");
        return "pages/roadmap";
    }

    @GetMapping("/test-leaderboard")
    public String testLeaderboard(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "test-leaderboard");
        return "pages/test-leaderboard";
    }

    /**
     * Adds the current user info and progress to the model for layout rendering.
     */
    private void addUserToModel(Authentication auth, Model model) {
        if (auth != null) {
            User user = authService.findByEmail(auth.getName());
            if (user != null) {
                model.addAttribute("user", user);
                UserProgress progress = userProgressRepository.findByUser(user).orElse(null);
                model.addAttribute("progress", progress);
            }
        }
    }
}