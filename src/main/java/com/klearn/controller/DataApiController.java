package com.klearn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klearn.model.*;
import com.klearn.repository.*;
import org.springframework.transaction.annotation.Transactional;
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
    private final ExerciseRepository exerciseRepository;
    private final SpeakingExerciseRepository speakingRepo;
    private final ReadingPassageRepository readingPassageRepository;
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

    @GetMapping("/lessons/{lessonId}/listening")
    @Transactional(readOnly = true) // Quan trọng: Tránh lỗi LazyInitializationException khi truy cập questions/answers
    public ResponseEntity<List<Map<String, Object>>> getListeningByLesson(@PathVariable Long lessonId) {

        // 1. Tìm tất cả Exercise thuộc bài học này và có loại là listening
        List<Exercise> exercises = exerciseRepository.findByLesson_LessonIdAndType(
                lessonId, Exercise.ExerciseType.listening);

        List<Map<String, Object>> result = new ArrayList<>();

        for (Exercise ex : exercises) {
            Map<String, Object> passage = new HashMap<>();
            List<Map<String, Object>> questionsList = new ArrayList<>();

            // 2. Kiểm tra tránh NullPointerException nếu Exercise không có câu hỏi
            if (ex.getQuestions() != null) {
                for (Question q : ex.getQuestions()) {
                    Map<String, Object> qMap = new HashMap<>();
                    qMap.put("q", q.getContent());

                    List<String> options = new ArrayList<>();
                    int correctIndex = -1;

                    // 3. Duyệt danh sách answers (đã được @OrderBy trong Model)
                    List<Answer> answers = q.getAnswers();
                    if (answers != null) {
                        for (int i = 0; i < answers.size(); i++) {
                            Answer a = answers.get(i);
                            options.add(a.getContent());

                            // Kiểm tra isCorrect (TINYINT(1) map sang Boolean)
                            if (Boolean.TRUE.equals(a.getIsCorrect())) {
                                correctIndex = i; // Lưu vị trí 0, 1, 2...
                            }
                        }
                    }

                    qMap.put("options", options);
                    qMap.put("answer", correctIndex); // Index trả về cho JavaScript
                    questionsList.add(qMap);
                }
            }

            // 4. Đóng gói dữ liệu theo cấu trúc JS mong đợi
            passage.put("audioUrl", ex.getAudioUrl());
            passage.put("level", "TOPIK 1"); // Bạn có thể tùy biến lấy từ Exercise nếu có field level
            passage.put("questions", questionsList);

            result.add(passage);
        }

        return ResponseEntity.ok(result);
    }
    @GetMapping("/speaking")
    public ResponseEntity<List<SpeakingExercise>> getSpeaking() {
        return ResponseEntity.ok(speakingRepo.findAll());
    }

    @GetMapping("/lessons/{lessonId}/reading")
    public ResponseEntity<List<Map<String, Object>>> getReadingByLesson(@PathVariable Long lessonId) {

        List<ReadingPassage> passages =
                readingPassageRepository.findByLesson_LessonId(lessonId);

        List<Map<String, Object>> result = new ArrayList<>();

        for (ReadingPassage p : passages) {
            Map<String, Object> map = new HashMap<>();

            map.put("level", p.getLevel());
            map.put("text", p.getText());
            map.put("translation", p.getTranslation());

            try {
                ObjectMapper mapper = new ObjectMapper();

                List<Map<String, Object>> questions =
                        mapper.readValue(
                                p.getQuestions(),
                                new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Object>>>() {}
                        );

                map.put("questions", questions);

            } catch (Exception e) {
                map.put("questions", new ArrayList<>());
            }

            result.add(map);
        }

        return ResponseEntity.ok(result);
    }
}
