package com.klearn.controller;

import com.klearn.model.User;
import com.klearn.model.UserProgress;
import com.klearn.repository.UserProgressRepository;
import com.klearn.repository.VocabWordRepository;
import com.klearn.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class VocabController {

    private final AuthService authService;
    private final UserProgressRepository userProgressRepository;
    private final VocabWordRepository vocabWordRepository;

    @GetMapping("/vocabulary")
    public String vocabulary(Authentication auth, Model model) {
        addUserToModel(auth, model);
        model.addAttribute("currentPage", "vocabulary");
        
        // Fetch all vocabulary words from the database
        var words = vocabWordRepository.findAll();
        model.addAttribute("vocabList", words);
        
        // Extract distinct categories
        var categories = words.stream()
                .map(com.klearn.model.VocabWord::getCategory)
                .distinct()
                .toList();
        model.addAttribute("categories", categories);
        
        return "pages/vocabulary";
    }

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
