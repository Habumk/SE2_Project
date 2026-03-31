package com.klearn.repository;

import com.klearn.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestion_QuestionId(Long questionId);

    Optional<Answer> findFirstByQuestion_QuestionIdAndIsCorrectTrue(Long questionId);
}
