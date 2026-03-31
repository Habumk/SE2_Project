package com.klearn.controller;

import com.klearn.model.User;
import com.klearn.model.UserProgress;
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
        return "pages/hangul";
    }

    @GetMapping("/grammar")
    public String grammar(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "grammar");
        return "pages/grammar";
    }

    @GetMapping("/listening")
    public String listening(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "listening");
        return "pages/listening";
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

    @GetMapping("/writing")
    public String writing(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "writing");
        return "pages/writing";
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
