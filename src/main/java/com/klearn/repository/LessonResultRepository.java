package com.klearn.repository;

import com.klearn.model.LessonResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonResultRepository extends JpaRepository<LessonResult, Long> {
    List<LessonResult> findByUser_UserId(Long userId);

    Optional<LessonResult> findByUser_UserIdAndLesson_LessonId(Long userId, Long lessonId);
}
