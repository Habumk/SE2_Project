package com.klearn.service;

import com.klearn.dto.ExerciseSubmitRequest;
import com.klearn.dto.PronunciationResult;
import com.klearn.model.Answer;
import com.klearn.model.Exercise;
import com.klearn.model.Question;
import com.klearn.model.UserAnswer;
import com.klearn.repository.ExerciseRepository;
import com.klearn.repository.AnswerRepository;
import com.klearn.repository.QuestionRepository;
import com.klearn.repository.UserAnswerRepository;
import com.klearn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final LessonCompletionService lessonCompletionService;

    public List<Exercise> getExercisesByLesson(Long lessonId) {
        return exerciseRepository.findByLesson_LessonId(lessonId);
    }

    public List<Question> getQuestionsByExercise(Long exerciseId) {
        return questionRepository.findByExercise_ExerciseId(exerciseId);
    }

    public BigDecimal calculateSpeakingScore(String expected, String result) {
        if (expected == null || result == null) {
            return new BigDecimal("0.00").setScale(2, RoundingMode.HALF_UP);
        }

        String s1 = normalizeForPronunciation(expected);
        String s2 = normalizeForPronunciation(result);

        if (s1.isEmpty() && s2.isEmpty()) return new BigDecimal("100.00");
        if (s1.isEmpty() || s2.isEmpty()) return new BigDecimal("0.00");

        int distance = levenshteinDistance(s1, s2);
        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0) return new BigDecimal("100.00");

        double score = (1.0 - ((double) distance / (double) maxLength)) * 100.0;
        return new BigDecimal(score).setScale(2, RoundingMode.HALF_UP);
    }

    private String normalizeForPronunciation(String value) {
        // Minimal normalization: whitespace only.
        return value.trim().replaceAll("\\s+", " ");
    }

    // UC-09: Pronunciation feedback tiers per SRS spec (D3)
    private String pronunciationFeedbackTier(BigDecimal score) {
        BigDecimal s = score.setScale(2, RoundingMode.HALF_UP);
        if (s.compareTo(new BigDecimal("90")) >= 0) return "EXCELLENT";
        if (s.compareTo(new BigDecimal("70")) >= 0) return "GOOD";
        if (s.compareTo(new BigDecimal("50")) >= 0) return "ALMOST";
        return "NEEDS_PRACTICE";
    }

    private String pronunciationFeedback(BigDecimal score) {
        String tier = pronunciationFeedbackTier(score);
        return switch (tier) {
            case "EXCELLENT"     -> "Excellent! 🎉";
            case "GOOD"          -> "Good job! Keep practicing 👍";
            case "ALMOST"        -> "Almost there! Try again 😊";
            default              -> "Let's try again! 🎙️";
        };
    }

    public Map<String, Object> submitAndEvaluate(Long exerciseId, ExerciseSubmitRequest request, Long userId) {
        var exercise = exerciseRepository.findById(exerciseId).orElse(null);
        if (exercise == null) throw new IllegalArgumentException("Exercise not found");

        var user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new IllegalArgumentException("User not found");

        List<Question> questions = questionRepository.findByExercise_ExerciseId(exerciseId);
        List<ExerciseSubmitRequest.QuestionSubmission> submissions =
            request != null && request.getSubmissions() != null ? request.getSubmissions() : List.of();

        Map<Long, ExerciseSubmitRequest.QuestionSubmission> submissionByQuestionId = submissions.stream()
            .filter(s -> s.getQuestionId() != null)
            .collect(Collectors.toMap(
                ExerciseSubmitRequest.QuestionSubmission::getQuestionId,
                Function.identity(),
                (a, b) -> a
            ));

        int correctCount = 0;
        List<PronunciationResult> pronunciationResults = new ArrayList<>();

        for (Question q : questions) {
            var sub = submissionByQuestionId.get(q.getQuestionId());
            if (sub == null) continue;

            UserAnswer ua = new UserAnswer();
            ua.setUser(user);
            ua.setQuestion(q);
            ua.setSubmittedAt(LocalDateTime.now());

            boolean isCorrect = false;

            if (exercise.getType() == Exercise.ExerciseType.speaking) {
                String expectedText = q.getExpectedText();
                String sttResult = sub.getSttResult();
                BigDecimal score = calculateSpeakingScore(expectedText, sttResult);

                ua.setSttResult(sttResult);
                ua.setPronunciationScore(score);
                ua.setSelectedAnswer(null);
                ua.setWrittenResponse(null);

                // UC-09: pass threshold >= 80% (SRS: XP bonus if score >= 80%)
                isCorrect = score.compareTo(new BigDecimal("80")) >= 0;
                ua.setIsCorrect(isCorrect);

                PronunciationResult pr = new PronunciationResult();
                pr.setExpectedText(expectedText);
                pr.setSttResult(sttResult);
                pr.setScore(score);
                pr.setFeedbackTier(pronunciationFeedbackTier(score));
                pr.setFeedback(pronunciationFeedback(score));
                pronunciationResults.add(pr);
            } else if (exercise.getType() == Exercise.ExerciseType.reading || exercise.getType() == Exercise.ExerciseType.listening) {
                Long selectedAnswerId = sub.getSelectedAnswerId();
                Answer selected = null;
                if (selectedAnswerId != null) {
                    selected = answerRepository.findById(selectedAnswerId).orElse(null);
                }

                ua.setSelectedAnswer(selected);
                ua.setWrittenResponse(null);
                ua.setSttResult(null);
                ua.setPronunciationScore(null);

                isCorrect = selected != null && Boolean.TRUE.equals(selected.getIsCorrect());
                ua.setIsCorrect(isCorrect);
            } else if (exercise.getType() == Exercise.ExerciseType.writing) {
                String writtenResponse = sub.getWrittenResponse();
                ua.setWrittenResponse(writtenResponse);
                ua.setSelectedAnswer(null);
                ua.setSttResult(null);
                ua.setPronunciationScore(null);

                String expectedText = q.getExpectedText();
                boolean correct = expectedText != null && writtenResponse != null
                    && writtenResponse.trim().equalsIgnoreCase(expectedText.trim());

                isCorrect = correct;
                ua.setIsCorrect(isCorrect);
            }

            if (isCorrect) correctCount++;
            userAnswerRepository.save(ua);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("exerciseId", exerciseId);
        result.put("type", exercise.getType().name());
        result.put("totalQuestions", questions.size());
        result.put("correctQuestions", correctCount);
        result.put("passed", correctCount == questions.size());

        // Overall tier feedback for UI
        int total = questions.size();
        double pct = total > 0 ? ((double) correctCount / (double) total) * 100.0 : 0.0;

        String tier;
        String feedback;
        if (pct >= 80.0) {
            tier = "EXCELLENT";
            feedback = "Xuất sắc! Ban đã nắm rất vững kiến thức của bài học này.";
        } else if (pct >= 60.0) {
            tier = "GOOD";
            feedback = "Làm tốt lắm! Hãy ôn tập thêm một chút để đạt điểm tuyệt đối nhé.";
        } else if (pct >= 40.0) {
            tier = "AVERAGE";
            feedback = "Khá ổn! Bạn cần xem lại các lỗi sai để ghi nhớ từ vựng và ngữ pháp lâu hơn.";
        } else {
            tier = "NEEDS_PRACTICE";
            feedback = "Cố gắng lên! Hãy ôn lại bài giảng lý thuyết và thử sức lại lần nữa nhé.";
        }
        result.put("scorePercent", BigDecimal.valueOf(pct).setScale(2, RoundingMode.HALF_UP));
        result.put("feedbackTier", tier);
        result.put("feedback", feedback);
        if (exercise.getType() == Exercise.ExerciseType.speaking) {
            result.put("pronunciationResults", pronunciationResults);
        }

        // If this submission completed all questions of the lesson,
        // persist LessonResult + award XP/level + badges.
        Long lessonId = exercise.getLesson() != null ? exercise.getLesson().getLessonId() : null;
        if (lessonId != null) {
            var lessonCompletion = lessonCompletionService.tryCompleteLesson(userId, lessonId);
            result.put("lessonCompletion", lessonCompletion);
        }

        return result;
    }

    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(dp[i - 1][j - 1] 
                            + costOfSubstitution(s1.charAt(i - 1), s2.charAt(j - 1)), 
                            dp[i - 1][j] + 1, 
                            dp[i][j - 1] + 1);
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private int min(int... numbers) {
        int min = Integer.MAX_VALUE;
        for (int number : numbers) {
            if (number < min) {
                min = number;
            }
        }
        return min;
    }
}
