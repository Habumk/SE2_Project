package com.klearn.controller;

import com.klearn.model.*;
import com.klearn.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST API providing learning data to the frontend JavaScript.
 * The JS on each page calls these endpoints to get data from the database
 * instead of the static data.js file.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataApiController {

    private final HangulCharacterRepository hangulRepo;
    private final VocabWordRepository vocabRepo;
    private final GrammarLessonRepository grammarRepo;
    private final ListeningExerciseRepository listeningRepo;
    private final SpeakingExerciseRepository speakingRepo;
    private final ReadingPassageRepository readingRepo;

    @GetMapping("/hangul")
    public ResponseEntity<Map<String, List<HangulCharacter>>> getHangul() {
        Map<String, List<HangulCharacter>> data = new LinkedHashMap<>();
        data.put("consonants", hangulRepo.findByTypeOrderByIdAsc("consonants"));
        data.put("vowels", hangulRepo.findByTypeOrderByIdAsc("vowels"));
        data.put("double", hangulRepo.findByTypeOrderByIdAsc("double"));
        data.put("compound", hangulRepo.findByTypeOrderByIdAsc("compound"));
        return ResponseEntity.ok(data);
    }

    @GetMapping("/vocab")
    public ResponseEntity<List<VocabWord>> getVocab(
            @RequestParam(value = "category", required = false) String category) {
        if (category != null && !category.equals("all")) {
            return ResponseEntity.ok(vocabRepo.findByCategoryOrderByIdAsc(category));
        }
        return ResponseEntity.ok(vocabRepo.findAll());
    }

    @GetMapping("/vocab/categories")
    public ResponseEntity<Map<String, String>> getVocabCategories() {
        Map<String, String> categories = new LinkedHashMap<>();
        categories.put("greeting", "🤝 Chào hỏi");
        categories.put("number", "🔢 Số đếm");
        categories.put("family", "👨\u200D👩\u200D👧\u200D👦 Gia đình");
        categories.put("food", "🍜 Thức ăn");
        categories.put("travel", "✈️ Du lịch");
        categories.put("time", "⏰ Thời gian");
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/grammar")
    public ResponseEntity<List<GrammarLesson>> getGrammar() {
        return ResponseEntity.ok(grammarRepo.findAll());
    }

    @GetMapping("/listening")
    public ResponseEntity<List<ListeningExercise>> getListening() {
        return ResponseEntity.ok(listeningRepo.findAll());
    }

    @GetMapping("/speaking")
    public ResponseEntity<List<SpeakingExercise>> getSpeaking() {
        return ResponseEntity.ok(speakingRepo.findAll());
    }

    @GetMapping("/reading")
    public ResponseEntity<List<ReadingPassage>> getReading() {
        return ResponseEntity.ok(readingRepo.findAll());
    }
}
