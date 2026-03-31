package com.klearn.repository;

import com.klearn.model.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findByUser_UserIdAndQuestion_Exercise_Lesson_LessonIdAndIsCorrectFalse(Long userId, Long lessonId);

    List<UserAnswer> findByUser_UserIdAndQuestion_Exercise_Lesson_LessonId(Long userId, Long lessonId);
}
