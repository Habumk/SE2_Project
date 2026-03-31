package com.klearn.service;

import com.klearn.dto.WrongAnswerDto;
import com.klearn.model.Answer;
import com.klearn.model.UserAnswer;
import com.klearn.repository.AnswerRepository;
import com.klearn.repository.UserAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LessonReviewService {

    private final UserAnswerRepository userAnswerRepository;
    private final AnswerRepository answerRepository;

    // UC-11 Wrong Answer Review (data source for /lessons/{id}/review)
    @Transactional(readOnly = true)
    public List<WrongAnswerDto> getWrongAnswers(Long lessonId, Long userId) {
        // Use latest submission per question, so a corrected retry doesn't keep old wrong answers.
        List<UserAnswer> userAnswers = userAnswerRepository
            .findByUser_UserIdAndQuestion_Exercise_Lesson_LessonId(userId, lessonId);

        Map<Long, UserAnswer> latestByQuestionId = userAnswers.stream()
            .filter(ua -> ua.getQuestion() != null && ua.getQuestion().getQuestionId() != null)
            .collect(java.util.stream.Collectors.toMap(
                ua -> ua.getQuestion().getQuestionId(),
                ua -> ua,
                (a, b) -> {
                    var at = a.getSubmittedAt();
                    var bt = b.getSubmittedAt();
                    if (at == null && bt == null) return a;
                    if (at == null) return b;
                    if (bt == null) return a;
                    return at.isAfter(bt) ? a : b;
                }
            ));

        List<WrongAnswerDto> result = new ArrayList<>();
        for (UserAnswer ua : latestByQuestionId.values()) {
            if (!Boolean.FALSE.equals(ua.getIsCorrect())) continue;
            var q = ua.getQuestion();
            if (q == null) continue;

            WrongAnswerDto dto = new WrongAnswerDto();
            dto.setQuestionId(q.getQuestionId());
            dto.setQuestionContent(q.getContent());
            dto.setExpectedText(q.getExpectedText());

            if (ua.getSelectedAnswer() != null) {
                dto.setSelectedAnswer(ua.getSelectedAnswer().getContent());
                dto.setExplanation(ua.getSelectedAnswer().getExplanation());
            }

            Answer correct = answerRepository
                .findFirstByQuestion_QuestionIdAndIsCorrectTrue(q.getQuestionId())
                .orElse(null);
            if (correct != null) {
                dto.setCorrectAnswer(correct.getContent());
                // Spec wants correct explanation from correct answer.
                dto.setExplanation(correct.getExplanation());
            }

            dto.setPronunciationScore(ua.getPronunciationScore());
            dto.setSttResult(ua.getSttResult());

            if (q.getExercise() != null && q.getExercise().getType() != null) {
                dto.setExerciseType(q.getExercise().getType().name());
            }

            result.add(dto);
        }

        return result;
    }
}

